package ru.kabanchik.client.domain.logic.chatDetails.api.repository

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.domain.model.chatDetails.Message

interface ChatDetailsRepository {
    suspend fun initChat(token: String)
    suspend fun sendMessage(message: Message)
    suspend fun listenMessages(): Flow<Message>
}