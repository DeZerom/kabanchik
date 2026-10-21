package ru.kabanchik.common.domain.profile.logic.api.repository

interface CommonProfileUserRepository {
    suspend fun getUserLogin(): String?
    suspend fun disconnect()
    suspend fun clearLocalData()
}
