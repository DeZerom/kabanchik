package ru.kabanchik.client.chatDetails.api

import com.arkivanov.decompose.ComponentContext
import ru.kabanchik.client.chatDetails.internal.DefaultChatDetailsComponent

interface ChatDetailsComponent {
    companion object {
        fun create(componentContext: ComponentContext): ChatDetailsComponent {
            return DefaultChatDetailsComponent(componentContext)
        }
    }
}