package ru.kabanchik.common.data.chat.logic.internal

import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.data.chat.logic.api.CommonChatRestSource
import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.data.chat.logic.api.toDomain
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository

internal class DefaultCommonChatsListRepository(
    private val stompSource: CommonStompSource,
    private val restSource: CommonChatRestSource,
) : CommonChatsListRepository {
    override suspend fun connect(token: String) {
        stompSource.connect(token)
    }

    override suspend fun getChats(): List<CommonChatSummary> {
        return restSource.getChats().map { it.toDomain() }
    }
}
