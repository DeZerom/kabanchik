package ru.kabanchik.client.feature.auth.internal.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.client.feature.auth.api.auth.AuthComponent
import ru.kabanchik.client.feature.auth.api.auth.AuthContract
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.common.snackBar.api.SnackBarData.Error

internal class DefaultAuthComponent(
    componentContext: ComponentContext,
    dependencies: AuthDependencies,
    private val navigateRegistration: () -> Unit,
    private val navigateHome: () -> Unit,
    private val showSnackBar: (SnackBarData) -> Unit
) : AuthComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        AuthStore(
            authInteractor = dependencies.authInteractor
        )
    }
    override val state: StateFlow<AuthContract.State> = store.state

    private val coroutineScope = coroutineScope()

    init {
        observeSideEffects()
    }

    override fun onLoginChanged(newLogin: String) {
        store.handleEvent(AuthContract.Event.LoginChanged(newLogin))
    }

    override fun onPasswordChanged(newPassword: String) {
        store.handleEvent(AuthContract.Event.PasswordChanged(newPassword))
    }

    override fun onAuthorizeClicked() {
        store.handleEvent(AuthContract.Event.AuthorizeClicked)
    }

    override fun onCreateAccountClicked() {
        navigateRegistration()
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is AuthContract.SideEffect.Error -> {
                    showSnackBar(Error(effect.text))
                }
                AuthContract.SideEffect.Success -> {
                    navigateHome()
                }
            }
        }.launchIn(coroutineScope)
    }
}