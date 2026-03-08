package ru.kabanchik.feature.client.chatDetails.api.list

import kotlinx.coroutines.flow.StateFlow

interface ClientChatsListComponent {
    val state: StateFlow<ClientChatsListContract.State>
    fun onCreateChatClicked()
}