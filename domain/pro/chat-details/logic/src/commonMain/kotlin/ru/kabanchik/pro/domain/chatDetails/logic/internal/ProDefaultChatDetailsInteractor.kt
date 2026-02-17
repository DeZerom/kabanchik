package ru.kabanchik.pro.domain.chatDetails.logic.internal

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.domain.chatDetails.logic.api.splitMessage
import ru.kabanchik.pro.domain.chatDetails.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chatDetails.logic.api.repository.ProChatDetailsRepository
import ru.kabanchik.pro.domain.chatDetails.logic.api.repository.ProChatDetailsTokenRepository
import ru.kabanchik.pro.domain.chatDetails.model.ProMessage

internal class ProDefaultChatDetailsInteractor(
    private val chatDetailsRepository: ProChatDetailsRepository,
    private val tokenRepository: ProChatDetailsTokenRepository
) : ProChatDetailsInteractor {
    override suspend fun initChat() {
        chatDetailsRepository.initChat(token = tokenRepository.getToken().orEmpty())
    }

    override suspend fun sendMessage(message: ProMessage) {
        val messagesParts = splitMessage(message.text)

        messagesParts.forEach { messagePart ->
            chatDetailsRepository.sendMessage(
                message = ProMessage(
                    authorLogin = message.authorLogin,
                    text = messagePart
                )
            )
        }
    }

    override suspend fun listenMessages(): Flow<ProMessage> {
        return chatDetailsRepository.listenMessages()
    }
}