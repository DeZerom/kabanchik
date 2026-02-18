package ru.kabanchik.client.data.chatDetails.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kabanchik.client.data.chatDetails.logic.api.MessagesStompSource
import ru.kabanchik.client.data.chatDetails.logic.internal.mappers.toApi
import ru.kabanchik.client.data.chatDetails.logic.internal.mappers.toDomain
import ru.kabanchik.client.domain.logic.chatDetails.api.repository.ChatDetailsRepository
import ru.kabanchik.client.domain.model.chatDetails.Message

internal class DefaultChatDetailsRepository(
    private val messagesStompSource: MessagesStompSource
) : ChatDetailsRepository {
    override suspend fun initChat(token: String) {
        messagesStompSource.connect(token)
    }

    override suspend fun sendMessage(message: Message) {
        messagesStompSource.send(message.toApi())
    }

    override suspend fun listenMessages(): Flow<Message> {
        return messagesStompSource.listenMessages().map { it.toDomain() }
    }
}