package ru.kabanchik.common.domain.profile.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.common.domain.profile.logic.internal.DefaultCommonProfileInteractor

object DomainCommonProfileModule {
    val module = module {
        singleOf(::DefaultCommonProfileInteractor) bind CommonProfileInteractor::class
    }
}
