package ru.kabanchik.pro.domain.chat.logic.internal

import kotlinx.coroutines.flow.first
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatsListRepository

internal class DefaultProChatsListInteractor(
    commonInteractor: CommonChatsListInteractor,
    private val listRepository: ProChatsListRepository
) : ProChatsListInteractor, CommonChatsListInteractor by commonInteractor {
    override suspend fun requestChat() {
        listRepository.register()

        val incoming = listRepository.listenIncoming().first()
        listRepository.acceptChat(clientLogin = incoming.clientLogin)
        listRepository.listenSystem().first()
    }
}