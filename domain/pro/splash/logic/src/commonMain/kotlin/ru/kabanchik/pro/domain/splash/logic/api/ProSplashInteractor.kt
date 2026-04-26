package ru.kabanchik.pro.domain.splash.logic.api

interface ProSplashInteractor {
    suspend fun isAuthorized(): Boolean
}