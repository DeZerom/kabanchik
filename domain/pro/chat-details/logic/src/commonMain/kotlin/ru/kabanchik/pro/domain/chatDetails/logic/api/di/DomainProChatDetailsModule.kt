package ru.kabanchik.pro.domain.chatDetails.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.pro.domain.chatDetails.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chatDetails.logic.internal.ProDefaultChatDetailsInteractor

object DomainProChatDetailsModule {
    val module = module {
        factoryOf(::ProDefaultChatDetailsInteractor) bind ProChatDetailsInteractor::class
    }
}