package ru.kabanchik.pro.data.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiProAuthResult(
    @SerialName("status")
    val status: String,

    @SerialName("message")
    val message: String
)
