package ru.kabanchik.client.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientApiChatRequest(
    @SerialName("clientRequestId")
    val clientRequestId: String,
)
