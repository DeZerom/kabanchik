package ru.kabanchik.pro.feature.auth.api

import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.pro.domain.auth.logic.api.ProAuthInteractor

interface ProAuthDependencies {
    val authInteractor: ProAuthInteractor
    val userInteractor: UserInteractor

    class Factory(
        override val authInteractor: ProAuthInteractor,
        override val userInteractor: UserInteractor
    ): ProAuthDependencies
}