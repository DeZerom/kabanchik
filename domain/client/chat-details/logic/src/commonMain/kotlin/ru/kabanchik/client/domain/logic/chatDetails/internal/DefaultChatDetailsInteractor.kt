package ru.kabanchik.client.domain.logic.chatDetails.internal

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsRepository
import ru.kabanchik.client.domain.model.chatDetails.Message

private const val MessageMaxLength = 4096

class DefaultChatDetailsInteractor(
    private val chatDetailsRepository: ChatDetailsRepository
) : ChatDetailsInteractor {
    override suspend fun initChat() {
        return chatDetailsRepository.initChat()
    }

    override suspend fun sendMessage(message: Message) {
        if (message.text.length <= MessageMaxLength) {
            chatDetailsRepository.sendMessage(message)
            return
        }

        val messages = mutableListOf<Message>()
        for (i in 0..(message.text.length / MessageMaxLength)) {
            val startIndex = i * MessageMaxLength
            val endIndex = (startIndex + MessageMaxLength).coerceAtMost(message.text.length)
            val part = message.text.substring(startIndex = startIndex, endIndex = endIndex)
            val partialMessage = Message(
                authorLogin = message.authorLogin,
                text = part
            )
            messages.add(partialMessage)
        }

        messages.forEach {
            chatDetailsRepository.sendMessage(it)
        }
    }

    override suspend fun listenMessages(): Flow<Message> {
        return chatDetailsRepository.listenMessages()
    }
}