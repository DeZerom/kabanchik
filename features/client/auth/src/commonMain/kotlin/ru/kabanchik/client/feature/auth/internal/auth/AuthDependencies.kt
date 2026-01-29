package ru.kabanchik.client.feature.auth.internal.auth

import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowDependencies

internal interface AuthDependencies {
    val authInteractor: AuthInteractor

    class Factory(
        dependencies: AuthFlowDependencies
    ) : AuthDependencies, AuthFlowDependencies by dependencies
}