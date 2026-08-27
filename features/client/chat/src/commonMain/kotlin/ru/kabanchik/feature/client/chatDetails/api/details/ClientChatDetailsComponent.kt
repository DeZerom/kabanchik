package ru.kabanchik.feature.client.chatDetails.api.details

import kotlinx.coroutines.flow.StateFlow

interface ClientChatDetailsComponent {
    val state: StateFlow<ChatDetailsContract.State>

    fun messageTextChanged(newText: String)
    fun filesSelectionRequested()
    fun fileRemoved(fileId: String)
    fun fileOpenRequested(fileId: String)
    fun imageOpenRequested(imageUrl: String)
    fun messageSent()
    fun onBackClicked()
}
