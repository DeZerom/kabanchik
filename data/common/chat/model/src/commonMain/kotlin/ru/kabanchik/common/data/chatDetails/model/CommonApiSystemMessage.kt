package ru.kabanchik.common.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonApiSystemMessage(
    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("content")
    val content: String
)

@Serializable
data class CommonApiErrorMessage(
    @SerialName("code")
    val code: String,

    @SerialName("content")
    val content: String,

    @SerialName("sessionId")
    val sessionId: String? = null,

    @SerialName("clientRequestId")
    val clientRequestId: String? = null,
)
