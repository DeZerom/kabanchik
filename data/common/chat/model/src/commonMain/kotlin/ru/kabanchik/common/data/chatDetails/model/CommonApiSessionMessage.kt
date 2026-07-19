package ru.kabanchik.common.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonApiSessionMessage(
    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("participant")
    val participant: CommonApiParticipant? = null,

    @SerialName("status")
    val status: CommonApiSessionStatus,
)

@Serializable
enum class CommonApiSessionStatus {
    @SerialName("WAITING")
    Waiting,

    @SerialName("OPEN")
    Open,

    @SerialName("CLOSED")
    Closed,
}

@Serializable
data class CommonApiParticipant(
    @SerialName("username")
    val login: String,

    @SerialName("displayName")
    val displayName: String,

    @SerialName("role")
    val role: String,
)
