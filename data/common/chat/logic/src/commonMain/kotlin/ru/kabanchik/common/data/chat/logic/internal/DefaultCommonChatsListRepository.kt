package ru.kabanchik.common.data.chat.logic.internal

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.data.chat.logic.api.CommonChatRestSource
import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.data.chat.logic.api.toDomain
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessageType
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository

internal class DefaultCommonChatsListRepository(
    private val stompSource: CommonStompSource,
    private val restSource: CommonChatRestSource,
) : CommonChatsListRepository {
    override suspend fun connect() {
        stompSource.connect()
    }

    override suspend fun listenReconnections(): Flow<Unit> {
        return stompSource.listenReconnections()
    }

    override suspend fun getChats(): List<CommonChatSummary> {
        return restSource.getChats().withFirstMessages()
    }

    override suspend fun getClosedChats(): List<CommonChatSummary> {
        return restSource.getClosedChats().withFirstMessages()
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return stompSource.listenMessages().map { it.toDomain() }
    }

    override suspend fun listenSession(): Flow<CommonSessionMessage> {
        return stompSource.listenSession().map { it.toDomain() }
    }

    // Заголовок чата - первое текстовое сообщение, в списке его нет
    private suspend fun List<CommonApiChatSummary>.withFirstMessages(): List<CommonChatSummary> {
        return coroutineScope {
            map { summary ->
                async {
                    val firstMessage = restSource.getMessages(summary.sessionId)
                        .firstOrNull { it.type == CommonApiMessageType.Text }

                    summary.toDomain().copy(firstMessageContent = firstMessage?.content)
                }
            }.awaitAll()
        }
    }
}
