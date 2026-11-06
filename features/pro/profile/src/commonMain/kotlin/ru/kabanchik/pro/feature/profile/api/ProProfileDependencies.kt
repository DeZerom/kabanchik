package ru.kabanchik.pro.feature.profile.api

import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor

interface ProProfileDependencies {
    val profileInteractor: CommonProfileInteractor
    val shiftInteractor: ProShiftInteractor
    val errorHandler: ErrorHandler

    class Factory(
        override val profileInteractor: CommonProfileInteractor,
        override val shiftInteractor: ProShiftInteractor,
        override val errorHandler: ErrorHandler
    ) : ProProfileDependencies
}
