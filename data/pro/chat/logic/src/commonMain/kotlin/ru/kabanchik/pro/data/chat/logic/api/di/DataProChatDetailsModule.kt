package ru.kabanchik.pro.data.chat.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.pro.data.chat.logic.internal.DefaultProChatDetailsRepository
import ru.kabanchik.pro.domain.chatDetails.logic.api.repository.ProChatDetailsRepository

object DataProChatDetailsModule {
    val module = module {
        singleOf(::DefaultProChatDetailsRepository) bind ProChatDetailsRepository::class
    }
}