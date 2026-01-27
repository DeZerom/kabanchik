package ru.kabanchik.client.domain.logic.chatDetails.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chatDetails.internal.DefaultChatDetailsInteractor

object DomainClientChatDetailsModule {
    val module = module {
        factoryOf(::DefaultChatDetailsInteractor) bind ChatDetailsInteractor::class
    }
}