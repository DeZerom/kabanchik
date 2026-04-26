package ru.kabanchik.pro.feature.splash.internal

import kotlinx.coroutines.launch
import ru.kabanchik.common.store.BaseCoroutineStore
import ru.kabanchik.pro.domain.splash.logic.api.ProSplashInteractor
import ru.kabanchik.pro.feature.splash.api.ProSplashContract.Event
import ru.kabanchik.pro.feature.splash.api.ProSplashContract.SideEffect
import ru.kabanchik.pro.feature.splash.api.ProSplashContract.State

internal class ProSplashStore(
    private val splashInteractor: ProSplashInteractor
) : BaseCoroutineStore<Event, State, SideEffect>() {
    init {
        initApp()
    }

    override fun initState(): State {
        return State()
    }

    override fun handleEvent(event: Event) = Unit

    private fun initApp() {
        coroutineScope.launch {
            val isAuthorized = splashInteractor.isAuthorized()

            pushSideEffect(
                sideEffect = if (isAuthorized) {
                    SideEffect.Authorized
                } else {
                    SideEffect.NotAuthorized
                }
            )
        }
    }
}