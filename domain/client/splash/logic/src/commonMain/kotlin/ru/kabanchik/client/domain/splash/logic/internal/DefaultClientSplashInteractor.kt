package ru.kabanchik.client.domain.splash.logic.internal

import ru.kabanchik.client.domain.splash.logic.api.ClientSplashInteractor
import ru.kabanchik.common.domain.splash.logic.api.CommonSplashInteractor

class DefaultClientSplashInteractor(
    commonInteractor: CommonSplashInteractor
): ClientSplashInteractor, CommonSplashInteractor by commonInteractor