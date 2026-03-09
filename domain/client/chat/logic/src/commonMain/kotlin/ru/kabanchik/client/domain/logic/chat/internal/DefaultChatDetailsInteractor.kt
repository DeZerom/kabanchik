package ru.kabanchik.client.domain.logic.chat.internal

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.domain.logic.chat.api.ChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chat.api.repository.ClientChatDetailsRepository
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatTokenRepository
import ru.kabanchik.common.domain.chat.logic.api.splitAndTrimMessage

class DefaultChatDetailsInteractor(
    private val chatDetailsRepository: ClientChatDetailsRepository,
    private val tokenRepository: CommonChatTokenRepository
) : ChatDetailsInteractor {
    override suspend fun initChat() {
        val token = tokenRepository.getToken().orEmpty()
        chatDetailsRepository.connect(token = token)
    }

    override suspend fun sendMessage(message: CommonMessage) {
        val messages = splitAndTrimMessage(message.text)

        messages.forEach { messagePart ->
//            chatDetailsRepository.sendMessage(
//                message = CommonMessage(
//                    authorLogin = message.authorLogin,
//                    text = messagePart
//                )
//            )
        }
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return chatDetailsRepository.listenMessages()
    }
}