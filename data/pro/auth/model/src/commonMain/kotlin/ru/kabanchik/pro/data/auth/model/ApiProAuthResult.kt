package ru.kabanchik.pro.data.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiProAuthResult(
    @SerialName("token")
    val token: String
)
