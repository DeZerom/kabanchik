package ru.kabanchik.client.data.auth.logic.api.sources

import ru.kabanchik.client.data.auht.model.ApiAuthResult
import ru.kabanchik.client.data.auht.model.ApiCredentials

interface AuthApi {
    suspend fun authorize(credentials: ApiCredentials): ApiAuthResult
    suspend fun register(credentials: ApiCredentials): ApiAuthResult
}