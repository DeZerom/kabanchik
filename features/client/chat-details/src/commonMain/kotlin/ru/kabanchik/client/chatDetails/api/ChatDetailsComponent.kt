package ru.kabanchik.client.chatDetails.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.client.chatDetails.internal.DefaultChatDetailsComponent

interface ChatDetailsComponent {
    val state: StateFlow<ChatDetailsContract.State>

    fun loginSelected(login: String)
    fun messageTextChanged(newText: String)
    fun messageSent()

    companion object {
        fun create(componentContext: ComponentContext): ChatDetailsComponent {
            return DefaultChatDetailsComponent(componentContext)
        }
    }
}