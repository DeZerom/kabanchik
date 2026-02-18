package ru.kabanchik.client.domain.auth.logic.api.repository

interface AuthUserRepository {
    suspend fun setUserLogin(login: String)
}