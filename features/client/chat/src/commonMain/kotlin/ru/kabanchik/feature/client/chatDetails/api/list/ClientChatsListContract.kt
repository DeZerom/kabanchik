package ru.kabanchik.feature.client.chatDetails.api.list

import ru.kabanchik.common.feature.chat.model.CommonChatListTab
import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.tools.textResource.TextResource

class ClientChatsListContract {
    data class State(
        val chats: List<CommonUiChatItem> = emptyList(),
        val isLoading: Boolean = false,
        val isChatCreating: Boolean = false,
        val selectedTab: CommonChatListTab = CommonChatListTab.Active,
        val closedChats: List<CommonUiChatItem> = emptyList(),
        val isClosedLoading: Boolean = false,
        val isClosedLoaded: Boolean = false,
    )

    sealed interface Event {
        object CreateChatClicked : Event
        class TabSelected(val tab: CommonChatListTab) : Event
    }

    sealed interface SideEffect {
        class ShowError(val message: TextResource) : SideEffect
        class NavigateChatDetails(val sessionId: String) : SideEffect
    }
}
