package ru.kabanchik.client.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiMessage(
    @SerialName("sender")
    val login: String,

    @SerialName("content")
    val text: String
)