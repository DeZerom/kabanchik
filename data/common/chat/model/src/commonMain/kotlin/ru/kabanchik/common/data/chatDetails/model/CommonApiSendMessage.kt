package ru.kabanchik.common.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonApiSendMessage(
    @SerialName("clientMessageId")
    val clientMessageId: String,

    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("content")
    val content: String?,

    @SerialName("attachmentIds")
    val attachmentIds: List<String>,
)
