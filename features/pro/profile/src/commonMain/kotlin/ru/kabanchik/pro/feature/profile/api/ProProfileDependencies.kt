package ru.kabanchik.pro.feature.profile.api

import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler

interface ProProfileDependencies {
    val profileInteractor: CommonProfileInteractor
    val errorHandler: ErrorHandler

    class Factory(
        override val profileInteractor: CommonProfileInteractor,
        override val errorHandler: ErrorHandler
    ) : ProProfileDependencies
}
