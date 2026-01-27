package ru.kabanchik.client.data.auth.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.client.data.auth.logic.internal.DefaultAuthRepository
import ru.kabanchik.client.domain.auth.logic.api.repository.AuthRepository

object DataClientAuthModule {
    val module = module {
        singleOf(::DefaultAuthRepository) bind AuthRepository::class
    }
}