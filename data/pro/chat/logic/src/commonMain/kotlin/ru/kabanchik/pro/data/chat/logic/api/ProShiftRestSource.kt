package ru.kabanchik.pro.data.chat.logic.api

import ru.kabanchik.pro.data.chatDetails.model.ProApiShift

interface ProShiftRestSource {
    suspend fun getShift(): ProApiShift
    suspend fun startShift(): ProApiShift
    suspend fun endShift(): ProApiShift
}
