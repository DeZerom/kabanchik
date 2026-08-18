package ru.kabanchik.client.domain.logic.chat.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonAttachment

interface ClientChatDetailsInteractor {
    suspend fun reconnect(sessionId: String)
    suspend fun getMessages(sessionId: String): List<CommonChatMessage>
    suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        bytes: ByteArray,
    ): CommonAttachment
    suspend fun downloadFile(sessionId: String, fileId: String): ByteArray
    suspend fun sendMessage(
        sessionId: String,
        content: String?,
        attachmentIds: List<String>,
    )
    suspend fun listenMessages(sessionId: String): Flow<CommonChatMessage>
    suspend fun endChat(sessionId: String)
}
