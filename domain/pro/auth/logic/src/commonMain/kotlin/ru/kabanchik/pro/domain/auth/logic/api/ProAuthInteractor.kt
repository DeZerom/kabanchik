package ru.kabanchik.pro.domain.auth.logic.api

interface ProAuthInteractor {
    suspend fun authorize(login: String, password: String): String?
}