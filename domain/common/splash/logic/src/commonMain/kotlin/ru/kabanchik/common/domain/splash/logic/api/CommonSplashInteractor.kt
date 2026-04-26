package ru.kabanchik.common.domain.splash.logic.api

interface CommonSplashInteractor {
    suspend fun isAuthorized(): Boolean
}