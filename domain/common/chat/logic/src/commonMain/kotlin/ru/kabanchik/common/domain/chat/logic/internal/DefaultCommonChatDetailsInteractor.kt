package ru.kabanchik.common.domain.chat.logic.internal

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

        var prevDate: LocalDate? = null

        return channelFlow {
            coroutineScope {
                sessionMessageFlow.onEach {
                    send(CommonChatMessage.OperatorFound)
                }.launchIn(this)
                sessionEndFlow.onEach {
                    send(CommonChatMessage.SessionEnd)
                }.launchIn(this)
                messagesFlow.onEach { message ->

                    if (prevDate != message.time.date) {
                        send(CommonChatMessage.Date(date = message.time.date))
                        prevDate = message.time.date
                    }

                    send(CommonChatMessage.Message(message = message))
                }.launchIn(this)
            }
        }
    }

    override suspend fun endChat() {
        detailsRepository.endChat()
    }
}