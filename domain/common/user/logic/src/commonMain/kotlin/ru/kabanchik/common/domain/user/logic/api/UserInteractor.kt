package ru.kabanchik.common.domain.user.logic.api

interface UserInteractor {
    suspend fun getUserLogin(): String?
    suspend fun setUserLogin(login: String)
}