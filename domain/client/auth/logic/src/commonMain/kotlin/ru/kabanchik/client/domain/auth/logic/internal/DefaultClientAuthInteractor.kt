package ru.kabanchik.client.domain.auth.logic.internal

import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.domain.auth.logic.api.repository.AuthRepository
import ru.kabanchik.common.domain.auth.logic.api.CommonAuthInteractor

internal class DefaultClientAuthInteractor(
    commonInteractor: CommonAuthInteractor,
    private val authRepository: AuthRepository,
) : AuthInteractor, CommonAuthInteractor by commonInteractor {
    override suspend fun authorize(login: String, password: String) {
        val result = authRepository.authorize(login, password)
        saveUser(login)
        saveToken(result.token)
    }

    override suspend fun register(login: String, password: String) {
        authRepository.register(login, password)
    }
}