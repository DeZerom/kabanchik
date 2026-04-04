package ru.kabanchik.common.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonApiSessionMessage(
    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("participant")
    val participant: CommonApiParticipant,
)

@Serializable
data class CommonApiParticipant(
    @SerialName("username")
    val login: String,

    @SerialName("displayName")
    val displayName: String,

    @SerialName("role")
    val role: String,
)
