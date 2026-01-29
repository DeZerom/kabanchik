package ru.kabanchik.client.data.auth.logic.internal

import ru.kabanchik.client.data.auht.model.ApiCredentials
import ru.kabanchik.client.data.auth.logic.api.sources.AuthApi
import ru.kabanchik.client.data.auth.logic.internal.mapper.toDomain
import ru.kabanchik.client.domain.auth.logic.api.repository.AuthRepository
import ru.kabanchik.client.domain.auth.model.AuthResult

internal class DefaultAuthRepository(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun authorize(login: String, password: String): AuthResult {
        return authApi.authorize(credentials = ApiCredentials(login = login, password = password)).toDomain()
    }

    override suspend fun register(login: String, password: String): AuthResult {
        return authApi.register(credentials = ApiCredentials(login = login, password = password)).toDomain()
    }
}