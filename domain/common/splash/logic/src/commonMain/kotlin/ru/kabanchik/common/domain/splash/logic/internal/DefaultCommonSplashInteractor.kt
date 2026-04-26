package ru.kabanchik.common.domain.splash.logic.internal

import ru.kabanchik.common.domain.splash.logic.api.CommonSplashInteractor
import ru.kabanchik.common.domain.splash.logic.api.repository.CommonSplashTokenRepository

internal class DefaultCommonSplashInteractor(
    private val repository: CommonSplashTokenRepository
) : CommonSplashInteractor {
    override suspend fun isAuthorized(): Boolean {
        return repository.getToken() != null
    }
}