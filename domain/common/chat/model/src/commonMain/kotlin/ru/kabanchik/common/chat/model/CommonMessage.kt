package ru.kabanchik.common.chat.model

import kotlinx.datetime.LocalDateTime

data class CommonMessage(
    val id: String,
    val sessionId: String,
    val authorLogin: String,
    val text: String,
    val type: CommonMessageType,
    val attachments: List<CommonAttachment> = emptyList(),
    val time: LocalDateTime
) {
    fun isUserAuthor(userLogin: String): Boolean {
        return authorLogin == userLogin
    }
}

data class CommonAttachment(
    val fileId: String,
    val originalName: String,
    val contentType: String,
    val size: Long,
    val downloadUrl: String,
)
