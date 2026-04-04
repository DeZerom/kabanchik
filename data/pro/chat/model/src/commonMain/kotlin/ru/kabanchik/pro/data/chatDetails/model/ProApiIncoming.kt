package ru.kabanchik.pro.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProApiIncoming(
    @SerialName("content")
    val userName: String
)
