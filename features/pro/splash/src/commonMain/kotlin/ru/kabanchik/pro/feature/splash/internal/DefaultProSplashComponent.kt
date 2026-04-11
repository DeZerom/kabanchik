package ru.kabanchik.pro.feature.splash.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kabanchik.pro.feature.splash.api.ProSplashComponent
import ru.kabanchik.pro.feature.splash.api.ProSplashContract
import ru.kabanchik.pro.feature.splash.api.ProSplashDependencies

internal class DefaultProSplashComponent(
    componentContext: ComponentContext,
    dependencies: ProSplashDependencies,
    private val navigateChatsList: () -> Unit,
    private val navigateAuth: () -> Unit
) : ProSplashComponent, ComponentContext by componentContext {
    private val coroutineScope = coroutineScope()

    private val store = retainedInstance {
        ProSplashStore(
            splashInteractor = dependencies.splashInteractor
        )
    }

    init {
        observeSideEffect()
    }

    private fun observeSideEffect() {
        store.sideEffect.onEach { effect ->
            when (effect) {
                ProSplashContract.SideEffect.Authorized -> {
                    navigateChatsList()
                }
                ProSplashContract.SideEffect.NotAuthorized -> {
                    navigateAuth()
                }
            }
        }.launchIn(coroutineScope)
    }
}