package ru.kabanchik.common.domain.chat.logic.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonChatMessage

interface CommonChatDetailsInteractor {
    suspend fun reconnect(sessionId: String)
    suspend fun getMessages(sessionId: String): List<CommonChatMessage>
    suspend fun sendMessage(sessionId: String, message: String)
    suspend fun listenMessages(sessionId: String): Flow<CommonChatMessage>
    suspend fun endChat(sessionId: String)
}
