package ru.kabanchik.pro.data.chat.logic.internal.mappers

import ru.kabanchik.pro.data.chatDetails.model.ProApiShift
import ru.kabanchik.pro.data.chatDetails.model.ProApiShiftStatus
import ru.kabanchik.pro.domain.chatDetails.model.ProShiftStatus

fun ProApiShift.toDomain(): ProShiftStatus = status.toDomain()

fun ProApiShiftStatus.toDomain(): ProShiftStatus = when (this) {
    ProApiShiftStatus.OffShift -> ProShiftStatus.OffShift
    ProApiShiftStatus.OnShift -> ProShiftStatus.OnShift
}
