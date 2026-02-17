package ru.kabanchik.pro.domain.chatDetails.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.pro.domain.chatDetails.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chatDetails.logic.internal.DefaultProChatDetailsInteractor

object DomainProChatDetailsModule {
    val module = module {
        factoryOf(::DefaultProChatDetailsInteractor) bind ProChatDetailsInteractor::class
    }
}