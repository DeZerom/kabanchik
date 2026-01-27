package ru.kabanchik.client.domain.auth.logic.api

interface AuthInteractor {
    suspend fun authorize(login: String, password: String): String?
    suspend fun register(login: String, password: String): String?
}