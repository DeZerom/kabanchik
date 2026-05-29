package ru.kabanchik.common.data.chat.logic.api

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import kotlin.time.Instant

fun CommonApiChatSummary.toDomain(): CommonChatSummary = CommonChatSummary(
    sessionId = sessionId,
    participantName = participantName,
    lastMessageContent = lastMessageContent,
    lastMessageTimestamp = Instant.parse(lastMessageTimestamp).toLocalDateTime(TimeZone.currentSystemDefault()),
    lastMessageSender = lastMessageSender,
)
