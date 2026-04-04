package ru.kabanchik.client.domain.logic.chat.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.client.domain.logic.chat.api.ClientChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chat.api.ClientChatsListInteractor
import ru.kabanchik.client.domain.logic.chat.internal.DefaultClientChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chat.internal.DefaultClientChatsListInteractor

object DomainClientChatDetailsModule {
    val module = module {
        factoryOf(::DefaultClientChatDetailsInteractor) bind ClientChatDetailsInteractor::class
        factoryOf(::DefaultClientChatsListInteractor) bind ClientChatsListInteractor::class
    }
}