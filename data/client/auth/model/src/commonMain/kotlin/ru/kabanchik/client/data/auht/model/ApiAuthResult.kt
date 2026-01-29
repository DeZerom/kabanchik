package ru.kabanchik.client.data.auht.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiAuthResult(
    @SerialName("status")
    val status: String,

    @SerialName("message")
    val message: String
)
