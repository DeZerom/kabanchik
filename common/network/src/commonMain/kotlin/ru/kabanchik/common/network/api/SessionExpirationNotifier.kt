package ru.kabanchik.common.network.api

import kotlinx.coroutines.flow.Flow

/**
 * Сообщает, что сервер отклонил токен пользователя.
 */
interface SessionExpirationNotifier {
    /**
     * Эмитит, когда защищенный REST-запрос получил 401 или 403:
     * токен истек или недействителен, и пользователя нужно отправить на авторизацию.
     */
    val sessionExpired: Flow<Unit>
}
