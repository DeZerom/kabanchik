package ru.kabanchik.pro.domain.chat.logic.internal

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.domain.chat.logic.api.CommonChatsListInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatsListRepository

internal class DefaultProChatsListInteractor(
    commonInteractor: CommonChatsListInteractor,
    private val listRepository: ProChatsListRepository
) : ProChatsListInteractor, CommonChatsListInteractor by commonInteractor {
    override suspend fun requestChat(): CommonSessionMessage {
        val incomingFlow = listRepository.listenIncoming()
        val reconnections = listRepository.listenReconnections()
        listRepository.register()

        val incoming = coroutineScope {
            // Регистрация привязана к STOMP-сессии: после переподключения ее нужно повторить
            val reRegistration = launch {
                reconnections.collect { listRepository.register() }
            }
            incomingFlow.first().also { reRegistration.cancel() }
        }
        val sessionFlow = listRepository.listenSession()
        return coroutineScope {
            val chatCreation = async {
                sessionFlow.filter { it.sessionId == incoming.sessionId }.first()
            }
            listRepository.acceptChat(sessionId = incoming.sessionId)

            chatCreation.await()
        }
    }
}
