package ru.kabanchik.pro.feature.auth.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.pro.feature.auth.api.ProAuthComponent
import ru.kabanchik.pro.feature.auth.api.ProAuthContract
import ru.kabanchik.pro.feature.auth.api.ProAuthDependencies

internal class DefaultProAuthComponent(
    componentContext: ComponentContext,
    dependencies: ProAuthDependencies,
    private val onAuthorize: () -> Unit,
    private val onError: (TextResource) -> Unit
) : ProAuthComponent, ComponentContext by componentContext {
    private val store = retainedInstance {
        ProAuthStore(
            authInteractor = dependencies.authInteractor,
            userInteractor = dependencies.userInteractor
        )
    }

    override val uiState: StateFlow<ProAuthContract.State> = store.state

    private val coroutineScope = coroutineScope()

    init {
        observeSideEffects()
    }

    override fun onAuthorizeClicked() {
        store.handleEvent(ProAuthContract.Event.AuthorizeClicked)
    }

    override fun onLoginChanged(newLogin: String) {
        store.handleEvent(ProAuthContract.Event.LoginChanged(newLogin))
    }

    override fun onPasswordChanged(newPassword: String) {
        store.handleEvent(ProAuthContract.Event.PasswordChanged(newPassword))
    }

    private fun observeSideEffects() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                ProAuthContract.SideEffect.Authorized -> onAuthorize()
                is ProAuthContract.SideEffect.Error -> onError(effect.text)
            }
        }.launchIn(coroutineScope)
    }
}