package ru.kabanchik.common.network.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.kabanchik.common.network.api.SessionExpirationNotifier

private const val AuthPathPrefix = "/auth/api/"
private val SessionExpiredStatusCodes = setOf(401, 403)

internal class DefaultSessionExpirationNotifier : SessionExpirationNotifier {
    private val events = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val sessionExpired: Flow<Unit> = events.asSharedFlow()

    fun onResponseError(statusCode: Int, path: String) {
        if (isSessionExpiredResponse(statusCode, path)) {
            events.tryEmit(Unit)
        }
    }
}

/**
 * chat-service отвечает 403 (а не 401) на отсутствующий, истекший или невалидный токен.
 * Запросы auth-service исключены: там 401 означает неверный логин или пароль.
 */
internal fun isSessionExpiredResponse(statusCode: Int, path: String): Boolean {
    return statusCode in SessionExpiredStatusCodes && !path.startsWith(AuthPathPrefix)
}
