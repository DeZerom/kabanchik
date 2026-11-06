package ru.kabanchik.pro.feature.chat.api.list

import ru.kabanchik.common.feature.chat.model.CommonChatListTab
import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.tools.textResource.TextResource

class ProChatsListContract {
    data class State(
        val chats: List<CommonUiChatItem> = emptyList(),
        val isLoading: Boolean = false,
        val isWaitingForClient: Boolean = false,
        val selectedTab: CommonChatListTab = CommonChatListTab.Active,
        val closedChats: List<CommonUiChatItem> = emptyList(),
        val isClosedLoading: Boolean = false,
        val isClosedLoaded: Boolean = false,
    )

    sealed interface Event {
        object RequestChat : Event
        class TabSelected(val tab: CommonChatListTab) : Event
    }

    sealed interface SideEffect {
        class ShowError(val message: TextResource) : SideEffect
        class NavigateDetails(val sessionId: String) : SideEffect
    }
}
