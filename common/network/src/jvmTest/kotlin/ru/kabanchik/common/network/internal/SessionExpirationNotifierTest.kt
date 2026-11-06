package ru.kabanchik.common.network.internal

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SessionExpirationNotifierTest {
    @Test
    fun forbiddenFromChatServiceExpiresSession() {
        assertTrue(isSessionExpiredResponse(statusCode = 403, path = "/chat/api/executor/shift"))
    }

    @Test
    fun unauthorizedFromChatServiceExpiresSession() {
        assertTrue(isSessionExpiredResponse(statusCode = 401, path = "/chat/api/chats"))
    }

    @Test
    fun authServiceErrorsDoNotExpireSession() {
        assertFalse(isSessionExpiredResponse(statusCode = 401, path = "/auth/api/login-executor"))
        assertFalse(isSessionExpiredResponse(statusCode = 403, path = "/auth/api/register-executor"))
    }

    @Test
    fun otherErrorsDoNotExpireSession() {
        assertFalse(isSessionExpiredResponse(statusCode = 404, path = "/chat/api/chats"))
        assertFalse(isSessionExpiredResponse(statusCode = 500, path = "/chat/api/executor/shift/end"))
    }

    @Test
    fun emitsEventOnlyForExpiredSession() {
        runBlocking {
            val notifier = DefaultSessionExpirationNotifier()

            val ignored = async(start = CoroutineStart.UNDISPATCHED) { notifier.sessionExpired.first() }
            notifier.onResponseError(statusCode = 500, path = "/chat/api/chats")
            assertNull(withTimeoutOrNull(100) { ignored.await() })
            ignored.cancel()

            val event = async(start = CoroutineStart.UNDISPATCHED) { notifier.sessionExpired.first() }
            notifier.onResponseError(statusCode = 403, path = "/chat/api/chats")
            assertEquals(Unit, withTimeoutOrNull(1000) { event.await() })
        }
    }
}
