package ru.kabanchik.client.data.chatDetails.internal.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiMessage(
    @SerialName("login")
    val login: String,

    @SerialName("text")
    val text: String
)
