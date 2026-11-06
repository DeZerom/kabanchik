package ru.kabanchik.pro.feature.chat.api.list

import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.common.feature.chat.model.CommonChatListTab

interface ProChatsListComponent {
    val state: StateFlow<ProChatsListContract.State>

    fun onRequestClientClicked()
    fun onChatClicked(id: String)
    fun onTabSelected(tab: CommonChatListTab)
}