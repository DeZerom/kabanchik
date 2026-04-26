package ru.kabanchik.client.domain.splash.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.client.domain.splash.logic.api.ClientSplashInteractor
import ru.kabanchik.client.domain.splash.logic.internal.DefaultClientSplashInteractor

object DomainClientSplashModule {
    val module = module {
        factoryOf(::DefaultClientSplashInteractor) bind ClientSplashInteractor::class
    }
}