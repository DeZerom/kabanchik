package ru.kabanchik.feature.client.chatDetails.internal.list

import com.arkivanov.decompose.ComponentContext
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListComponent

class DefaultClientChatsListComponent(
    componentContext: ComponentContext,
    private val navigateChatDetails: () -> Unit
) : ClientChatsListComponent, ComponentContext by componentContext {
    override fun onCreateChatClicked() {
        navigateChatDetails()
    }
}