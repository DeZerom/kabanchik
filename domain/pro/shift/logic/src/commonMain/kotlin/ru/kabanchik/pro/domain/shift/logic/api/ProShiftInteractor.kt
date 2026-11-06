package ru.kabanchik.pro.domain.shift.logic.api

import kotlinx.coroutines.flow.StateFlow
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus

interface ProShiftInteractor {
    /**
     * Последнее известное состояние смены, `null` - еще не запрашивали.
     */
    val shiftStatus: StateFlow<ProShiftStatus?>

    suspend fun refresh(): ProShiftStatus
    suspend fun startShift(): ProShiftStatus

    /**
     * Завершает смену. Если запрос упал, но сервер уже считает оператора вне смены,
     * завершение считается успешным: оставшиеся чаты backend освободит сам.
     */
    suspend fun endShift(): ProShiftStatus

    /**
     * Завершает смену, если оператор на ней, и выходит из аккаунта.
     * Если смену завершить не удалось, из аккаунта не выходит.
     */
    suspend fun logout()

    /**
     * Сервер отклонил команду, потому что оператор вне смены.
     */
    fun markOffShift()

    /**
     * Забывает состояние смены, например когда сессия истекла.
     */
    fun clear()
}
