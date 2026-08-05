package ru.kabanchik.pro.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProApiIncoming(
    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("participant")
    val participant: ProApiParticipant
)

@Serializable
data class ProApiParticipant(
    @SerialName("username")
    val login: String,

    @SerialName("displayName")
    val displayName: String,

    @SerialName("role")
    val role: String,
)
