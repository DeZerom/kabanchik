package ru.kabanchik.client.domain.auth.logic.internal

import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.domain.auth.logic.api.repository.AuthRepository
import ru.kabanchik.client.domain.auth.logic.api.repository.AuthTokenRepository

private const val OkStatus = "ok"

internal class DefaultAuthInteractor(
    private val authRepository: AuthRepository,
    private val tokenRepository: AuthTokenRepository
) : AuthInteractor {
    override suspend fun authorize(login: String, password: String): String? {
        val result = authRepository.authorize(login, password)
        if (result.status != OkStatus) return result.message

        tokenRepository.saveToken(result.message)

        return null
    }

    override suspend fun register(login: String, password: String): String? {
        val result = authRepository.register(login, password)

        return if (result.status == OkStatus) {
            null
        } else {
            result.message
        }
    }
}