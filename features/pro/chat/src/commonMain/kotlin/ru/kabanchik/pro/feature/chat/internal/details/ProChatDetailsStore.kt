package ru.kabanchik.pro.feature.chat.internal.details

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
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
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.Event
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.SideEffect
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.State

internal class ProChatDetailsStore(
    private val chatDetailsInteractor: ProChatDetailsInteractor,
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val fileOpener: FileOpener,
    private val sessionId: String,
    private val shouldReconnect: Boolean
) : BaseCoroutineStore<Event, State, SideEffect>() {
    private var fileOpeningJob: Job? = null
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        pushSideEffect(SideEffect.Error(errorHandler.handleError(throwable).defaultMessage))
    }

    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.MessageSent -> sendMessage()
            is Event.FilesSelected -> reduceState {
                copy(selectedFiles = selectedFiles.appendSelectedFiles(event.files))
            }
            is Event.FileRemoved -> reduceState {
                if (isSending) this else copy(
                    selectedFiles = selectedFiles.removeSelectedFile(event.fileId)
                )
            }
            is Event.FileOpenRequested -> openFile(event.fileId)
            is Event.MessageTextChanged -> reduceState { copy(currentMessage = event.newText) }
        }
    }

    init {
        initChat()
    }

    private fun initChat() {
        coroutineScope.launch(coroutineExceptionHandler) {
            reduceState { State(isLoading = true) }
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

                reduceState {
                    copy(
                        currentMessage = if (currentMessage == message) "" else currentMessage,
                        selectedFiles = emptyList(),
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
            bytes = pendingFile.file.bytes,
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
        reduceState {
            copy(messages = messages.upsert(message.toState(currentState.login)))
        }
    }
}
