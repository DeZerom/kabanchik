package ru.kabanchik.client.data.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kabanchik.client.data.chat.logic.api.ClientMessagesStompSource
import ru.kabanchik.client.domain.logic.chatDetails.api.repository.ClientChatDetailsRepository
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.chat.model.CommonSystemMessage
import ru.kabanchik.common.data.chat.logic.api.toApiSendMessage
import ru.kabanchik.common.data.chat.logic.api.toDomain

internal class DefaultClientChatDetailsRepository(
    private val messagesStompSource: ClientMessagesStompSource
) : ClientChatDetailsRepository {
    override suspend fun connect(token: String) {
        messagesStompSource.connect(token)
    }

    override suspend fun createChat() {
        messagesStompSource.startChat()
    }

    override suspend fun listenSystem(): Flow<CommonSystemMessage> {
        return messagesStompSource.listenSystem().map { it.toDomain() }
    }

    override suspend fun listenSession(): Flow<CommonSessionMessage> {
        return messagesStompSource.listenSession().map { it.toDomain() }
    }

    override suspend fun sendMessage(message: String) {
        messagesStompSource.send(message.toApiSendMessage())
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return messagesStompSource.listenMessages().map { it.toDomain() }
    }

    override suspend fun endChat() {
        messagesStompSource.endChat()
    }

    override suspend fun listenSessionEnd(): Flow<CommonMessage> {
        return messagesStompSource.listenSessionEnd().map { it.toDomain() }
    }
}