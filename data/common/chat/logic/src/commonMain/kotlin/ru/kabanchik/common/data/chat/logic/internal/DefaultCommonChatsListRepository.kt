package ru.kabanchik.common.data.chat.logic.internal

import ru.kabanchik.common.data.chat.logic.api.CommonStompSource
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository

internal class DefaultCommonChatsListRepository(
    private val  stompSource: CommonStompSource
) : CommonChatsListRepository {
    override suspend fun connect(token: String) {
        stompSource.connect(token)
    }
}