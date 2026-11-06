package ru.kabanchik.pro.data.shift.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.pro.data.shift.logic.internal.DefaultProShiftRepository
import ru.kabanchik.pro.domain.shift.logic.api.repository.ProShiftRepository

object DataProShiftModule {
    val module = module {
        // single: репозиторий хранит общее для всего приложения состояние смены
        singleOf(::DefaultProShiftRepository) bind ProShiftRepository::class
    }
}
