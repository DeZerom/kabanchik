package ru.kabanchik.pro.domain.shift.logic.internal

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.pro.domain.shift.logic.api.ProShiftInteractor
import ru.kabanchik.pro.domain.shift.logic.api.repository.ProShiftRepository
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus

internal class DefaultProShiftInteractor(
    private val shiftRepository: ProShiftRepository,
    private val profileInteractor: CommonProfileInteractor
) : ProShiftInteractor {
    override val shiftStatus: StateFlow<ProShiftStatus?> = shiftRepository.shiftStatus

    override suspend fun refresh(): ProShiftStatus {
        return shiftRepository.getShift().also(shiftRepository::setShiftStatus)
    }

    override suspend fun startShift(): ProShiftStatus {
        return shiftRepository.startShift().also(shiftRepository::setShiftStatus)
    }

    override suspend fun endShift(): ProShiftStatus {
        return endShiftOnServer().also(shiftRepository::setShiftStatus)
    }

    override suspend fun logout() {
        // Статус не публикуем до конца: его смена закрывает экраны, с которых вызван выход
        if (shiftRepository.shiftStatus.value == ProShiftStatus.OnShift) {
            endShiftOnServer()
        }
        profileInteractor.logout()
        shiftRepository.setShiftStatus(null)
    }

    override fun markOffShift() {
        shiftRepository.setShiftStatus(ProShiftStatus.OffShift)
    }

    override fun clear() {
        shiftRepository.setShiftStatus(null)
    }

    private suspend fun endShiftOnServer(): ProShiftStatus {
        return try {
            shiftRepository.endShift()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            // При ошибке часть изменений могла сохраниться: сверяемся с сервером
            val actualStatus = try {
                shiftRepository.getShift()
            } catch (refreshError: CancellationException) {
                throw refreshError
            } catch (_: Throwable) {
                throw e
            }
            if (actualStatus == ProShiftStatus.OffShift) actualStatus else throw e
        }
    }
}
