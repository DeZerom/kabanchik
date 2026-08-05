package ru.kabanchik.client.domain.logic.chat.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage

interface ClientChatDetailsRepository {
    suspend fun reconnect(sessionId: String)
    suspend fun getMessages(sessionId: String): List<CommonMessage>
    suspend fun listenSession(): Flow<CommonSessionMessage>
    suspend fun sendMessage(
        sessionId: String,
        clientMessageId: String,
        content: String?,
        attachmentIds: List<String>,
    )
    suspend fun listenMessages(): Flow<CommonMessage>
    suspend fun endChat(sessionId: String)
    suspend fun listenSessionEnd(): Flow<CommonMessage>
}
