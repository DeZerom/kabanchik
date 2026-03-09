package ru.kabanchik.pro.domain.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.domain.chat.logic.api.splitAndTrimMessage
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatDetailsRepository

internal class DefaultProChatDetailsInteractor(
    private val chatDetailsRepository: ProChatDetailsRepository,
) : ProChatDetailsInteractor {
    override suspend fun initChat() {
//        chatDetailsRepository.initChat(token = tokenRepository.getToken().orEmpty())
    }

    override suspend fun sendMessage(message: String) {
        val messagesParts = splitAndTrimMessage(message)

        messagesParts.forEach { messagePart ->
//            chatDetailsRepository.sendMessage(
//                message = ProMessage(
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