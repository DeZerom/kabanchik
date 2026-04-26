package ru.kabanchik.common.domain.auth.logic.api.repository

interface CommonAuthTokenRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
}