package ru.kabanchik.client.feature.splash.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.client.feature.splash.api.ClientSplashComponent
import ru.kabanchik.client.feature.splash.api.ClientSplashContract
import ru.kabanchik.client.feature.splash.api.ClientSplashDependencies

internal class DefaultClientSplashComponent(
    componentContext: ComponentContext,
    dependencies: ClientSplashDependencies,
    private val navigateChatsList: () -> Unit,
    private val navigateAuth: () -> Unit
) : ClientSplashComponent, ComponentContext by componentContext {
    private val coroutineScope = coroutineScope()

    private val store = retainedInstance {
        ClientSplashStore(
            splashInteractor = dependencies.splashInteractor
        )
    }

    init {
        observeSideEffect()
    }

    private fun observeSideEffect() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                ClientSplashContract.SideEffect.Authorized -> {
                    navigateChatsList()
                }
                ClientSplashContract.SideEffect.NotAuthorized -> {
                    navigateAuth()
                }
            }
        }.launchIn(coroutineScope)
    }
}