package ru.kabanchik.client.domain.logic.chatDetails.internal

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chatDetails.api.repository.ChatDetailsRepository
import ru.kabanchik.client.domain.logic.chatDetails.api.repository.ChatDetailsTokenRepository
import ru.kabanchik.client.domain.model.chatDetails.Message
import ru.kabanchik.common.domain.chatDetails.logic.api.splitAndTrimMessage

class DefaultChatDetailsInteractor(
    private val chatDetailsRepository: ChatDetailsRepository,
    private val tokenRepository: ChatDetailsTokenRepository
) : ChatDetailsInteractor {
    override suspend fun initChat() {
        val token = tokenRepository.getToken().orEmpty()
        return chatDetailsRepository.initChat(token = token)
    }

    override suspend fun sendMessage(message: Message) {
        val messages = splitAndTrimMessage(message.text)

        messages.forEach { messagePart ->
            chatDetailsRepository.sendMessage(
                message = Message(
                    authorLogin = message.authorLogin,
                    text = messagePart
                )
            )
        }
    }

    override suspend fun listenMessages(): Flow<Message> {
        return chatDetailsRepository.listenMessages()
    }
}