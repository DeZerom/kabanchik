package ru.kabanchik.feature.client.chatDetails.internal

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsContract
import kotlin.time.Duration.Companion.seconds

internal class ChatDetailsStore: BaseCoroutineStore<ChatDetailsContract.Event, ChatDetailsContract.State, ChatDetailsContract.SideEffect>() {
    override fun handleEvent(event: ChatDetailsContract.Event) {
        when (event) {
            is ChatDetailsContract.Event.UserSelected -> initWS()
            is ChatDetailsContract.Event.MessageTextChanged -> reduceChatState { ChatDetailsContract.State.Chat(currentMessage = event.newText) }
            ChatDetailsContract.Event.MessageSent -> sendMessage()
        }
    }

    override fun initState(): ChatDetailsContract.State {
        return ChatDetailsContract.State.NoLogin
    }

    private fun initWS() {
        coroutineScope.launch {
            reduceState { ChatDetailsContract.State.Loading }
            delay(3.seconds)
            reduceState { ChatDetailsContract.State.Chat() }
        }
    }

    private fun reduceChatState(reducer: ChatDetailsContract.State.Chat.() -> ChatDetailsContract.State) {
        val chatState = currentState as? ChatDetailsContract.State.Chat ?: return
        reducer(chatState)
    }

    private fun sendMessage() {
        // todo
    }
}