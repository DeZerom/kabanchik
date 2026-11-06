package ru.kabanchik.common.domain.profile.logic.internal

import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.common.domain.profile.logic.api.repository.CommonProfileUserRepository

internal class DefaultCommonProfileInteractor(
    private val userRepository: CommonProfileUserRepository
) : CommonProfileInteractor {
    override suspend fun getUserLogin(): String? {
        return userRepository.getUserLogin()
    }

    override suspend fun logout() {
        // Сначала рвем соединение, чтобы сокет не переподключился со старым токеном
        userRepository.disconnect()
        userRepository.clearLocalData()
    }
}
