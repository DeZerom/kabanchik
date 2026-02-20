package ru.kabanchik.feature.client.chatDetails.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsComponent
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsContract
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsDependencies

internal class DefaultChatDetailsComponent(
    componentContext: ComponentContext,
    dependencies: ChatDetailsDependencies,
    private val showSnackBar: (SnackBarData) -> Unit,
) : ChatDetailsComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        ChatDetailsStore(
            chatDetailsInteractor = dependencies.chatDetailsInteractor,
            userInteractor = dependencies.userInteractor,
            errorHandler = dependencies.errorHandler
        )
    }
    override val state: StateFlow<ChatDetailsContract.State> = store.state

    private val coroutineScope = coroutineScope()

    init {
        observeSideEffects()
    }

    override fun messageTextChanged(newText: String) {
        store.handleEvent(ChatDetailsContract.Event.MessageTextChanged(newText))
    }

    override fun messageSent() {
        store.handleEvent(ChatDetailsContract.Event.MessageSent)
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is ChatDetailsContract.SideEffect.Error -> {
                    showSnackBar(SnackBarData.Error(effect.text))
                }
            }
        }.launchIn(coroutineScope)
    }
}