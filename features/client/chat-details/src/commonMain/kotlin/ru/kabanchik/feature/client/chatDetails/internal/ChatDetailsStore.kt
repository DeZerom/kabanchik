package ru.kabanchik.feature.client.chatDetails.internal

import kotlinx.coroutines.launch
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsInteractor
import ru.kabanchik.client.domain.model.chatDetails.Message
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsContract

internal class ChatDetailsStore(
    private val chatDetailsInteractor: ChatDetailsInteractor
): BaseCoroutineStore<ChatDetailsContract.Event, ChatDetailsContract.State, ChatDetailsContract.SideEffect>() {
    override fun handleEvent(event: ChatDetailsContract.Event) {
        when (event) {
            is ChatDetailsContract.Event.UserSelected -> initChat(event.userLogin)
            is ChatDetailsContract.Event.MessageTextChanged -> reduceChatState { copy(currentMessage = event.newText) }
            ChatDetailsContract.Event.MessageSent -> sendMessage()
        }
    }

    override fun initState(): ChatDetailsContract.State {
        return ChatDetailsContract.State.NoLogin
    }

    private fun initChat(login: String) {
        coroutineScope.launch {
            reduceState { ChatDetailsContract.State.Loading }
            chatDetailsInteractor.initChat()
            listenMessages()
            reduceState { ChatDetailsContract.State.Chat(login = login) }
        }
    }

    private fun sendMessage() {
        val chatState = currentState as? ChatDetailsContract.State.Chat ?: return
        if (chatState.currentMessage.isBlank()) return

        coroutineScope.launch {
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
            chatDetailsInteractor.listenMessages().collect {
                addMessage(it)
            }
        }
    }

    private fun reduceChatState(reducer: ChatDetailsContract.State.Chat.() -> ChatDetailsContract.State) {
        val chatState = currentState as? ChatDetailsContract.State.Chat ?: return
        reduceState { reducer(chatState) }
    }

    private fun addMessage(message: Message) {
        reduceChatState {
            copy(messages = messages + message.toUiState(login))
        }
    }
}