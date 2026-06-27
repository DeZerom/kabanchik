package ru.kabanchik.common.data.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.data.chat.logic.api.CommonChatRestSource
import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.data.chat.logic.api.toDomain
import ru.kabanchik.common.data.chatDetails.model.CommonApiReconnectMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSendMessage
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatDetailsRepository

internal class DefaultCommonChatDetailsRepository(
    private val commonStompSource: CommonStompSource,
    private val commonRestSource: CommonChatRestSource
) : CommonChatDetailsRepository {
    override suspend fun reconnect(sessionId: String) {
        commonStompSource.reconnect(CommonApiReconnectMessage(sessionId))
    }

    override suspend fun getMessages(sessionId: String): List<CommonMessage> {
        return commonRestSource.getMessages(sessionId).map { it.toDomain() }
    }

    override suspend fun sendMessage(message: String) {
        commonStompSource.send(
            CommonApiSendMessage(content = message)
        )
    }

    override suspend fun listenSession(): Flow<CommonSessionMessage> {
        return commonStompSource.listenSession().map { it.toDomain() }
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return commonStompSource.listenMessages().map { it.toDomain() }
    }

    override suspend fun endChat() {
        commonStompSource.endChat()
    }

    override suspend fun listenSessionEnd(): Flow<CommonMessage> {
        return commonStompSource.listenSessionEnd().map { it.toDomain() }
    }
}
