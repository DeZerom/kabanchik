package ru.kabanchik.feature.client.chatDetails.internal.details

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.kabanchik.client.domain.logic.chat.api.ClientChatDetailsInteractor
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.features.chat.logic.details.toState
import ru.kabanchik.common.features.chat.logic.details.upsert
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract.Event
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract.SideEffect
import ru.kabanchik.feature.client.chatDetails.api.details.ChatDetailsContract.State

internal class ClientChatDetailsStore(
    private val chatDetailsInteractor: ClientChatDetailsInteractor,
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler,
    private val sessionId: String,
    private val shouldReconnect: Boolean
): BaseCoroutineStore<Event, State, SideEffect>() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        pushSideEffect(SideEffect.Error(errorHandler.handleError(error).defaultMessage))
    }
    
    init {
        initChat()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.MessageTextChanged -> reduceState { copy(currentMessage = event.newText) }
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
        if (currentState.currentMessage.isBlank()) return

        coroutineScope.launch(coroutineExceptionHandler) {
            val message = currentState.currentMessage
            chatDetailsInteractor.sendMessage(
                sessionId = sessionId,
                content = message,
                attachmentIds = emptyList(),
            )
            reduceState { copy(currentMessage = "") }
        }
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
}
