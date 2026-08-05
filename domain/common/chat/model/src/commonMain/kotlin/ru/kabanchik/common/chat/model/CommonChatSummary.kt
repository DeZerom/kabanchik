package ru.kabanchik.common.chat.model

import kotlinx.datetime.LocalDateTime

data class CommonChatSummary(
    val sessionId: String,
    val status: CommonSessionStatus,
    val participantName: String?,
    val firstMessageContent: String?,
    val lastMessageContent: String?,
    val lastMessageTimestamp: LocalDateTime?,
    val lastMessageSender: String?,
)
