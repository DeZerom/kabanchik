package ru.kabanchik.client.feature.auth.internal.auth

import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowDependencies
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler

internal interface AuthDependencies {
    val authInteractor: AuthInteractor
    val errorHandler: ErrorHandler

    class Factory(
        dependencies: AuthFlowDependencies
    ) : AuthDependencies, AuthFlowDependencies by dependencies
}