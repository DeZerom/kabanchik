package ru.kabanchik.client.domain.logic.chatDetails.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chatDetails.api.ClientChatsListInteractor
import ru.kabanchik.client.domain.logic.chatDetails.internal.DefaultChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chatDetails.internal.DefaultClientChatsListInteractor

object DomainClientChatDetailsModule {
    val module = module {
        factoryOf(::DefaultChatDetailsInteractor) bind ChatDetailsInteractor::class
        factoryOf(::DefaultClientChatsListInteractor) bind ClientChatsListInteractor::class
    }
}