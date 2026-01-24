package ru.kabanchik.client.data.chatDetails.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kabanchik.client.data.chatDetails.internal.mappers.toApi
import ru.kabanchik.client.data.chatDetails.internal.mappers.toDomain
import ru.kabanchik.client.data.chatDetails.internal.models.ApiMessage
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsRepository
import ru.kabanchik.client.domain.model.chatDetails.Message
import ru.kabanchik.common.network.api.WebSocketSource
import ru.kabanchik.common.network.api.listen
import ru.kabanchik.common.network.api.sendSerialized

class DefaultChatDetailsRepository(
    private val webSocketSource: WebSocketSource
) : ChatDetailsRepository {
    override suspend fun initChat() {
        webSocketSource.connect(url = "ws://185.102.139.25:8080")
    }

    override suspend fun sendMessage(message: Message) {
        webSocketSource.sendSerialized(message.toApi())
    }

    override fun listenMessages(): Flow<Message> {
        return webSocketSource.listen<ApiMessage>().map { it.toDomain() }
    }
}