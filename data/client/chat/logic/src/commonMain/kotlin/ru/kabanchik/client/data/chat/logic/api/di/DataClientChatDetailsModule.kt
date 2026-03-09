package ru.kabanchik.client.data.chat.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.client.data.chat.logic.internal.DefaultClientChatDetailsRepository
import ru.kabanchik.client.data.chat.logic.internal.DefaultClientChatsListRepository
import ru.kabanchik.client.domain.logic.chat.api.repository.ClientChatDetailsRepository
import ru.kabanchik.client.domain.logic.chat.api.repository.ClientChatsListRepository

object DataClientChatDetailsModule {
    val module = module {
        singleOf(::DefaultClientChatsListRepository) bind ClientChatsListRepository::class
        singleOf(::DefaultClientChatDetailsRepository) bind ClientChatDetailsRepository::class
    }
}