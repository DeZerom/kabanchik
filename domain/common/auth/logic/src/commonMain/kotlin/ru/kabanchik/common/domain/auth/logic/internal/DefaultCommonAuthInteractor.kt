package ru.kabanchik.common.domain.auth.logic.internal

import ru.kabanchik.common.domain.auth.logic.api.CommonAuthInteractor
import ru.kabanchik.common.domain.auth.logic.api.repository.CommonAuthTokenRepository
import ru.kabanchik.common.domain.auth.logic.api.repository.CommonAuthUserRepository

internal class DefaultCommonAuthInteractor(
    private val tokenRepository: CommonAuthTokenRepository,
    private val userRepository: CommonAuthUserRepository
) : CommonAuthInteractor {
    override suspend fun saveUser(login: String) {
        userRepository.setUserLogin(login)
    }

    override suspend fun saveToken(token: String) {
        tokenRepository.saveToken(token)
    }

    override suspend fun getToken(): String? {
        return tokenRepository.getToken()
    }
}