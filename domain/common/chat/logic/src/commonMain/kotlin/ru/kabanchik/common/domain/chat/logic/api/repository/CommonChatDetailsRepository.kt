package ru.kabanchik.common.domain.chat.logic.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.chat.model.CommonSessionMessage

interface CommonChatDetailsRepository {
    suspend fun reconnect(sessionId: String)
    suspend fun getMessages(sessionId: String): List<CommonMessage>
    suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        bytes: ByteArray,
    ): CommonAttachment
    suspend fun downloadFile(sessionId: String, fileId: String): ByteArray
    suspend fun sendMessage(
        sessionId: String,
        clientMessageId: String,
        content: String?,
        attachmentIds: List<String>,
    )
    suspend fun listenSession(): Flow<CommonSessionMessage>
    suspend fun listenMessages(): Flow<CommonMessage>
    suspend fun endChat(sessionId: String)
    suspend fun listenSessionEnd(): Flow<CommonMessage>
}
