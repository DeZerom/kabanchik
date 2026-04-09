package ru.kabanchik.common.domain.auth.logic.api.repository

interface CommonAuthUserRepository {
    suspend fun setUserLogin(login: String)
}