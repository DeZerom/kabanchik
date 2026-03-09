package ru.kabanchik.pro.data.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.data.chat.logic.api.toApiSendMessage
import ru.kabanchik.common.data.chat.logic.api.toDomain
import ru.kabanchik.pro.data.chat.logic.api.ProMessagesStompSource
import ru.kabanchik.pro.data.chat.logic.internal.mappers.toDomain
import ru.kabanchik.pro.data.chatDetails.model.ProApiAcceptChat
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatDetailsRepository
import ru.kabanchik.pro.domain.chatDetails.model.ProIncoming

internal class DefaultProChatDetailsRepository(
    private val stompSource: ProMessagesStompSource
) : ProChatDetailsRepository {
    override suspend fun connect(token: String) {
        stompSource.connect(token)
    }

    override suspend fun listenIncoming(): Flow<ProIncoming> {
        return stompSource.listenIncoming().map { it.toDomain() }
    }

    override suspend fun acceptChat(clientLogin: String) {
        stompSource.acceptChat(ProApiAcceptChat(clientLogin))
    }

    override suspend fun listenSession(): Flow<CommonSessionMessage> {
        return stompSource.listenSession().map { it.toDomain() }
    }

    override suspend fun sendMessage(message: String) {
        stompSource.send(message.toApiSendMessage())
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return stompSource.listenMessages().map { it.toDomain() }
    }

    override suspend fun endChat() {
        return stompSource.endChat()
    }

    override suspend fun listenSessionEnd(): Flow<CommonMessage> {
        return stompSource.listenSessionEnd().map { it.toDomain() }
    }
}