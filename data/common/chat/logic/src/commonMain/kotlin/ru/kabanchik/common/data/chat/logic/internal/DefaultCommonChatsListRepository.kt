package ru.kabanchik.common.data.chat.logic.internal

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.data.chat.logic.api.CommonChatRestSource
import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.data.chat.logic.api.toDomain
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessageType
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository

internal class DefaultCommonChatsListRepository(
    private val stompSource: CommonStompSource,
    private val restSource: CommonChatRestSource,
) : CommonChatsListRepository {
    override suspend fun connect() {
        stompSource.connect()
    }

    override suspend fun getChats(): List<CommonChatSummary> {
        return coroutineScope {
            restSource.getChats()
                .map { summary ->
                    async {
                        val firstMessage = restSource.getMessages(summary.sessionId)
                            .firstOrNull { it.type == CommonApiMessageType.Text }

                        summary.toDomain().copy(firstMessageContent = firstMessage?.content)
                    }
                }
                .awaitAll()
        }
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return stompSource.listenMessages().map { it.toDomain() }
    }
}
