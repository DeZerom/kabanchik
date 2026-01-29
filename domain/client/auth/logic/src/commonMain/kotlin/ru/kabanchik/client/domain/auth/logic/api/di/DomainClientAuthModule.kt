package ru.kabanchik.client.domain.auth.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.domain.auth.logic.internal.DefaultAuthInteractor

object DomainClientAuthModule {
    val module = module {
        factoryOf(::DefaultAuthInteractor) bind AuthInteractor::class
    }
}