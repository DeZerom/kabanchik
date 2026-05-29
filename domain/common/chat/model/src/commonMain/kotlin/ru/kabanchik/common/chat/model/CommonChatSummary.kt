package ru.kabanchik.common.chat.model

import kotlinx.datetime.LocalDateTime

data class CommonChatSummary(
    val sessionId: String,
    val participantName: String,
    val lastMessageContent: String,
    val lastMessageTimestamp: LocalDateTime,
    val lastMessageSender: String,
)
