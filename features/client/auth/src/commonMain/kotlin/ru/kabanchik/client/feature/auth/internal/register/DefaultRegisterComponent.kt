package ru.kabanchik.client.feature.auth.internal.register

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _messagesFlow = Channel<String>(Channel.BUFFERED)
    override val messagesFlow = _messagesFlow.receiveAsFlow()

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
                    _messagesFlow.send(effect.text)
                }
            }
        }.launchIn(coroutineScope)
    }
}