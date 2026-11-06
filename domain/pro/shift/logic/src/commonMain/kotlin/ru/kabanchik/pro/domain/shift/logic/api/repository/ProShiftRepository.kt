package ru.kabanchik.pro.domain.shift.logic.api.repository

import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus

interface ProShiftRepository {
    val shiftStatus: StateFlow<ProShiftStatus?>

    fun setShiftStatus(status: ProShiftStatus?)
    suspend fun getShift(): ProShiftStatus
    suspend fun startShift(): ProShiftStatus
    suspend fun endShift(): ProShiftStatus
}
