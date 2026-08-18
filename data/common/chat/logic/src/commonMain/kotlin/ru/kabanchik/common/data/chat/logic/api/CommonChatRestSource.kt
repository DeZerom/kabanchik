package ru.kabanchik.common.data.chat.logic.api

import ru.kabanchik.common.data.chatDetails.model.CommonApiChatFile
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage

interface CommonChatRestSource {
    suspend fun getChats(): List<CommonApiChatSummary>
    suspend fun getMessages(sessionId: String): List<CommonApiMessage>
    suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        bytes: ByteArray,
    ): CommonApiChatFile
    suspend fun downloadFile(sessionId: String, fileId: String): ByteArray
}
