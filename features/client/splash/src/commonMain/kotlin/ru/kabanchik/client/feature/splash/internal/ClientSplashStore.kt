package ru.kabanchik.client.feature.splash.internal

import kotlinx.coroutines.launch
import ru.kabanchik.client.domain.splash.logic.api.ClientSplashInteractor
import ru.kabanchik.client.feature.splash.api.ClientSplashContract.Event
import ru.kabanchik.client.feature.splash.api.ClientSplashContract.SideEffect
import ru.kabanchik.client.feature.splash.api.ClientSplashContract.State
import ru.kabanchik.common.store.BaseCoroutineStore

internal class ClientSplashStore(
    private val splashInteractor: ClientSplashInteractor
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