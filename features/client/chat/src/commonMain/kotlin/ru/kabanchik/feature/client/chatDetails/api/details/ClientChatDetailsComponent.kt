package ru.kabanchik.feature.client.chatDetails.api.details

import kotlinx.coroutines.flow.StateFlow

interface ClientChatDetailsComponent {
    val state: StateFlow<ChatDetailsContract.State>

    fun messageTextChanged(newText: String)
    fun messageSent()
}