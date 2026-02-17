package ru.kabanchik.common.data.user.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.data.user.logic.internal.DefaultUserRepository
import ru.kabanchik.common.domain.user.logic.api.repository.UserRepository

object DataCommonUserModule {
    val module = module {
        factoryOf(::DefaultUserRepository) bind UserRepository::class
    }
}