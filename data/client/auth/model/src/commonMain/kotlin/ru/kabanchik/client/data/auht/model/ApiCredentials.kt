package ru.kabanchik.client.data.auht.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiCredentials(
    @SerialName("username")
    val login: String,

    @SerialName("password")
    val password: String,
)
