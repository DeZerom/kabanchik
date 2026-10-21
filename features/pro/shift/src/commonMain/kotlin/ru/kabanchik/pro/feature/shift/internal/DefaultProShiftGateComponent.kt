package ru.kabanchik.pro.feature.shift.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.shift.api.ProShiftGateComponent
import ru.kabanchik.pro.feature.shift.api.ProShiftGateContract
import ru.kabanchik.pro.feature.shift.api.ProShiftGateDependencies

internal class DefaultProShiftGateComponent(
    componentContext: ComponentContext,
    dependencies: ProShiftGateDependencies,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val navigateHome: () -> Unit,
    private val navigateAuth: () -> Unit
) : ProShiftGateComponent, ComponentContext by componentContext {
    private val coroutineScope = coroutineScope()

    private val store = retainedInstance {
        ProShiftGateStore(
            shiftInteractor = dependencies.shiftInteractor,
            errorHandler = dependencies.errorHandler
        )
    }
    override val state: StateFlow<ProShiftGateContract.State> = store.state

    init {
        observeSideEffects()
        // Смену могли начать на другом устройстве: перепроверяем при возврате на экран
        lifecycle.doOnStart { store.handleEvent(ProShiftGateContract.Event.ScreenStarted) }
    }

    override fun onStartShiftClicked() {
        store.handleEvent(ProShiftGateContract.Event.StartShiftClicked)
    }

    override fun onRetryClicked() {
        store.handleEvent(ProShiftGateContract.Event.RetryClicked)
    }

    override fun onLogoutClicked() {
        store.handleEvent(ProShiftGateContract.Event.LogoutClicked)
    }

    override fun onLogoutConfirmed() {
        store.handleEvent(ProShiftGateContract.Event.LogoutConfirmed)
    }

    override fun onLogoutDismissed() {
        store.handleEvent(ProShiftGateContract.Event.LogoutDismissed)
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is ProShiftGateContract.SideEffect.ShowError -> {
                    showSnackBar(SnackBarData.Error(effect.message))
                }

                ProShiftGateContract.SideEffect.NavigateHome -> {
                    navigateHome()
                }

                ProShiftGateContract.SideEffect.NavigateAuth -> {
                    navigateAuth()
                }
            }
        }.launchIn(coroutineScope)
    }
}
