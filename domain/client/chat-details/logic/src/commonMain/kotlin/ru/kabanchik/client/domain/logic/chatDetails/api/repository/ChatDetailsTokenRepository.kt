package ru.kabanchik.client.domain.logic.chatDetails.api.repository

interface ChatDetailsTokenRepository {
    suspend fun getToken(): String?
}