package ru.kabanchik.common.data.user.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.kabanchik.common.data.user.logic.internal.DefaultUserRepository
import ru.kabanchik.common.domain.auth.logic.api.repository.CommonAuthUserRepository
import ru.kabanchik.common.domain.user.logic.api.repository.UserRepository

object DataCommonUserModule {
    val module = module {
        singleOf(::DefaultUserRepository) binds arrayOf(
            UserRepository::class,
            CommonAuthUserRepository::class
        )
    }
}