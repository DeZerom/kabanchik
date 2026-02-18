package ru.kabanchik.pro.feature.auth.api

import ru.kabanchik.pro.domain.auth.logic.api.ProAuthInteractor

interface ProAuthDependencies {
    val authInteractor: ProAuthInteractor

    class Factory(
        override val authInteractor: ProAuthInteractor,
    ): ProAuthDependencies
}