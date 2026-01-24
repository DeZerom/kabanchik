package ru.kabanchik.client.chatDetails.internal

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kabanchik.client.chatDetails.api.ChatDetailsContract.Event
import ru.kabanchik.client.chatDetails.api.ChatDetailsContract.SideEffect
import ru.kabanchik.client.chatDetails.api.ChatDetailsContract.State
import ru.kabanchik.common.store.BaseCoroutineStore
import kotlin.time.Duration.Companion.seconds

internal class ChatDetailsStore: BaseCoroutineStore<Event, State, SideEffect>() {
    override fun handleEvent(event: Event) {
        when (event) {
            is Event.UserSelected -> initWS()
            is Event.MessageTextChanged -> reduceChatState { copy(currentMessage = event.newText) }
            Event.MessageSent -> sendMessage()
        }
    }

    override fun initState(): State {
        return State.NoLogin
    }

    private fun initWS() {
        coroutineScope.launch {
            reduceState { State.Loading }
            delay(3.seconds)
            reduceState { State.Chat() }
        }
    }

    private fun reduceChatState(reducer: State.Chat.() -> State) {
        val chatState = currentState as? State.Chat ?: return
        reducer(chatState)
    }

    private fun sendMessage() {
        // todo
    }
}