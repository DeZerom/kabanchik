package ru.kabanchik.pro.domain.auth.logic.api.repository

interface ProAuthUserRepository {
    suspend fun setUserLogin(login: String)
}