package ru.kabanchik.common.domain.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository

internal class DefaultCommonChatsListInteractor(
    private val listRepository: CommonChatsListRepository
) : CommonChatsListInteractor {
    override suspend fun connect() {
        listRepository.connect()
    }

    override suspend fun getChats(): List<CommonChatSummary> {
        return listRepository.getChats()
            .sortedByDescending { chat -> chat.lastMessageTimestamp }
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return listRepository.listenMessages()
    }
}
