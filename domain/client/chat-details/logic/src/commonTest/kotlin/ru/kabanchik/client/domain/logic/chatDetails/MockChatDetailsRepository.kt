package ru.kabanchik.client.domain.logic.chatDetails

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsRepository
import ru.kabanchik.client.domain.model.chatDetails.Message

class MockChatDetailsRepository : ChatDetailsRepository {
    val messages = mutableListOf<Message>()

    override suspend fun initChat() {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: Message) {
        messages.add(message)
    }

    override suspend fun listenMessages(): Flow<Message> {
        TODO("Not yet implemented")
    }
}