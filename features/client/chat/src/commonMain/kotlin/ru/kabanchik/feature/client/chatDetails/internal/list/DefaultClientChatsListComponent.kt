package ru.kabanchik.feature.client.chatDetails.internal.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListComponent
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListContract
import ru.kabanchik.feature.client.chatDetails.api.list.ClientChatsListDependencies

class DefaultClientChatsListComponent(
    componentContext: ComponentContext,
    dependencies: ClientChatsListDependencies,
    private val navigateChatDetails: (String) -> Unit,
    private val showSnackBar: (SnackBarData) -> Unit,
) : ClientChatsListComponent, ComponentContext by componentContext {
    private val coroutineScope = coroutineScope()

    private val store = retainedInstance {
        ClientChatsListStore(
            listInteractor = dependencies.listInteractor,
            errorHandler = dependencies.errorHandler
        )
    }
    override val state: StateFlow<ClientChatsListContract.State> = store.state

    init {
        observeSideEffects()
    }

    override fun onCreateChatClicked() {
        store.handleEvent(ClientChatsListContract.Event.CreateChatClicked)
    }

    override fun onChatClicked(id: String) {
        navigateChatDetails(id)
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is ClientChatsListContract.SideEffect.ShowError -> {
                    showSnackBar(SnackBarData.Error(effect.message))
                }

                ClientChatsListContract.SideEffect.NavigateChatDetails -> {
                    navigateChatDetails("")
                }
            }
        }.launchIn(coroutineScope)
    }
}
