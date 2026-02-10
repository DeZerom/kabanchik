package ru.kabanchik.pro.domain.auth.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.pro.domain.auth.logic.api.ProAuthInteractor
import ru.kabanchik.pro.domain.auth.logic.internal.DefaultProAuthInteractor

object DomainProAuthDiModule {
    val module = module {
        factoryOf(::DefaultProAuthInteractor) bind ProAuthInteractor::class
    }
}