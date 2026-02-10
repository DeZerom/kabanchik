package ru.kabanchik.pro.domain.auth.logic.internal

import ru.kabanchik.pro.domain.auth.logic.api.ProAuthInteractor
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthRepository
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthTokenRepository

private const val OkStatus = "ok"

internal class DefaultProAuthInteractor(
    private val authRepository: ProAuthRepository,
    private val tokenRepository: ProAuthTokenRepository
) : ProAuthInteractor {
    override suspend fun authorize(login: String, password: String): String? {
        val result = authRepository.authorize(login, password)
        if (result.status != OkStatus) return result.message

        tokenRepository.saveToken(result.message)

        return null
    }
}