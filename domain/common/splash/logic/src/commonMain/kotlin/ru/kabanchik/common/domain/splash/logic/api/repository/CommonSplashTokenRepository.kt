package ru.kabanchik.common.domain.splash.logic.api.repository

interface CommonSplashTokenRepository {
    suspend fun getToken(): String?
}