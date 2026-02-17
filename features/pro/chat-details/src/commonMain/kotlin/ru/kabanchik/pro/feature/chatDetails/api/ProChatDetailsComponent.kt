package ru.kabanchik.pro.feature.chatDetails.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.pro.feature.chatDetails.internal.DefaultProChatDetailsComponent

interface ProChatDetailsComponent {
    val state: StateFlow<ProChatDetailsContract.State>

    fun onMessageChanged(newMessage: String)
    fun onSendClicked()

    companion object {
        fun create(
            componentContext: ComponentContext,
            dependencies: ProChatDetailsDependencies
        ): ProChatDetailsComponent {
            return DefaultProChatDetailsComponent(
                componentContext = componentContext,
                dependencies = dependencies
            )
        }
    }
}