package ru.kabanchik.client.feature.profile.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.client.feature.profile.api.ClientProfileComponent
import ru.kabanchik.client.feature.profile.api.ClientProfileContract
import ru.kabanchik.client.feature.profile.api.ClientProfileDependencies
import ru.kabanchik.common.snackBar.api.SnackBarData

internal class DefaultClientProfileComponent(
    componentContext: ComponentContext,
    dependencies: ClientProfileDependencies,
    private val showSnackBar: (SnackBarData) -> Unit,
    private val navigateAuth: () -> Unit
) : ClientProfileComponent, ComponentContext by componentContext {
    private val coroutineScope = coroutineScope()

    private val store = retainedInstance {
        ClientProfileStore(
            profileInteractor = dependencies.profileInteractor,
            errorHandler = dependencies.errorHandler
        )
    }
    override val state: StateFlow<ClientProfileContract.State> = store.state

    init {
        observeSideEffects()
    }

    override fun onLogoutClicked() {
        store.handleEvent(ClientProfileContract.Event.LogoutClicked)
    }

    override fun onLogoutConfirmed() {
        store.handleEvent(ClientProfileContract.Event.LogoutConfirmed)
    }

    override fun onLogoutDismissed() {
        store.handleEvent(ClientProfileContract.Event.LogoutDismissed)
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is ClientProfileContract.SideEffect.ShowError -> {
                    showSnackBar(SnackBarData.Error(effect.message))
                }

                ClientProfileContract.SideEffect.NavigateAuth -> {
                    navigateAuth()
                }
            }
        }.launchIn(coroutineScope)
    }
}
