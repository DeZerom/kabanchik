package ru.kabanchik.pro.data.shift.logic.api

import ru.kabanchik.pro.data.shift.model.ProApiShift

interface ProShiftRestSource {
    suspend fun getShift(): ProApiShift
    suspend fun startShift(): ProApiShift
    suspend fun endShift(): ProApiShift
}
