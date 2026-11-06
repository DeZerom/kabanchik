package ru.kabanchik.client.domain.logic.chat.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage

interface ClientChatsListInteractor {
    suspend fun connect()
    suspend fun listenReconnections(): Flow<Unit>
    suspend fun getChats(): List<CommonChatSummary>
    suspend fun getClosedChats(): List<CommonChatSummary>
    suspend fun listenMessages(): Flow<CommonMessage>

    /**
     * `sessionId` заявок, которые завершили.
     */
    suspend fun listenClosedChats(): Flow<String>
    suspend fun createChat(): CommonSessionMessage
}
