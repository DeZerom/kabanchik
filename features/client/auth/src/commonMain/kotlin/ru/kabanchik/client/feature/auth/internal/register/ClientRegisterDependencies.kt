package ru.kabanchik.client.feature.auth.internal.register

import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowDependencies
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler

internal interface ClientRegisterDependencies {
    val authInteractor: AuthInteractor
    val errorHandler: ErrorHandler

    class Factory(
        dependencies: AuthFlowDependencies
    ) : ClientRegisterDependencies, AuthFlowDependencies by dependencies
}