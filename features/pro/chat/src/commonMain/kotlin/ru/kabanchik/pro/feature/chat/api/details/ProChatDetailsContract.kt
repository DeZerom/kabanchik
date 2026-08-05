package ru.kabanchik.pro.feature.chat.api.details

import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.tools.textResource.TextResource

class ProChatDetailsContract {
    data class State(
        val login: String = "",
        val currentMessage: String = "",
        val messages: List<CommonUiMessage> = emptyList(),
        val isLoading: Boolean = false
    ) {
        val toolbarTitle = messages
            .filterIsInstance<CommonUiMessage.Message>()
            .firstOrNull()
            ?.text
            .orEmpty()

        val toolbarSubtitle = messages
            .filterIsInstance<CommonUiMessage.Message>()
            .firstOrNull { !it.isUserAuthor }
            ?.authorLogin
            .orEmpty()
    }

    sealed interface Event {
        class MessageTextChanged(val newText: String) : Event
        object MessageSent : Event
    }

    sealed interface SideEffect {
        class Error(val text: TextResource) : SideEffect
    }
}
