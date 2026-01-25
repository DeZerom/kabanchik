package ru.kabanchik.feature.client.chatDetails.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.feature.client.chatDetails.internal.DefaultChatDetailsComponent

interface ChatDetailsComponent {
    val state: StateFlow<ChatDetailsContract.State>

    fun loginSelected(login: String)
    fun messageTextChanged(newText: String)
    fun messageSent()

    companion object {
        fun create(
            componentContext: ComponentContext,
            dependencies: ChatDetailsDependencies
        ): ChatDetailsComponent {
            return DefaultChatDetailsComponent(componentContext, dependencies)
        }
    }
}