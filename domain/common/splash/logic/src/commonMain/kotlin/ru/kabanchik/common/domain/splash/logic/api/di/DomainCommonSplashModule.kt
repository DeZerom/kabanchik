package ru.kabanchik.common.domain.splash.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.domain.splash.logic.api.CommonSplashInteractor
import ru.kabanchik.common.domain.splash.logic.internal.DefaultCommonSplashInteractor

object DomainCommonSplashModule {
    val module = module {
        factoryOf(::DefaultCommonSplashInteractor) bind CommonSplashInteractor::class
    }
}