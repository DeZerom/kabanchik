package ru.kabanchik.pro.feature.chatDetails.api

import ru.kabanchik.common.tools.textResource.TextResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProChatDetailsContract {
    sealed class State {
        object Loading : State()
        data class Chat(
            val login: String = "",
            val currentMessage: String = "",
            val messages: List<ProMessage> = emptyList()
        ) : State()
    }

    sealed interface Event {
        class MessageTextChanged(val newText: String) : Event
        object MessageSent : Event
    }

    sealed interface SideEffect {
        class Error(val text: TextResource) : SideEffect
    }

    @OptIn(ExperimentalUuidApi::class)
    data class ProMessage(
        val id: String = Uuid.random().toString(),
        val date: String = "",
        val time: String = "",
        val isUserAuthor: Boolean = false,
        val text: String = "",
        val authorLogin: String = ""
    )
}