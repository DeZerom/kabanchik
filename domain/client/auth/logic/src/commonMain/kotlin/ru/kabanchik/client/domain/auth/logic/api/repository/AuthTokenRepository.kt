package ru.kabanchik.client.domain.auth.logic.api.repository

interface AuthTokenRepository {
    suspend fun saveToken(token: String)
}