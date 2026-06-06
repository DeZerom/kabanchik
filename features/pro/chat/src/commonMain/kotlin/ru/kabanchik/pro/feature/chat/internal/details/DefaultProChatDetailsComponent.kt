package ru.kabanchik.pro.feature.chat.internal.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.common.snackBar.api.SnackBarData

class DefaultProChatDetailsComponent(
    componentContext: ComponentContext,
    dependencies: ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsDependencies,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val sessionId: String
) : ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        _root_ide_package_.ru.kabanchik.pro.feature.chat.internal.details.ProChatDetailsStore(
            chatDetailsInteractor = dependencies.chatDetailsInteractor,
            userInteractor = dependencies.userInteractor,
            errorHandler = dependencies.errorHandler,
            sessionId = sessionId
        )
    }
    override val state: StateFlow<ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.State> = store.state

    private val coroutineScope = coroutineScope()

    init {
        observeSideEffects()
    }

    override fun onMessageChanged(newMessage: String) {
        store.handleEvent(_root_ide_package_.ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.Event.MessageTextChanged(newMessage))
    }

    override fun onSendClicked() {
        store.handleEvent(_root_ide_package_.ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.Event.MessageSent)
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract.SideEffect.Error -> {
                    showSnackBar(SnackBarData.Error(effect.text))
                }
            }
        }.launchIn(coroutineScope)
    }
}
