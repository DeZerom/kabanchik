package ru.kabanchik.feature.client.chatDetails.internal.details

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.kabanchik.client.domain.logic.chat.api.ClientChatDetailsInteractor
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.feature.chat.model.CommonPendingFile
import ru.kabanchik.common.features.chat.logic.details.appendSelectedFiles
import ru.kabanchik.common.features.chat.logic.details.chatFileOpenErrorText
import ru.kabanchik.common.features.chat.logic.details.findFile
import ru.kabanchik.common.features.chat.logic.details.removeSelectedFile
import ru.kabanchik.common.features.chat.logic.details.toState
import ru.kabanchik.common.features.chat.logic.details.upsert
import ru.kabanchik.common.features.chat.logic.details.withLoadingFile
import ru.kabanchik.common.files.api.FileOpener
import ru.kabanchik.common.files.api.FileOpeningException
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract.Event
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract.SideEffect
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract.State

internal class ClientChatDetailsStore(
    private val chatDetailsInteractor: ClientChatDetailsInteractor,
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val fileOpener: FileOpener,
    private val sessionId: String,
    private val shouldReconnect: Boolean
): BaseCoroutineStore<Event, State, SideEffect>() {

    private var fileOpeningJob: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        pushSideEffect(SideEffect.Error(errorHandler.handleError(error).defaultMessage))
    }
    
    init {
        initChat()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.MessageTextChanged -> reduceState { copy(currentMessage = event.newText) }
            is Event.FilesSelected -> reduceState {
                copy(selectedFiles = selectedFiles.appendSelectedFiles(event.files))
            }
            is Event.FileRemoved -> removeFile(event.fileId)
            is Event.FileOpenRequested -> openFile(event.fileId)
            Event.MessageSent -> sendMessage()
        }
    }

    override fun initState(): State {
        return State()
    }

    private fun initChat() {
        coroutineScope.launch(coroutineExceptionHandler) {
            reduceState { copy(isLoading = true) }
            val login = userInteractor.getUserLogin()
            reduceState { copy(login = login.orEmpty()) }
            loadMessages()
            listenMessages()
            reconnectIfNeeded()
            reduceState { copy(isLoading = false) }
        }
    }

    private suspend fun loadMessages() {
        if (sessionId.isBlank()) return

        val messages = chatDetailsInteractor.getMessages(sessionId = sessionId)
        reduceState {
            copy(messages = messages.map { it.toState(currentState.login) })
        }
    }

    private suspend fun reconnectIfNeeded() {
        if (shouldReconnect) {
            chatDetailsInteractor.reconnect(sessionId = sessionId)
        }
    }

    private fun sendMessage() {
        if (currentState.isSending || currentState.currentMessage.isBlank() && currentState.selectedFiles.isEmpty()) return

        reduceState { copy(isSending = true) }
        coroutineScope.launch(coroutineExceptionHandler) {
            try {
                val message = currentState.currentMessage
                val files = currentState.selectedFiles
                val attachmentIds = files.map { pendingFile ->
                    uploadFile(pendingFile)
                }

                chatDetailsInteractor.sendMessage(
                    sessionId = sessionId,
                    content = message.takeIf { it.isNotBlank() },
                    attachmentIds = attachmentIds,
                )

                files.forEach { it.file.release() }
                reduceState {
                    copy(
                        currentMessage = if (currentMessage == message) "" else currentMessage,
                        selectedFiles = selectedFiles.filterNot { selectedFile ->
                            files.any { sentFile -> sentFile.id == selectedFile.id }
                        },
                    )
                }
            } finally {
                reduceState { copy(isSending = false) }
            }
        }
    }

    private fun openFile(fileId: String) {
        val file = currentState.messages.findFile(fileId) ?: return

        fileOpeningJob?.cancel()
        reduceState { copy(messages = messages.withLoadingFile(fileId)) }

        val job = coroutineScope.launch {
            try {
                fileOpener.open(
                    url = file.downloadUrl,
                    fileName = file.originalName,
                )
            } catch (_: FileOpeningException) {
                pushSideEffect(SideEffect.Error(chatFileOpenErrorText()))
            }
        }
        fileOpeningJob = job
        job.invokeOnCompletion {
            if (fileOpeningJob === job) {
                fileOpeningJob = null
                reduceState { copy(messages = messages.withLoadingFile(fileId = null)) }
            }
        }
    }

    private suspend fun uploadFile(pendingFile: CommonPendingFile): String {
        pendingFile.attachmentId?.let { return it }

        val attachmentId = chatDetailsInteractor.uploadFile(
            sessionId = sessionId,
            fileName = pendingFile.file.fileName,
            contentType = pendingFile.file.contentType,
            file = pendingFile.file,
        ).fileId

        reduceState {
            copy(
                selectedFiles = selectedFiles.map { file ->
                    if (file.id == pendingFile.id) file.copy(attachmentId = attachmentId) else file
                }
            )
        }
        return attachmentId
    }

    private fun removeFile(fileId: String) {
        if (currentState.isSending) return
        currentState.selectedFiles.firstOrNull { it.id == fileId }?.file?.release()
        reduceState { copy(selectedFiles = selectedFiles.removeSelectedFile(fileId)) }
    }

    private fun listenMessages() {
        coroutineScope.launch {
            chatDetailsInteractor.listenMessages(sessionId = sessionId)
                .catch {
                    pushSideEffect(SideEffect.Error(errorHandler.handleError(it).defaultMessage))
                }.collect {
                    addMessage(it)
                }
        }
    }

    private fun addMessage(message: CommonChatMessage) {
        val uiMessage = message.toState(currentState.login)

        reduceState {
            copy(messages = messages.upsert(uiMessage))
        }
    }

    override fun onDestroy() {
        val files = currentState.selectedFiles
        super.onDestroy()
        files.forEach { it.file.release() }
    }
}
