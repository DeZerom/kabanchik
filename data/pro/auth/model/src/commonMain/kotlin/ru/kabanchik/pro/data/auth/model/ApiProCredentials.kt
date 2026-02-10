package ru.kabanchik.pro.data.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiProCredentials(
    @SerialName("username")
    val login: String,

    @SerialName("password")
    val password: String,
)
