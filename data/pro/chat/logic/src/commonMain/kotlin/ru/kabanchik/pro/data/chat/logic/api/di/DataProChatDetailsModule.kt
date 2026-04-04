package ru.kabanchik.pro.data.chat.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.pro.data.chat.logic.internal.DefaultProChatDetailsRepository
import ru.kabanchik.pro.data.chat.logic.internal.DefaultProChatsListRepository
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatDetailsRepository
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatsListRepository

object DataProChatDetailsModule {
    val module = module {
        singleOf(::DefaultProChatsListRepository) bind ProChatsListRepository::class
        singleOf(::DefaultProChatDetailsRepository) bind ProChatDetailsRepository::class
    }
}