package ru.kabanchik.common.domain.user.logic.api.repository

interface UserRepository {
    suspend fun setUserLogin(login: String)
    suspend fun getUserLogin(): String?
}