package ru.kabanchik.common.network.internal.api.chat

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.timeout
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import ru.kabanchik.common.data.chat.logic.api.CommonChatRestSource
import ru.kabanchik.common.data.chat.logic.api.CommonUploadFile
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatFile
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage

internal class DefaultCommonChatRestSource(
    private val httpClient: HttpClient
) : CommonChatRestSource {
    override suspend fun getChats(): List<CommonApiChatSummary> {
        return httpClient.get(urlString = "/chat/api/chats").body()
    }

    override suspend fun getMessages(sessionId: String): List<CommonApiMessage> {
        return httpClient.get(urlString = "/chat/api/chats/$sessionId/messages").body()
    }

    override suspend fun uploadFiles(
        sessionId: String,
        files: List<CommonUploadFile>,
    ): List<CommonApiChatFile> {
        require(files.size in 1..CommonChatRestSource.MaxFilesPerUpload) {
            "Upload requires 1..${CommonChatRestSource.MaxFilesPerUpload} files, got ${files.size}"
        }

        return httpClient.post(urlString = "/chat/api/chats/$sessionId/files") {
            // Файлы до 500 МБ: общий таймаут запроса снимаем, socket timeout остаётся.
            timeout { requestTimeoutMillis = HttpTimeoutConfig.INFINITE_TIMEOUT_MS }
            setBody(createFilesUploadBody(files))
        }.body()
    }

    override suspend fun downloadFile(sessionId: String, fileId: String): ByteArray {
        return httpClient.get(urlString = "/chat/api/chats/$sessionId/files/$fileId").body()
    }
}

internal fun createFilesUploadBody(files: List<CommonUploadFile>): MultiPartFormDataContent {
    return MultiPartFormDataContent(
        formData {
            files.forEach { uploadFile ->
                append(
                    key = FilesPartName,
                    value = InputProvider(size = uploadFile.file.size) {
                        uploadFile.file.openSource()
                    },
                    headers = Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=\"${uploadFile.fileName}\"")
                        append(HttpHeaders.ContentType, uploadFile.contentType)
                    }
                )
            }
        }
    )
}

private const val FilesPartName = "files"
