package ru.kabanchik.common.domain.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.domain.chat.logic.api.CommonChatDetailsInteractor
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatDetailsRepository
import ru.kabanchik.common.domain.chat.logic.api.splitAndTrimMessage

class DefaultCommonChatDetailsInteractor(
    private val detailsRepository: CommonChatDetailsRepository
) : CommonChatDetailsInteractor {
    override suspend fun sendMessage(message: String) {
        val messages = splitAndTrimMessage(message)

        messages.forEach {
            detailsRepository.sendMessage(it)
        }
    }

    override suspend fun listenMessages(): Flow<CommonChatMessage> {
        val sessionMessageFlow = detailsRepository.listenSession()
        val messagesFlow = detailsRepository.listenMessages()
        val sessionEndFlow = detailsRepository.listenSessionEnd()

        return flow {
            sessionMessageFlow.collect {
                emit(CommonChatMessage.OperatorFound)
            }
            sessionEndFlow.collect {
                emit(CommonChatMessage.SessionEnd)
            }
            messagesFlow.collect { message ->
                var prevDate: LocalDate? = null

                if (prevDate != null && prevDate != message.time.date) {
                    emit(CommonChatMessage.Date(date = message.time.date))
                }

                prevDate = message.time.date
                emit(CommonChatMessage.Message(message = message))
            }
        }
    }

    override suspend fun endChat() {
        detailsRepository.endChat()
    }
}