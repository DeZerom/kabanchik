package ru.kabanchik.pro.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiProMessage(
    @SerialName("sender")
    val login: String,

    @SerialName("content")
    val text: String
)