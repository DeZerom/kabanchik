package ru.kabanchik.common.domain.user.logic.internal

import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.domain.user.logic.api.repository.UserRepository

internal class DefaultUserInteractor(
    private val userRepository: UserRepository
) : UserInteractor {
    override suspend fun getUserLogin(): String? {
        return userRepository.getUserLogin()
    }

    override suspend fun setUserLogin(login: String) {
        userRepository.setUserLogin(login)
    }
}