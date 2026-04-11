package ru.kabanchik.client.domain.splash.logic.api

interface ClientSplashInteractor {
    suspend fun isAuthorized(): Boolean
}