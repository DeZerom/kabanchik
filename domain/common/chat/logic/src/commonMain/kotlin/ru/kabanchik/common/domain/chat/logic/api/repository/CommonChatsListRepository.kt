package ru.kabanchik.common.domain.chat.logic.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage

interface CommonChatsListRepository {
    suspend fun connect()
    suspend fun listenReconnections(): Flow<Unit>
    suspend fun getChats(): List<CommonChatSummary>
    suspend fun getClosedChats(): List<CommonChatSummary>
    suspend fun listenMessages(): Flow<CommonMessage>
    suspend fun listenSession(): Flow<CommonSessionMessage>
}
