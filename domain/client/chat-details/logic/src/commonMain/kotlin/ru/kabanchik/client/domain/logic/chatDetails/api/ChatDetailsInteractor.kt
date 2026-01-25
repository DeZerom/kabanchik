package ru.kabanchik.client.domain.logic.chatDetails.api

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.domain.model.chatDetails.Message

interface ChatDetailsInteractor {
    suspend fun initChat()
    suspend fun sendMessage(message: Message)
    suspend fun listenMessages(): Flow<Message>
}