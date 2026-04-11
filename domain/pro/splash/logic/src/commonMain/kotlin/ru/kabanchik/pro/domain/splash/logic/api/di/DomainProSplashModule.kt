package ru.kabanchik.pro.domain.splash.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.pro.domain.splash.logic.api.ProSplashInteractor
import ru.kabanchik.pro.domain.splash.logic.internal.DefaultProSplashInteractor

object DomainProSplashModule {
    val module = module {
        factoryOf(::DefaultProSplashInteractor) bind ProSplashInteractor::class
    }
}