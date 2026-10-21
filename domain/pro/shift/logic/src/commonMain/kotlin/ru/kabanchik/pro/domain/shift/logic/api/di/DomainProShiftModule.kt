package ru.kabanchik.pro.domain.shift.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import ru.kabanchik.pro.domain.shift.logic.internal.DefaultProShiftInteractor

object DomainProShiftModule {
    val module = module {
        factoryOf(::DefaultProShiftInteractor) bind ProShiftInteractor::class
    }
}
