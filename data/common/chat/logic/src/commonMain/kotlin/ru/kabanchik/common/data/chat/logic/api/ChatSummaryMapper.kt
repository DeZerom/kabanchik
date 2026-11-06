package ru.kabanchik.common.data.chat.logic.api

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import kotlin.time.Instant

fun CommonApiChatSummary.toDomain(): CommonChatSummary = CommonChatSummary(
    sessionId = sessionId,
    status = status.toDomain(),
    participantName = participantName,
    firstMessageContent = null,
    lastMessageContent = lastMessageContent,
    lastMessageTimestamp = lastMessageTimestamp?.toSystemLocalDateTime(),
    lastMessageSender = lastMessageSender,
    closedAt = closedAt?.toSystemLocalDateTime(),
    closedBy = closedBy,
)

private fun String.toSystemLocalDateTime(): LocalDateTime =
    Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())
