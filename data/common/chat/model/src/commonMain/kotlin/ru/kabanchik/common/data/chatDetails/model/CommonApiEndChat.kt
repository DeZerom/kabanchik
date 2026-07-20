package ru.kabanchik.common.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonApiEndChat(
    @SerialName("sessionId")
    val sessionId: String,
)
