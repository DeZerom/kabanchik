package ru.kabanchik.pro.feature.chatDetails.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsComponent
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsContract
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsDependencies

class DefaultProChatDetailsComponent(
    componentContext: ComponentContext,
    dependencies: ProChatDetailsDependencies
) : ProChatDetailsComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        ProChatDetailsStore(
            chatDetailsInteractor = dependencies.chatDetailsInteractor,
            userInteractor = dependencies.userInteractor
        )
    }
    override val state: StateFlow<ProChatDetailsContract.State> = store.state

    override fun onMessageChanged(newMessage: String) {
        store.handleEvent(ProChatDetailsContract.Event.MessageTextChanged(newMessage))
    }

    override fun onSendClicked() {
        store.handleEvent(ProChatDetailsContract.Event.MessageSent)
    }
}