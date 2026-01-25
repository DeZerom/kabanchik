package ru.kabanchik.client.domain.logic.chatDetails.internal

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsRepository
import ru.kabanchik.client.domain.model.chatDetails.Message

class DefaultChatDetailsInteractor(
    private val chatDetailsRepository: ChatDetailsRepository
) : ChatDetailsInteractor {
    override suspend fun initChat() {
        return chatDetailsRepository.initChat()
    }

    override suspend fun sendMessage(message: Message) {
        chatDetailsRepository.sendMessage(message)
    }

    override suspend fun listenMessages(): Flow<Message> {
        return chatDetailsRepository.listenMessages()
    }
}