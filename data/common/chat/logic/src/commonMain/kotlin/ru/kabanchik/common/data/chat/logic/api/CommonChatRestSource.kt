package ru.kabanchik.common.data.chat.logic.api

import ru.kabanchik.common.data.chatDetails.model.CommonApiChatFile
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.files.api.ReadableFile

interface CommonChatRestSource {
    suspend fun getChats(): List<CommonApiChatSummary>
    suspend fun getMessages(sessionId: String): List<CommonApiMessage>
    suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        file: ReadableFile,
    ): CommonApiChatFile
    suspend fun downloadFile(sessionId: String, fileId: String): ByteArray
}
