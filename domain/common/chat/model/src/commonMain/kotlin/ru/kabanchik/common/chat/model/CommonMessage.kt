package ru.kabanchik.common.chat.model

import kotlinx.datetime.LocalDateTime

data class CommonMessage(
    val id: String,
    val sessionId: String,
    val authorLogin: String,
    val text: String,
    val type: CommonMessageType,
    val time: LocalDateTime
) {
    fun isUserAuthor(userLogin: String): Boolean {
        return authorLogin == userLogin
    }
}
