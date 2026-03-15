package ru.kabanchik.client.domain.logic.chat.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatMessage

interface ClientChatDetailsInteractor {
    suspend fun sendMessage(message: String)
    suspend fun listenMessages(): Flow<CommonChatMessage>
    suspend fun endChat()
}