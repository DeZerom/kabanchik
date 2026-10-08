package ru.kabanchik.common.data.chat.logic.api

import ru.kabanchik.common.data.chat.logic.api.CommonChatRestSource.Companion.MaxFilesPerUpload
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatFile
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.files.api.ReadableFile

interface CommonChatRestSource {
    suspend fun getChats(): List<CommonApiChatSummary>
    suspend fun getMessages(sessionId: String): List<CommonApiMessage>

    /**
     * Загружает от 1 до [MaxFilesPerUpload] файлов одним запросом.
     * Порядок результата соответствует порядку [files].
     */
    suspend fun uploadFiles(
        sessionId: String,
        files: List<CommonUploadFile>,
    ): List<CommonApiChatFile>
    suspend fun downloadFile(sessionId: String, fileId: String): ByteArray

    companion object {
        const val MaxFilesPerUpload: Int = 10
        const val MaxFileSizeBytes: Long = 500L * 1024 * 1024
    }
}

data class CommonUploadFile(
    val fileName: String,
    val contentType: String,
    val file: ReadableFile,
)
