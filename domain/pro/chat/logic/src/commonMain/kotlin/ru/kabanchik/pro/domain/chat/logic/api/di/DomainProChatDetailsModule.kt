package ru.kabanchik.pro.domain.chat.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor
import ru.kabanchik.pro.domain.chat.logic.internal.DefaultProChatDetailsInteractor
import ru.kabanchik.pro.domain.chat.logic.internal.DefaultProChatsListInteractor

object DomainProChatDetailsModule {
    val module = module {
        factoryOf(::DefaultProChatsListInteractor) bind ProChatsListInteractor::class
        factoryOf(::DefaultProChatDetailsInteractor) bind ProChatDetailsInteractor::class
    }
}