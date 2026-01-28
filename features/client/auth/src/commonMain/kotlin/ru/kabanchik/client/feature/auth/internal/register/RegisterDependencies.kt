package ru.kabanchik.client.feature.auth.internal.register

import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor
import ru.kabanchik.client.feature.auth.api.flow.AuthFlowDependencies

internal interface RegisterDependencies {
    val authInteractor: AuthInteractor

    class Factory(
        dependencies: AuthFlowDependencies
    ) : RegisterDependencies, AuthFlowDependencies by dependencies
}