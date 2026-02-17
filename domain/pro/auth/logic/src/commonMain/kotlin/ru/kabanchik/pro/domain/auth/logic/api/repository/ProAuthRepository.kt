package ru.kabanchik.pro.domain.auth.logic.api.repository

import ru.kabanchik.pro.domain.auth.model.ProAuthResult

interface ProAuthRepository {
    suspend fun authorize(login: String, password: String): ProAuthResult
}