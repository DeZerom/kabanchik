package ru.kabanchik.common.data.chat.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.data.chat.logic.internal.DefaultCommonChatsListRepository
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository

object DataCommonChatModule {
    val module = module {
        singleOf(::DefaultCommonChatsListRepository) bind CommonChatsListRepository::class
    }
}