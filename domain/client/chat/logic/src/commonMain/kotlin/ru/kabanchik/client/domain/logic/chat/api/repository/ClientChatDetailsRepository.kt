package ru.kabanchik.client.domain.logic.chat.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.chat.model.CommonSystemMessage

interface ClientChatDetailsRepository {
    suspend fun connect(token: String)
    suspend fun createChat()
    suspend fun listenSystem(): Flow<CommonSystemMessage>
    suspend fun listenSession(): Flow<CommonSessionMessage>
    suspend fun sendMessage(message: String)
    suspend fun listenMessages(): Flow<CommonMessage>
    suspend fun endChat()
    suspend fun listenSessionEnd(): Flow<CommonMessage>
}