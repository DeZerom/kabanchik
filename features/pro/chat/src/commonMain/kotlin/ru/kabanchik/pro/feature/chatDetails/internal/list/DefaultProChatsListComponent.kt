package ru.kabanchik.pro.feature.chatDetails.internal.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.pro.feature.chatDetails.api.list.ProChatsListComponent
import ru.kabanchik.pro.feature.chatDetails.api.list.ProChatsListContract

internal class DefaultProChatsListComponent(
    componentContext: ComponentContext
) : ProChatsListComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        ProChatsListStore()
    }
    override val state: StateFlow<ProChatsListContract.State> = store.state

    override fun onRequestClientClicked() {
        store.handleEvent(ProChatsListContract.Event.RequestChat)
    }
}