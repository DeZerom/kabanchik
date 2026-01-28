package ru.kabanchik.client.feature.auth.api.flow

import ru.kabanchik.client.domain.auth.logic.api.AuthInteractor

interface AuthFlowDependencies {
    val authInteractor: AuthInteractor

    class Factory(
        override val authInteractor: AuthInteractor
    ) : AuthFlowDependencies
}