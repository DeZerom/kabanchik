package ru.kabanchik.pro.feature.shift.api

import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor

interface ProShiftGateDependencies {
    val shiftInteractor: ProShiftInteractor
    val errorHandler: ErrorHandler

    class Factory(
        override val shiftInteractor: ProShiftInteractor,
        override val errorHandler: ErrorHandler
    ) : ProShiftGateDependencies
}
