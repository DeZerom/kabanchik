package ru.kabanchik.common.domain.user.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.domain.user.logic.internal.DefaultUserInteractor

object DomainCommonUserModule {
    val module = module {
        singleOf(::DefaultUserInteractor) bind UserInteractor::class
    }
}