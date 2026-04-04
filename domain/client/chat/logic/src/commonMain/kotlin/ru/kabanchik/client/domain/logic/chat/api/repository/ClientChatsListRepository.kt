package ru.kabanchik.client.domain.logic.chat.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonSystemMessage

interface ClientChatsListRepository {
    suspend fun connect(token: String)
    suspend fun createChat()
    suspend fun listenSystem(): Flow<CommonSystemMessage>
}