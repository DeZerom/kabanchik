package ru.kabanchik.pro.data.auth.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.pro.data.auth.logic.internal.DefaultProAuthRepository
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthRepository

object DataProAuthModule {
    val module = module {
        singleOf(::DefaultProAuthRepository) bind ProAuthRepository::class
    }
}