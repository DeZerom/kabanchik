package ru.kabanchik.pro.feature.profile.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.pro.feature.profile.api.ProProfileComponent
import ru.kabanchik.pro.feature.profile.api.ProProfileContract
import ru.kabanchik.pro.feature.profile.api.ProProfileDependencies

internal class DefaultProProfileComponent(
    componentContext: ComponentContext,
    dependencies: ProProfileDependencies,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val navigateAuth: () -> Unit
) : ProProfileComponent, ComponentContext by componentContext {
    private val coroutineScope = coroutineScope()

    private val store = retainedInstance {
        ProProfileStore(
            profileInteractor = dependencies.profileInteractor,
            shiftInteractor = dependencies.shiftInteractor,
            errorHandler = dependencies.errorHandler
        )
    }
    override val state: StateFlow<ProProfileContract.State> = store.state

    init {
        observeSideEffects()
    }

    override fun onEndShiftClicked() {
        store.handleEvent(ProProfileContract.Event.EndShiftClicked)
    }

    override fun onEndShiftConfirmed() {
        store.handleEvent(ProProfileContract.Event.EndShiftConfirmed)
    }

    override fun onEndShiftDismissed() {
        store.handleEvent(ProProfileContract.Event.EndShiftDismissed)
    }

    override fun onLogoutClicked() {
        store.handleEvent(ProProfileContract.Event.LogoutClicked)
    }

    override fun onLogoutConfirmed() {
        store.handleEvent(ProProfileContract.Event.LogoutConfirmed)
    }

    override fun onLogoutDismissed() {
        store.handleEvent(ProProfileContract.Event.LogoutDismissed)
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is ProProfileContract.SideEffect.ShowError -> {
                    showSnackBar(SnackBarData.Error(effect.message))
                }

                ProProfileContract.SideEffect.NavigateAuth -> {
                    navigateAuth()
                }
            }
        }.launchIn(coroutineScope)
    }
}
