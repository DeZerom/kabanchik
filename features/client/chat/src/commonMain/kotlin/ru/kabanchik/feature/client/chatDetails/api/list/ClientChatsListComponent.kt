package ru.kabanchik.feature.client.chatDetails.api.list

import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.common.feature.chat.model.CommonChatListTab

interface ClientChatsListComponent {
    val state: StateFlow<ClientChatsListContract.State>
    fun onCreateChatClicked()
    fun onChatClicked(id: String)
    fun onTabSelected(tab: CommonChatListTab)
}