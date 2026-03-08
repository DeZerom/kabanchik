package ru.kabanchik.pro.domain.chatDetails.logic.api.repository

interface ProChatDetailsTokenRepository {
    suspend fun getToken(): String?
}