package ru.kabanchik.common.domain.auth.logic.api

interface CommonAuthInteractor {
    suspend fun saveUser(login: String)
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
}