package ru.kabanchik.common.network.internal.api.chat

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.kabanchik.common.data.chat.logic.api.CommonChatRestSource
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage

internal class DefaultCommonChatRestSource(
    private val httpClient: HttpClient
) : CommonChatRestSource {
    override suspend fun getChats(): List<CommonApiChatSummary> {
        return httpClient.get(urlString = "/api/chats").body()
    }

    override suspend fun getMessages(sessionId: String): List<CommonApiMessage> {
        return httpClient.get(urlString = "/api/chats/$sessionId/messages").body()
    }
}
