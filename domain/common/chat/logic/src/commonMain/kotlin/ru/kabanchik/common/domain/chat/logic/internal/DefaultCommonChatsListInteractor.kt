package ru.kabanchik.common.domain.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository

internal class DefaultCommonChatsListInteractor(
    private val listRepository: CommonChatsListRepository
) : CommonChatsListInteractor {
    override suspend fun connect() {
        listRepository.connect()
    }

    override suspend fun listenReconnections(): Flow<Unit> {
        return listRepository.listenReconnections()
    }

    override suspend fun getChats(): List<CommonChatSummary> {
        return listRepository.getChats()
            .sortedByDescending { chat -> chat.lastMessageTimestamp }
    }

    // Сервер уже отсортировал завершенные заявки по времени завершения
    override suspend fun getClosedChats(): List<CommonChatSummary> {
        return listRepository.getClosedChats()
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return listRepository.listenMessages()
    }

    override suspend fun listenClosedChats(): Flow<String> {
        return listRepository.listenSession()
            .filter { it.status == CommonSessionStatus.Closed }
            .map { it.sessionId }
    }
}
