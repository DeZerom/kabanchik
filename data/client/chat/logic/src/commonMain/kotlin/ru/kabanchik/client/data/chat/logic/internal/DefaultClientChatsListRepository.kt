package ru.kabanchik.client.data.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kabanchik.client.data.chat.logic.api.ClientMessagesStompSource
import ru.kabanchik.client.data.chatDetails.model.ClientApiChatRequest
import ru.kabanchik.client.domain.logic.chat.api.repository.ClientChatsListRepository
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.data.chat.logic.api.toDomain
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository

internal class DefaultClientChatsListRepository(
    commonRepository: CommonChatsListRepository,
    private val stompSource: ClientMessagesStompSource
) : ClientChatsListRepository, CommonChatsListRepository by commonRepository {
    override suspend fun createChat(clientRequestId: String) {
        stompSource.startChat(ClientApiChatRequest(clientRequestId))
    }

    override suspend fun listenSession(): Flow<CommonSessionMessage> {
        return stompSource.listenSession().map { it.toDomain() }
    }
}
