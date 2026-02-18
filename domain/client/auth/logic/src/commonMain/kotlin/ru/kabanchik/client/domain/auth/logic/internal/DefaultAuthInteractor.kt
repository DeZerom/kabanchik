package ru.kabanchik.client.domain.auth.logic.internal

import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.domain.auth.logic.api.repository.AuthRepository
import ru.kabanchik.client.domain.auth.logic.api.repository.AuthTokenRepository
import ru.kabanchik.client.domain.auth.logic.api.repository.AuthUserRepository

internal class DefaultAuthInteractor(
    private val authRepository: AuthRepository,
    private val tokenRepository: AuthTokenRepository,
    private val userRepository: AuthUserRepository
) : AuthInteractor {
    override suspend fun authorize(login: String, password: String) {
        val result = authRepository.authorize(login, password)
        userRepository.setUserLogin(login)
        tokenRepository.saveToken(result.token)
    }

    override suspend fun register(login: String, password: String) {
        authRepository.register(login, password)
    }
}