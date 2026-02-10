package ru.kabanchik.pro.domain.auth.logic.api.repository

interface ProAuthTokenRepository {
    suspend fun saveToken(token: String)
}