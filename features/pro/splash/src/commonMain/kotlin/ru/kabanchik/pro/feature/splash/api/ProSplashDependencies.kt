package ru.kabanchik.pro.feature.splash.api

import ru.kabanchik.pro.domain.splash.logic.api.ProSplashInteractor

interface ProSplashDependencies {
    val splashInteractor: ProSplashInteractor

    class Factory(
        override val splashInteractor: ProSplashInteractor
    ) : ProSplashDependencies
}