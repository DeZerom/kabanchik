package ru.kabanchik.pro.domain.splash.logic.internal

import ru.kabanchik.common.domain.splash.logic.api.CommonSplashInteractor
import ru.kabanchik.pro.domain.splash.logic.api.ProSplashInteractor

internal class DefaultProSplashInteractor(
    commonSplashInteractor: CommonSplashInteractor
) : ProSplashInteractor, CommonSplashInteractor by commonSplashInteractor