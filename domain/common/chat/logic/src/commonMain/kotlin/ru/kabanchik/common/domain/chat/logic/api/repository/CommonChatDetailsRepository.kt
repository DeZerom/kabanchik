package ru.kabanchik.common.domain.chat.logic.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage

interface CommonChatDetailsRepository {
    suspend fun reconnect(sessionId: String)
    suspend fun sendMessage(message: String)
    suspend fun listenSession(): Flow<CommonSessionMessage>
    suspend fun listenMessages(): Flow<CommonMessage>
    suspend fun endChat()
    suspend fun listenSessionEnd(): Flow<CommonMessage>
}
