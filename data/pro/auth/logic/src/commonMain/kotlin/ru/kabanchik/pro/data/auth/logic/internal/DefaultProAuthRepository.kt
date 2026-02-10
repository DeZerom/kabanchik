package ru.kabanchik.pro.data.auth.logic.internal

import ru.kabanchik.pro.data.auth.logic.api.sources.ProAuthApi
import ru.kabanchik.pro.data.auth.logic.internal.mapper.toDomain
import ru.kabanchik.pro.data.auth.model.ApiProCredentials
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthRepository
import ru.kabanchik.pro.domain.auth.model.AuthResult

class DefaultProAuthRepository(
    private val authApi: ProAuthApi
) : ProAuthRepository {
    override suspend fun authorize(login: String, password: String): AuthResult {
        return authApi.authorize(
            credentials = ApiProCredentials(
                login = login,
                password = password
            )
        ).toDomain()
    }
}