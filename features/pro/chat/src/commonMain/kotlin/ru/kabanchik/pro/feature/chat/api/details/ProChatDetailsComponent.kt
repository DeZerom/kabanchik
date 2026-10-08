package ru.kabanchik.pro.feature.chat.api.details

import kotlinx.coroutines.flow.StateFlow

interface ProChatDetailsComponent {
    val state: StateFlow<ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.State>

    fun onMessageChanged(newMessage: String)
    fun onFileSelectionRequested()
    fun onFileRemoved(fileId: String)
    fun onFileOpenRequested(fileId: String)
    fun onImageOpenRequested(imageUrl: String)
    fun onSendClicked()
    fun onBackClicked()
}
