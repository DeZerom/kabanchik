package ru.kabanchik.pro.feature.chatDetails.api.details

import kotlinx.coroutines.flow.StateFlow

interface ProChatDetailsComponent {
    val state: StateFlow<ProChatDetailsContract.State>

    fun onMessageChanged(newMessage: String)
    fun onSendClicked()
}