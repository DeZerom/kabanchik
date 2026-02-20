package ru.kabanchik.client.feature.auth.api.flow

import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler

interface AuthFlowDependencies {
    val authInteractor: AuthInteractor
    val errorHandler: ErrorHandler

    class Factory(
        override val authInteractor: AuthInteractor,
        override val errorHandler: ErrorHandler
    ) : AuthFlowDependencies
}