package ru.kabanchik.client.data.token.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.kabanchik.client.data.token.logic.internal.DefaultTokenRepository
import ru.kabanchik.client.domain.token.logic.api.TokenRepository
import ru.kabanchik.common.domain.auth.logic.api.repository.CommonAuthTokenRepository
import ru.kabanchik.common.domain.splash.logic.api.repository.CommonSplashTokenRepository

object DataCommonTokenModule {
    val module = module {
        singleOf(::DefaultTokenRepository) binds arrayOf(
            TokenRepository::class,
            CommonSplashTokenRepository::class,
            CommonAuthTokenRepository::class,
        )
    }
}
