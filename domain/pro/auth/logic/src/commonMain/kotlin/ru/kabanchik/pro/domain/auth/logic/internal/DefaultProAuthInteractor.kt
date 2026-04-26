package ru.kabanchik.pro.domain.auth.logic.internal

import ru.kabanchik.common.domain.auth.logic.api.CommonAuthInteractor
import ru.kabanchik.pro.domain.auth.logic.api.ProAuthInteractor
import ru.kabanchik.pro.domain.auth.logic.api.repository.ProAuthRepository

internal class DefaultProAuthInteractor(
    commonInteractor: CommonAuthInteractor,
    private val authRepository: ProAuthRepository,
) : ProAuthInteractor, CommonAuthInteractor by commonInteractor {
    override suspend fun authorize(login: String, password: String) {
        val result = authRepository.authorize(login, password)
        saveUser(login)
        saveToken(result.token)
    }
}