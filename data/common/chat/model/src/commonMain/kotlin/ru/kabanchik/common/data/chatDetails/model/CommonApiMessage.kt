package ru.kabanchik.common.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonApiMessage(
    @SerialName("id")
    val id: String,

    @SerialName("sessionId")
    val sessionId: String,

    @SerialName("sender")
    val sender: String,

    @SerialName("content")
    val content: String?,

    @SerialName("type")
    val type: CommonApiMessageType,

    @SerialName("attachments")
    val attachments: List<CommonApiAttachment>,

    @SerialName("timestamp")
    val timestamp: String,
)

@Serializable
enum class CommonApiMessageType {
    @SerialName("TEXT")
    Text,

    @SerialName("SYSTEM")
    System,

    @SerialName("FILE")
    File,
}

@Serializable
data class CommonApiAttachment(
    @SerialName("fileId")
    val fileId: String,

    @SerialName("originalName")
    val originalName: String,

    @SerialName("contentType")
    val contentType: String,

    @SerialName("size")
    val size: Long,

    @SerialName("downloadUrl")
    val downloadUrl: String,
)
