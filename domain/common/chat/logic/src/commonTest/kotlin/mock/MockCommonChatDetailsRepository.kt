package mock

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatDetailsRepository

class MockCommonChatDetailsRepository(
    private val messages: List<CommonMessage>,
    private val uploadedFile: CommonAttachment = CommonAttachment(
        fileId = "uploaded-file-id",
        originalName = "document.pdf",
        contentType = "application/pdf",
        size = 3L,
        downloadUrl = "/files/uploaded-file-id",
    ),
    private val downloadedBytes: ByteArray = byteArrayOf(1, 2, 3),
) : CommonChatDetailsRepository {
    var reconnectedSessionId: String? = null
    var requestedMessagesSessionId: String? = null
    val sentMessages = mutableListOf<SentMessage>()
    var uploadedFileRequest: UploadedFileRequest? = null
    var downloadedFileRequest: DownloadedFileRequest? = null

    override suspend fun reconnect(sessionId: String) {
        reconnectedSessionId = sessionId
    }

    override suspend fun getMessages(sessionId: String): List<CommonMessage> {
        requestedMessagesSessionId = sessionId
        return messages
    }

    override suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        bytes: ByteArray,
    ): CommonAttachment {
        uploadedFileRequest = UploadedFileRequest(
            sessionId = sessionId,
            fileName = fileName,
            contentType = contentType,
            bytes = bytes,
        )
        return uploadedFile
    }

    override suspend fun downloadFile(sessionId: String, fileId: String): ByteArray {
        downloadedFileRequest = DownloadedFileRequest(sessionId = sessionId, fileId = fileId)
        return downloadedBytes
    }

    override suspend fun sendMessage(
        sessionId: String,
        clientMessageId: String,
        content: String?,
        attachmentIds: List<String>,
    ) {
        sentMessages += SentMessage(
            sessionId = sessionId,
            clientMessageId = clientMessageId,
            content = content,
            attachmentIds = attachmentIds,
        )
    }

    override suspend fun listenSession(): Flow<CommonSessionMessage> {
        return emptyFlow()
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return flow {
            messages.forEach { message -> emit(message) }
        }
    }

    override suspend fun endChat(sessionId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun listenSessionEnd(): Flow<CommonMessage> {
        return emptyFlow()
    }

    data class SentMessage(
        val sessionId: String,
        val clientMessageId: String,
        val content: String?,
        val attachmentIds: List<String>,
    )

    data class UploadedFileRequest(
        val sessionId: String,
        val fileName: String,
        val contentType: String,
        val bytes: ByteArray,
    )

    data class DownloadedFileRequest(
        val sessionId: String,
        val fileId: String,
    )
}
