package ru.kabanchik.pro.domain.auth.logic.internal

import ru.kabanchik.pro.domain.auth.logic.api.ProAuthInteractor
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthRepository
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthTokenRepository
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthUserRepository

internal class DefaultProAuthInteractor(
    private val authRepository: ProAuthRepository,
    private val tokenRepository: ProAuthTokenRepository,
    private val userRepository: ProAuthUserRepository
) : ProAuthInteractor {
    override suspend fun authorize(login: String, password: String) {
        val result = authRepository.authorize(login, password)
        userRepository.setUserLogin(login)
        tokenRepository.saveToken(result.token)
    }
}