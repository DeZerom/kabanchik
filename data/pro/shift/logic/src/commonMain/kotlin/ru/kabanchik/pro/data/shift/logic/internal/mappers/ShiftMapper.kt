package ru.kabanchik.pro.data.shift.logic.internal.mappers

import ru.kabanchik.pro.data.shift.model.ProApiShift
import ru.kabanchik.pro.data.shift.model.ProApiShiftStatus
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus

internal fun ProApiShift.toDomain(): ProShiftStatus = status.toDomain()

internal fun ProApiShiftStatus.toDomain(): ProShiftStatus = when (this) {
    ProApiShiftStatus.OffShift -> ProShiftStatus.OffShift
    ProApiShiftStatus.OnShift -> ProShiftStatus.OnShift
}
