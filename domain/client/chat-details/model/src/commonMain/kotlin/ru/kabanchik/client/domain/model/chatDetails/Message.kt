package ru.kabanchik.client.domain.model.chatDetails

data class Message(
    val authorLogin: String,
    val text: String
) {
    fun isUserAuthor(userLogin: String): Boolean {
        return authorLogin == userLogin
    }
}