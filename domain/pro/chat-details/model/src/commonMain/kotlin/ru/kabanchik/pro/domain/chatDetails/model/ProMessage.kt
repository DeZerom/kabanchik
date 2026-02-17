package ru.kabanchik.pro.domain.chatDetails.model

data class ProMessage(
    val authorLogin: String,
    val text: String
) {
    fun isUserAuthor(userLogin: String): Boolean {
        return authorLogin == userLogin
    }
}