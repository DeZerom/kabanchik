package ru.kabanchik.common.chat.model

data class CommonSessionMessage(
    val sessionId: String,
    val status: CommonSessionStatus,
    val participantLogin: String? = null,
    val participantName: String? = null,
    val participantRole: String? = null,
)

enum class CommonSessionStatus {
    Waiting,
    Open,
    Closed,
}
