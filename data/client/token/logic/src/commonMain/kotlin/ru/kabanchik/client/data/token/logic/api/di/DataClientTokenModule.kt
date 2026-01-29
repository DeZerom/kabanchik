package ru.kabanchik.client.data.token.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.kabanchik.client.data.token.logic.internal.DefaultTokenRepository
import ru.kabanchik.client.domain.auth.logic.api.repository.AuthTokenRepository
import ru.kabanchik.client.domain.token.logic.api.TokenRepository

object DataClientTokenModule {
    val module = module {
        singleOf(::DefaultTokenRepository) binds arrayOf(
            TokenRepository::class,
            AuthTokenRepository::class
        )
    }
}