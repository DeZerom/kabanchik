package ru.kabanchik.client.chatDetails.internal

import com.arkivanov.decompose.ComponentContext
import ru.kabanchik.client.chatDetails.api.ChatDetailsComponent

internal class DefaultChatDetailsComponent(
    componentContext: ComponentContext
) : ChatDetailsComponent, ComponentContext by componentContext