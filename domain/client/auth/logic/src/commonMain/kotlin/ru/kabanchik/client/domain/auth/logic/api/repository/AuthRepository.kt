package ru.kabanchik.client.domain.auth.logic.api.repository

import ru.kabanchik.client.domain.auth.model.AuthResult

interface AuthRepository {
    suspend fun authorize(login: String, password: String): AuthResult
    suspend fun register(login: String, password: String): Int
}