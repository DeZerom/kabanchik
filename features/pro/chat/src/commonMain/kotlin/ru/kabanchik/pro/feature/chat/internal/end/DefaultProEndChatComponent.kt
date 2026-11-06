package ru.kabanchik.pro.feature.chat.internal.end

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.common.chat.model.CommonChatResolution
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsDependencies
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatComponent
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatContract.Event
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatContract.SideEffect
import ru.kabanchik.pro.feature.chat.api.end.ProEndChatContract.State

internal class DefaultProEndChatComponent(
    componentContext: ComponentContext,
    dependencies: ProChatDetailsDependencies,
    sessionId: String,
    private val onFinished: () -> Unit,
    private val onError: (TextResource) -> Unit,
    private val onDismissed: () -> Unit,
) : ProEndChatComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        ProEndChatStore(
            chatDetailsInteractor = dependencies.chatDetailsInteractor,
            errorHandler = dependencies.errorHandler,
            sessionId = sessionId,
        )
    }
    override val state: StateFlow<State> = store.state

    private val coroutineScope = coroutineScope()

    init {
        observeSideEffects()
    }

    override fun onResolutionSelected(resolution: CommonChatResolution) {
        store.handleEvent(Event.ResolutionSelected(resolution))
    }

    override fun onEndClicked() {
        store.handleEvent(Event.EndClicked)
    }

    // Пока запрос не завершился, шит не закрываем: иначе результат завершения потеряется
    override fun onDismissRequested() {
        if (!state.value.isEnding) {
            onDismissed()
        }
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                SideEffect.Finished -> onFinished()
                is SideEffect.Error -> onError(effect.text)
            }
        }.launchIn(coroutineScope)
    }
}
