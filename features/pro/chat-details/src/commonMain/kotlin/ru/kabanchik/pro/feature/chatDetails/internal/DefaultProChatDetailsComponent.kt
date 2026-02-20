package ru.kabanchik.pro.feature.chatDetails.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsComponent
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsContract
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsDependencies

class DefaultProChatDetailsComponent(
    componentContext: ComponentContext,
    dependencies: ProChatDetailsDependencies,
    private val showSnackBar: (SnackBarData) -> Unit
) : ProChatDetailsComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        ProChatDetailsStore(
            chatDetailsInteractor = dependencies.chatDetailsInteractor,
            userInteractor = dependencies.userInteractor,
            errorHandler = dependencies.errorHandler
        )
    }
    override val state: StateFlow<ProChatDetailsContract.State> = store.state

    private val coroutineScope = coroutineScope()

    init {
        observeSideEffects()
    }

    override fun onMessageChanged(newMessage: String) {
        store.handleEvent(ProChatDetailsContract.Event.MessageTextChanged(newMessage))
    }

    override fun onSendClicked() {
        store.handleEvent(ProChatDetailsContract.Event.MessageSent)
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is ProChatDetailsContract.SideEffect.Error -> {
                    showSnackBar(SnackBarData.Error(effect.text))
                }
            }
        }.launchIn(coroutineScope)
    }
}