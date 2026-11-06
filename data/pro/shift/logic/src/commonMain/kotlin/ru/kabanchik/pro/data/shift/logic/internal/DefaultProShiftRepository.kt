package ru.kabanchik.pro.data.shift.logic.internal

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kabanchik.pro.data.shift.logic.api.ProShiftRestSource
import ru.kabanchik.pro.data.shift.logic.internal.mappers.toDomain
import ru.kabanchik.pro.domain.shift.logic.api.repository.ProShiftRepository
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus

internal class DefaultProShiftRepository(
    private val restSource: ProShiftRestSource
) : ProShiftRepository {
    private val _shiftStatus = MutableStateFlow<ProShiftStatus?>(null)
    override val shiftStatus: StateFlow<ProShiftStatus?> = _shiftStatus.asStateFlow()

    override fun setShiftStatus(status: ProShiftStatus?) {
        _shiftStatus.value = status
    }

    override suspend fun getShift(): ProShiftStatus {
        return restSource.getShift().toDomain()
    }

    override suspend fun startShift(): ProShiftStatus {
        return restSource.startShift().toDomain()
    }

    override suspend fun endShift(): ProShiftStatus {
        return restSource.endShift().toDomain()
    }
}
