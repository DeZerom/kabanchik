package ru.kabanchik.pro.feature.chatDetails.internal

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.pro.domain.chatDetails.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chatDetails.model.ProMessage
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsContract.Event
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsContract.SideEffect
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsContract.State

internal class ProChatDetailsStore(
    private val chatDetailsInteractor: ProChatDetailsInteractor,
    private val userInteractor: UserInteractor,
    private val errorHandler: ErrorHandler
) : BaseCoroutineStore<Event, State, SideEffect>() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        pushSideEffect(SideEffect.Error(errorHandler.handleError(throwable).defaultMessage))
    }

    override fun initState(): State {
        return State.Loading
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.MessageSent -> sendMessage()
            is Event.MessageTextChanged -> reduceChatState { copy(currentMessage = event.newText) }
        }
    }

    init {
        initChat()
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
            chatDetailsInteractor.sendMessage(
                message = ProMessage(
                    authorLogin = chatState.login,
                    text = chatState.currentMessage
                )
            )
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

    private fun addMessage(message: ProMessage) {
        reduceChatState {
            copy(messages = messages + message.toUiState(login))
        }
    }
}