package ru.kabanchik.client.feature.auth.internal.register

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.client.feature.auth.api.register.RegisterComponent
import ru.kabanchik.client.feature.auth.api.register.RegisterContract

internal class DefaultRegisterComponent(
    componentContext: ComponentContext,
    dependencies: RegisterDependencies,
    private val navigateBack: () -> Unit
) : RegisterComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        RegisterStore(
            authInteractor = dependencies.authInteractor
        )
    }
    override val state: StateFlow<RegisterContract.State> = store.state

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
}