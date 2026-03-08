package ru.kabanchik.client.domain.logic.chatDetails.internal

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.domain.logic.chatDetails.api.ClientChatsListInteractor
import ru.kabanchik.client.domain.logic.chatDetails.api.repository.ChatDetailsTokenRepository
import ru.kabanchik.client.domain.logic.chatDetails.api.repository.ClientChatDetailsRepository
import ru.kabanchik.common.chat.model.CommonSystemMessage

class DefaultClientChatsListInteractor(
    private val chatDetailsRepository: ClientChatDetailsRepository,
    private val tokenRepository: ChatDetailsTokenRepository
) : ClientChatsListInteractor {
    override suspend fun connect() {
        chatDetailsRepository.connect(tokenRepository.getToken().orEmpty())
    }

    override suspend fun createChat() {
        chatDetailsRepository.createChat()
    }

    override suspend fun listenSystem(): Flow<CommonSystemMessage> {
        return chatDetailsRepository.listenSystem()
    }
}