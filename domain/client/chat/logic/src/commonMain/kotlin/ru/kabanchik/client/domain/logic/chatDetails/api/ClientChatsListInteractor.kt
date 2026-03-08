package ru.kabanchik.client.domain.logic.chatDetails.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonSystemMessage

interface ClientChatsListInteractor {
    suspend fun connect()
    suspend fun createChat()
    suspend fun listenSystem(): Flow<CommonSystemMessage>
}