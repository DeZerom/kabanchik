package ru.kabanchik.common.domain.chat.logic.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage

interface CommonChatsListRepository {
    suspend fun connect(token: String)
    suspend fun getChats(): List<CommonChatSummary>
    suspend fun listenMessages(): Flow<CommonMessage>
}
