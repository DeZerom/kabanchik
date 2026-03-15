package ru.kabanchik.common.domain.chat.logic.api.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.domain.chat.logic.api.CommonChatDetailsInteractor
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor
import ru.kabanchik.common.domain.chat.logic.internal.DefaultCommonChatDetailsInteractor
import ru.kabanchik.common.domain.chat.logic.internal.DefaultCommonChatsListInteractor

object DomainCommonChatModule {
    val module = module {
        factoryOf(::DefaultCommonChatsListInteractor) bind CommonChatsListInteractor::class
        factoryOf(::DefaultCommonChatDetailsInteractor) bind CommonChatDetailsInteractor::class
    }
}