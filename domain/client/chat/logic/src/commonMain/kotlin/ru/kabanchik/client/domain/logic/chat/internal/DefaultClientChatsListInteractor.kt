package ru.kabanchik.client.domain.logic.chat.internal

import kotlinx.coroutines.flow.Flow
import ru.kabanchik.client.domain.logic.chat.api.ClientChatsListInteractor
import ru.kabanchik.client.domain.logic.chat.api.repository.ClientChatsListRepository
import ru.kabanchik.common.chat.model.CommonSystemMessage
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor

internal class DefaultClientChatsListInteractor(
    commonInteractor: CommonChatsListInteractor,
    private val listRepository: ClientChatsListRepository,
) : ClientChatsListInteractor, CommonChatsListInteractor by commonInteractor {
    override suspend fun createChat() {
        listRepository.createChat()
    }

    override suspend fun listenSystem(): Flow<CommonSystemMessage> {
        return listRepository.listenSystem()
    }
}