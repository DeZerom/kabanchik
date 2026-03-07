package ru.kabanchik.pro.data.chatDetails.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kabanchik.pro.data.chatDetails.logic.api.ProMessagesStompSource
import ru.kabanchik.pro.data.chatDetails.logic.internal.mappers.toApi
import ru.kabanchik.pro.data.chatDetails.logic.internal.mappers.toDomain
import ru.kabanchik.pro.domain.chatDetails.logic.api.repository.ProChatDetailsRepository
import ru.kabanchik.pro.domain.chatDetails.model.ProMessage

internal class DefaultProChatDetailsRepository(
    private val stompSource: ProMessagesStompSource
) : ProChatDetailsRepository {
    override suspend fun initChat(token: String) {
        stompSource.connect(token)
    }

    override suspend fun sendMessage(message: ProMessage) {
        stompSource.send(message = message.toApi())
    }

    override suspend fun listenMessages(): Flow<ProMessage> {
        return stompSource.listenMessages().map { it.toDomain() }
    }
}