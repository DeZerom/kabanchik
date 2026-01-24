package ru.kabanchik.client.domain.logic.chatDetails.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.domain.model.chatDetails.Message

interface ChatDetailsRepository {
    suspend fun initChat()
    suspend fun sendMessage(message: Message)
    fun listenMessages(): Flow<Message>
}