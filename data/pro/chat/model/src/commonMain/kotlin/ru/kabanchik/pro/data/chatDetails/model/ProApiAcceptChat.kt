package ru.kabanchik.pro.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProApiAcceptChat(
    @SerialName("sessionId")
    val sessionId: String
)
