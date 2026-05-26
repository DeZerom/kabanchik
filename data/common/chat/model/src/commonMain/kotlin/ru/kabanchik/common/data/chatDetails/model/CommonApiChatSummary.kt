package ru.kabanchik.common.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonApiChatSummary(
    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("participantName")
    val participantName: String,

    @SerialName("lastMessageContent")
    val lastMessageContent: String,

    @SerialName("lastMessageTimestamp")
    val lastMessageTimestamp: String,

    @SerialName("lastMessageSender")
    val lastMessageSender: String,
)
