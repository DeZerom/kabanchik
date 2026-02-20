package ru.kabanchik.feature.client.chatDetails.internal

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsInteractor
import ru.kabanchik.client.domain.model.chatDetails.Message
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsContract.Event
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsContract.SideEffect
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsContract.State

internal class ChatDetailsStore(
    private val chatDetailsInteractor: ChatDetailsInteractor,
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler
): BaseCoroutineStore<Event, State, SideEffect>() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        pushSideEffect(SideEffect.Error(errorHandler.handleError(error).defaultMessage))
    }
    
    init {
        initChat()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.MessageTextChanged -> reduceChatState { copy(currentMessage = event.newText) }
            Event.MessageSent -> sendMessage()
        }
    }

    override fun initState(): State {
        return State.Loading
    }

    private fun initChat() {
        coroutineScope.launch(coroutineExceptionHandler) {
            reduceState { State.Loading }
            chatDetailsInteractor.initChat()
            listenMessages()
            val login = userInteractor.getUserLogin()
            reduceState { State.Chat(login = login.orEmpty()) }
        }
    }

    private fun sendMessage() {
        val chatState = currentState as? State.Chat ?: return
        if (chatState.currentMessage.isBlank()) return

        coroutineScope.launch(coroutineExceptionHandler) {
            val message = Message(
                authorLogin = chatState.login,
                text = chatState.currentMessage
            )

            chatDetailsInteractor.sendMessage(message)
            reduceChatState { copy(currentMessage = "") }
        }
    }

    private fun listenMessages() {
        coroutineScope.launch {
            chatDetailsInteractor.listenMessages()
                .catch {
                    pushSideEffect(SideEffect.Error(errorHandler.handleError(it).defaultMessage))
                }.collect {
                    addMessage(it)
                }
        }
    }

    private fun reduceChatState(reducer: State.Chat.() -> State) {
        val chatState = currentState as? State.Chat ?: return
        reduceState { reducer(chatState) }
    }

    private fun addMessage(message: Message) {
        reduceChatState {
            copy(messages = messages + message.toUiState(login))
        }
    }
}