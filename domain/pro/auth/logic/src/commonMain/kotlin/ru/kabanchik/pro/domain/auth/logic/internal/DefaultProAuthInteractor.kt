package ru.kabanchik.pro.domain.auth.logic.internal

import ru.kabanchik.pro.domain.auth.logic.api.ProAuthInteractor
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthRepository
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthTokenRepository

internal class DefaultProAuthInteractor(
    private val authRepository: ProAuthRepository,
    private val tokenRepository: ProAuthTokenRepository
) : ProAuthInteractor {
    override suspend fun authorize(login: String, password: String) {
        val result = authRepository.authorize(login, password)
        tokenRepository.saveToken(result.token)
    }
}