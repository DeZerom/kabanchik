package ru.kabanchik.client.feature.profile.api

import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler

interface ClientProfileDependencies {
    val profileInteractor: CommonProfileInteractor
    val errorHandler: ErrorHandler

    class Factory(
        override val profileInteractor: CommonProfileInteractor,
        override val errorHandler: ErrorHandler
    ) : ClientProfileDependencies
}
