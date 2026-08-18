package ru.kabanchik.common.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonApiChatFile(
    @SerialName("fileId")
    val fileId: String,

    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("uploader")
    val uploader: String,

    @SerialName("originalName")
    val originalName: String,

    @SerialName("contentType")
    val contentType: String,

    @SerialName("size")
    val size: Long,

    @SerialName("downloadUrl")
    val downloadUrl: String,

    @SerialName("uploadedAt")
    val uploadedAt: String,
)
