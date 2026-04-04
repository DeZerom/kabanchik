package ru.kabanchik.common.chat.model

data class CommonSessionMessage(
    val sessionId: String,
    val participantLogin: String,
    val participantName: String,
    val participantRole: String
)
