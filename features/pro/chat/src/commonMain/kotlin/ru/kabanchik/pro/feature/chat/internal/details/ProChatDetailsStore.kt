package ru.kabanchik.pro.feature.chat.internal.details

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.feature.chat.model.CommonPendingFile
import ru.kabanchik.common.features.chat.logic.details.toState
import ru.kabanchik.common.features.chat.logic.details.upsert
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.Event
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.SideEffect
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.State

internal class ProChatDetailsStore(
    private val chatDetailsInteractor: ProChatDetailsInteractor,
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val sessionId: String,
    private val shouldReconnect: Boolean
) : BaseCoroutineStore<Event, State, SideEffect>() {
    private var isSending = false

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
                copy(selectedFiles = selectedFiles + event.files.map(::CommonPendingFile))
            }
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
        if (isSending || currentState.currentMessage.isBlank() && currentState.selectedFiles.isEmpty()) return

        isSending = true
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
                        selectedFiles = selectedFiles.drop(files.size),
                    )
                }
            } finally {
                isSending = false
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
                    if (file === pendingFile) file.copy(attachmentId = attachmentId) else file
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
