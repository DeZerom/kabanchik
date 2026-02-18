package ru.kabanchik.feature.client.chatDetails.api

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatDetailsContract {
    sealed class State {
        object Loading : State()
        data class Chat(
            val login: String = "",
            val currentMessage: String = "",
            val messages: List<Message> = emptyList()
        ) : State()
    }

    sealed interface Event {
        class MessageTextChanged(val newText: String) : Event
        object MessageSent : Event
    }

    sealed interface SideEffect

    @OptIn(ExperimentalUuidApi::class)
    data class Message(
        val id: String = Uuid.random().toString(),
        val date: String = "",
        val time: String = "",
        val isUserAuthor: Boolean = false,
        val text: String = "",
        val authorLogin: String = ""
    )
}