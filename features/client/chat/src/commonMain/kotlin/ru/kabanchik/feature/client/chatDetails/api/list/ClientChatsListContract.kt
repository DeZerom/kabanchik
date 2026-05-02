package ru.kabanchik.feature.client.chatDetails.api.list

import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.tools.textResource.TextResource

class ClientChatsListContract {
    data class State(
        val chats: List<CommonUiChatItem> = emptyList(),
        val isLoading: Boolean = false,
        val isChatCreating: Boolean = false
    )

    sealed interface Event {
        object CreateChatClicked : Event
    }

    sealed interface SideEffect {
        class ShowError(val message: TextResource) : SideEffect
        object NavigateChatDetails : SideEffect
    }
}