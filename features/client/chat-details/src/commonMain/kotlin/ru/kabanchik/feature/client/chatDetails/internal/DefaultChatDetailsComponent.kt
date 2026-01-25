package ru.kabanchik.feature.client.chatDetails.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsComponent
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsContract
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsDependencies

internal class DefaultChatDetailsComponent(
    componentContext: ComponentContext,
    dependencies: ChatDetailsDependencies
) : ChatDetailsComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        ChatDetailsStore(
            chatDetailsInteractor = dependencies.chatDetailsInteractor
        )
    }
    override val state: StateFlow<ChatDetailsContract.State> = store.state

    override fun loginSelected(login: String) {
        store.handleEvent(ChatDetailsContract.Event.UserSelected(login))
    }

    override fun messageTextChanged(newText: String) {
        store.handleEvent(ChatDetailsContract.Event.MessageTextChanged(newText))
    }

    override fun messageSent() {
        store.handleEvent(ChatDetailsContract.Event.MessageSent)
    }
}