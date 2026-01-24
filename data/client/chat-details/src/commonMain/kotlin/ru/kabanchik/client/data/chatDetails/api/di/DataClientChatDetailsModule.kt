package ru.kabanchik.client.data.chatDetails.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.client.data.chatDetails.internal.DefaultChatDetailsRepository
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsRepository

object DataClientChatDetailsModule {
    val module = module {
        singleOf(::DefaultChatDetailsRepository) bind ChatDetailsRepository::class
    }
}