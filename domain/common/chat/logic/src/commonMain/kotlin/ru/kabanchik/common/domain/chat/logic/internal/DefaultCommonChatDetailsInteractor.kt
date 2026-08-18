package ru.kabanchik.common.domain.chat.logic.internal

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.domain.chat.logic.api.CommonChatDetailsInteractor
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatDetailsRepository
import ru.kabanchik.common.domain.chat.logic.api.splitAndTrimMessage
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DefaultCommonChatDetailsInteractor(
    private val detailsRepository: CommonChatDetailsRepository
) : CommonChatDetailsInteractor {
    override suspend fun reconnect(sessionId: String) {
        detailsRepository.reconnect(sessionId)
    }

    override suspend fun getMessages(sessionId: String): List<CommonChatMessage> {
        return detailsRepository.getMessages(sessionId)
            .toChatMessages()
    }

    override suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        bytes: ByteArray,
    ): CommonAttachment {
        return detailsRepository.uploadFile(
            sessionId = sessionId,
            fileName = fileName,
            contentType = contentType,
            bytes = bytes,
        )
    }

    override suspend fun downloadFile(sessionId: String, fileId: String): ByteArray {
        return detailsRepository.downloadFile(sessionId = sessionId, fileId = fileId)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun sendMessage(
        sessionId: String,
        content: String?,
        attachmentIds: List<String>,
    ) {
        val messageParts = content?.let(::splitAndTrimMessage) ?: listOf(null)

        messageParts.forEachIndexed { index, part ->
            detailsRepository.sendMessage(
                sessionId = sessionId,
                clientMessageId = Uuid.random().toString(),
                content = part,
                attachmentIds = if (index == 0) attachmentIds else emptyList(),
            )
        }
    }

    override suspend fun listenMessages(sessionId: String): Flow<CommonChatMessage> {
        val sessionMessageFlow = detailsRepository.listenSession()
        val messagesFlow = detailsRepository.listenMessages().filterBySessionId(sessionId)
        val sessionEndFlow = detailsRepository.listenSessionEnd().filterBySessionId(sessionId)

        var prevDate: LocalDate? = null

        return channelFlow {
            coroutineScope {
                sessionMessageFlow
                    .filter { it.sessionId == sessionId && it.status == CommonSessionStatus.Open }
                    .onEach {
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

    override suspend fun endChat(sessionId: String) {
        detailsRepository.endChat(sessionId)
    }

    private fun Flow<CommonMessage>.filterBySessionId(
        sessionId: String
    ): Flow<CommonMessage> {
        return filter { it.sessionId == sessionId }
    }

    private fun List<CommonMessage>.toChatMessages(): List<CommonChatMessage> {
        var prevDate: LocalDate? = null

        return flatMap { message ->
            if (prevDate != message.time.date) {
                prevDate = message.time.date
                listOf(
                    CommonChatMessage.Date(date = message.time.date),
                    CommonChatMessage.Message(message = message)
                )
            } else {
                listOf(CommonChatMessage.Message(message = message))
            }
        }
    }
}
