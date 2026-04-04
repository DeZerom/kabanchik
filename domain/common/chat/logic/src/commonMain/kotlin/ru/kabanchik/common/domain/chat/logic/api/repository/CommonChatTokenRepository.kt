package ru.kabanchik.common.domain.chat.logic.api.repository

interface CommonChatTokenRepository {
    suspend fun getToken(): String?
}