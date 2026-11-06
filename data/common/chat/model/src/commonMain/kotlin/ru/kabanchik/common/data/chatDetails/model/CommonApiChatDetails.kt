package ru.kabanchik.common.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonApiChatDetails(
    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("clientRequestId")
    val clientRequestId: String? = null,

    @SerialName("status")
    val status: CommonApiSessionStatus,

    @SerialName("participant")
    val participant: CommonApiParticipant? = null,

    @SerialName("createdAt")
    val createdAt: String? = null,

    @SerialName("closedAt")
    val closedAt: String? = null,

    @SerialName("closedBy")
    val closedBy: String? = null,
)
