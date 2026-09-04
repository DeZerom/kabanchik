package ru.kabanchik.common.network.internal.api.chat

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import ru.kabanchik.common.data.chat.logic.api.CommonChatRestSource
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatFile
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.files.api.ReadableFile

internal class DefaultCommonChatRestSource(
    private val httpClient: HttpClient
) : CommonChatRestSource {
    override suspend fun getChats(): List<CommonApiChatSummary> {
        return httpClient.get(urlString = "/chat/api/chats").body()
    }

    override suspend fun getMessages(sessionId: String): List<CommonApiMessage> {
        return httpClient.get(urlString = "/chat/api/chats/$sessionId/messages").body()
    }

    override suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        file: ReadableFile,
    ): CommonApiChatFile {
        return httpClient.post(urlString = "/chat/api/chats/$sessionId/files") {
            setBody(createFileUploadBody(fileName, contentType, file))
        }.body()
    }

    override suspend fun downloadFile(sessionId: String, fileId: String): ByteArray {
        return httpClient.get(urlString = "/chat/api/chats/$sessionId/files/$fileId").body()
    }
}

internal fun createFileUploadBody(
    fileName: String,
    contentType: String,
    file: ReadableFile,
): MultiPartFormDataContent {
    return MultiPartFormDataContent(
        formData {
            append(
                key = "file",
                value = InputProvider(size = file.size) {
                    file.openSource()
                },
                headers = Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    append(HttpHeaders.ContentType, contentType)
                }
            )
        }
    )
}
