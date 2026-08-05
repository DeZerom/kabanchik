package ru.kabanchik.common.data.chat.logic.api

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
    lastMessageTimestamp = lastMessageTimestamp
        ?.let(Instant::parse)
        ?.toLocalDateTime(TimeZone.currentSystemDefault()),
    lastMessageSender = lastMessageSender,
)
