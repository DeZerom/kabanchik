package ru.kabanchik.common.domain.auth.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.domain.auth.logic.api.CommonAuthInteractor
import ru.kabanchik.common.domain.auth.logic.internal.DefaultCommonAuthInteractor

object DomainCommonAuthModule {
    val module = module {
        factoryOf(::DefaultCommonAuthInteractor) bind CommonAuthInteractor::class
    }
}