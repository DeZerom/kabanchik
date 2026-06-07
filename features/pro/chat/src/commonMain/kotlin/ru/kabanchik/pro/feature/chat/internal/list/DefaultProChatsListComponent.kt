package ru.kabanchik.pro.feature.chat.internal.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.common.snackBar.api.SnackBarData.Error
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListComponent
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListContract
import ru.kabanchik.pro.feature.chat.api.list.ProChatsListDependencies

internal class DefaultProChatsListComponent(
    componentContext: ComponentContext,
    dependencies: ProChatsListDependencies,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val navigateDetails: (String, Boolean) -> Unit,
) : ProChatsListComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        ProChatsListStore(
            proChatsListInteractor = dependencies.chatsListInteractor,
            errorHandler = dependencies.errorHandler
        )
    }
    override val state: StateFlow<ProChatsListContract.State> = store.state

    private val coroutineScope = coroutineScope()

    init {
        observeSideEffects()
    }

    override fun onRequestClientClicked() {
        store.handleEvent(ProChatsListContract.Event.RequestChat)
    }

    override fun onChatClicked(id: String) {
        navigateDetails(id, true)
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is ProChatsListContract.SideEffect.ShowError -> {
                    showSnackBar(Error(effect.message))
                }
                ProChatsListContract.SideEffect.NavigateDetails -> {
                    navigateDetails("", false)
                }
            }
        }.launchIn(coroutineScope)
    }
}
