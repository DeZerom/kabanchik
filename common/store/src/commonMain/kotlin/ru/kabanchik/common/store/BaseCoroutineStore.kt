package ru.kabanchik.common.store

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseCoroutineStore<Event, State, SideEffect>(): InstanceKeeper.Instance  {
    private val _state = MutableStateFlow(initState())
    val state = _state.asStateFlow()

    val currentState: State = state.value

    private val _sideEffect = Channel<SideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    protected val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    abstract fun handleEvent(event: Event)
    protected abstract fun initState(): State

    protected fun reduceState(reducer: State.() -> State) {
        _state.value = reducer(state.value)
    }

    protected fun pushSideEffect(sideEffect: SideEffect) {
        coroutineScope.launch {
            _sideEffect.send(sideEffect)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}