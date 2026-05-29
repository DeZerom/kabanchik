package ru.kabanchik.common.domain.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatTokenRepository
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository

internal class DefaultCommonChatsListInteractor(
    private val tokenRepository: CommonChatTokenRepository,
    private val listRepository: CommonChatsListRepository
) : CommonChatsListInteractor {
    override suspend fun connect() {
        val token = tokenRepository.getToken().orEmpty()
        listRepository.connect(token)
    }

    override suspend fun getChats(): List<CommonChatSummary> {
        return listRepository.getChats()
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return listRepository.listenMessages()
    }
}
