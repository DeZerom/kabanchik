package ru.kabanchik.pro.feature.chatDetails.internal

import kotlinx.coroutines.launch
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.pro.domain.chatDetails.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chatDetails.model.ProMessage
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsContract.Event
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsContract.SideEffect
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsContract.State

internal class ProChatDetailsStore(
    private val chatDetailsInteractor: ProChatDetailsInteractor,
    private val userInteractor: UserInteractor
) : BaseCoroutineStore<Event, State, SideEffect>() {
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
        coroutineScope.launch {
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

        coroutineScope.launch {
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
            chatDetailsInteractor.listenMessages().collect {
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