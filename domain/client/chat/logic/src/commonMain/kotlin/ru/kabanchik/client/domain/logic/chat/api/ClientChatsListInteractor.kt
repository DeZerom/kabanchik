package ru.kabanchik.client.domain.logic.chat.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonSystemMessage

interface ClientChatsListInteractor {
    suspend fun connect()
    suspend fun getChats(): List<CommonChatSummary>
    suspend fun createChat()
    suspend fun listenSystem(): Flow<CommonSystemMessage>
}
