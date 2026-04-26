package ru.kabanchik.client.feature.splash.api

import ru.kabanchik.client.domain.splash.logic.api.ClientSplashInteractor

interface ClientSplashDependencies {
    val splashInteractor: ClientSplashInteractor

    class Factory(
        override val splashInteractor: ClientSplashInteractor
    ) : ClientSplashDependencies
}