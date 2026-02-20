package ru.kabanchik.client.feature.auth.internal.register

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kabanchik.features.client.auth.generated.resources.Res
import kabanchik.features.client.auth.generated.resources.auth_reg_successful_registration
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.client.feature.auth.api.register.RegisterComponent
import ru.kabanchik.client.feature.auth.api.register.RegisterContract
import ru.kabanchik.common.snackBar.api.SnackBarData
import ru.kabanchik.common.tools.textResource.TextResource

internal class DefaultRegisterComponent(
    componentContext: ComponentContext,
    private val dependencies: RegisterDependencies,
    private val navigateBack: () -> Unit,
    private val showSnackBar: (SnackBarData) -> Unit
) : RegisterComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        RegisterStore(
            authInteractor = dependencies.authInteractor,
            errorHandler = dependencies.errorHandler
        )
    }
    override val state: StateFlow<RegisterContract.State> = store.state

    private val coroutineScope = coroutineScope()

    init {
        observeSideEffects()
    }

    override fun onLoginChanged(newLogin: String) {
        store.handleEvent(RegisterContract.Event.LoginChanged(newLogin))
    }

    override fun onPasswordChanged(newPassword: String) {
        store.handleEvent(RegisterContract.Event.PasswordChanged(newPassword))
    }

    override fun onCreateAccountClicked() {
        store.handleEvent(RegisterContract.Event.CreateAccountClicked)
    }

    override fun onHaveAccountClicked() {
        navigateBack()
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                is RegisterContract.SideEffect.Error -> {
                    showSnackBar(SnackBarData.Error(text = effect.text))
                }
                RegisterContract.SideEffect.Success -> {
                    showSnackBar(SnackBarData.Success(text = TextResource.Id(Res.string.auth_reg_successful_registration)))
                    navigateBack()
                }
            }
        }.launchIn(coroutineScope)
    }
}