package ru.kabanchik.client.domain.token.logic.api

interface TokenRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
}