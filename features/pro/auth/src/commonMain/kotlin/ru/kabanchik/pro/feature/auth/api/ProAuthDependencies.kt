package ru.kabanchik.pro.feature.auth.api

import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.pro.domain.auth.logic.api.ProAuthInteractor

interface ProAuthDependencies {
    val authInteractor: ProAuthInteractor
    val errorHandler: ErrorHandler

    class Factory(
        override val authInteractor: ProAuthInteractor,
        override val errorHandler: ErrorHandler
    ): ProAuthDependencies
}