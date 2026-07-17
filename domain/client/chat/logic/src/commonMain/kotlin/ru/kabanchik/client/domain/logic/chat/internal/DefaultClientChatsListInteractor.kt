package ru.kabanchik.client.domain.logic.chat.internal

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import ru.kabanchik.client.domain.logic.chat.api.ClientChatsListInteractor
import ru.kabanchik.client.domain.logic.chat.api.repository.ClientChatsListRepository
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor

internal class DefaultClientChatsListInteractor(
    commonInteractor: CommonChatsListInteractor,
    private val listRepository: ClientChatsListRepository,
) : ClientChatsListInteractor, CommonChatsListInteractor by commonInteractor {
    override suspend fun createChat(): CommonSessionMessage {
        val sessionFlow = listRepository.listenSession()

        return coroutineScope {
            val chatCreation = async { sessionFlow.first() }
            listRepository.createChat()

            chatCreation.await()
        }
    }
}
