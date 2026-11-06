package ru.kabanchik.common.domain.chat.logic.internal

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.domain.chat.logic.api.CommonChatDetailsInteractor
import ru.kabanchik.common.domain.chat.logic.api.MaxAttachmentsPerMessage
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatDetailsRepository
import ru.kabanchik.common.domain.chat.logic.api.splitAndTrimMessage
import ru.kabanchik.common.files.api.ReadableFile
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DefaultCommonChatDetailsInteractor(
    private val detailsRepository: CommonChatDetailsRepository
) : CommonChatDetailsInteractor {
    override suspend fun reconnect(sessionId: String) {
        detailsRepository.reconnect(sessionId)
    }

    override suspend fun listenReconnections(): Flow<Unit> {
        return detailsRepository.listenReconnections()
    }

    override suspend fun getMessages(sessionId: String): List<CommonChatMessage> {
        return detailsRepository.getMessages(sessionId)
            .toChatMessages()
    }

    override suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        file: ReadableFile,
    ): CommonAttachment {
        return detailsRepository.uploadFile(
            sessionId = sessionId,
            fileName = fileName,
            contentType = contentType,
            file = file,
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
        require(attachmentIds.size <= MaxAttachmentsPerMessage) {
            "Message can contain at most $MaxAttachmentsPerMessage attachments"
        }

        val messageParts = content?.let(::splitAndTrimMessage) ?: listOf(null)

        messageParts.forEachIndexed { index, part ->
            detailsRepository.sendMessage(
                sessionId = sessionId,
                clientMessageId = Uuid.random().toString(),
                content = part,
                attachmentIds = if (index == messageParts.lastIndex) attachmentIds else emptyList(),
            )
        }
    }

    override suspend fun listenMessages(sessionId: String): Flow<CommonChatMessage> {
        val sessionMessageFlow = detailsRepository.listenSession()
        val messagesFlow = detailsRepository.listenMessages().filterBySessionId(sessionId)
        val sessionEndFlow = detailsRepository.listenSessionEnd().filterBySessionId(sessionId)

        var prevDate: LocalDate? = null

        return channelFlow {
            suspend fun sendWithDate(message: CommonMessage) {
                if (prevDate != message.time.date) {
                    send(CommonChatMessage.Date(date = message.time.date))
                    prevDate = message.time.date
                }
                send(message.toChatMessage())
            }

            coroutineScope {
                sessionMessageFlow
                    .filter { it.sessionId == sessionId && it.status == CommonSessionStatus.Open }
                    .onEach {
                        send(CommonChatMessage.OperatorFound)
                    }.launchIn(this)
                sessionEndFlow.onEach { message ->
                    sendWithDate(message)
                }.launchIn(this)
                messagesFlow.onEach { message ->
                    sendWithDate(message)
                }.launchIn(this)
            }
        }
    }

    override suspend fun getStatus(sessionId: String): CommonSessionStatus {
        return detailsRepository.getChatStatus(sessionId)
    }

    override suspend fun listenClosed(sessionId: String): Flow<Unit> {
        val closedSessions = detailsRepository.listenSession()
            .filter { it.sessionId == sessionId && it.status == CommonSessionStatus.Closed }
            .map { }
        val sessionEnds = detailsRepository.listenSessionEnd()
            .filterBySessionId(sessionId)
            .map { }
        return merge(closedSessions, sessionEnds)
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
                    message.toChatMessage()
                )
            } else {
                listOf(message.toChatMessage())
            }
        }
    }

    // Системные сообщения в истории создаются только при завершении заявки
    private fun CommonMessage.toChatMessage(): CommonChatMessage {
        return if (type == CommonMessageType.System) {
            CommonChatMessage.SessionEnd(message = this)
        } else {
            CommonChatMessage.Message(message = this)
        }
    }
}
