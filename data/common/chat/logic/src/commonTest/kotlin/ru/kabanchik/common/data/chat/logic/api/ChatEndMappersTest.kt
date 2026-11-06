package ru.kabanchik.common.data.chat.logic.api

import ru.kabanchik.common.chat.model.CommonChatErrorCode
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiErrorMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ChatEndMappersTest {
    @Test
    fun mapsEndChatErrorCodes() {
        val codes = mapOf(
            "INVALID_END_REQUEST" to CommonChatErrorCode.InvalidEndRequest,
            "NOT_SESSION_EXECUTOR" to CommonChatErrorCode.NotSessionExecutor,
            "SESSION_NOT_OPEN" to CommonChatErrorCode.SessionNotOpen,
            "SESSION_END_FAILED" to CommonChatErrorCode.SessionEndFailed,
            "SESSION_CLOSED" to CommonChatErrorCode.SessionClosed,
            "SOMETHING_NEW" to CommonChatErrorCode.Other,
        )

        codes.forEach { (apiCode, code) ->
            assertEquals(code, error(apiCode).toDomain().code, apiCode)
        }
    }

    @Test
    fun keepsCorrelationIdOfError() {
        val result = error("SESSION_CLOSED", correlationId = "message-id").toDomain()

        assertEquals("message-id", result.correlationId)
        assertEquals("session-id", result.sessionId)
    }

    @Test
    fun mapsClosingFieldsOfSummary() {
        val result = summary(closedAt = "2026-11-05T10:00:00Z", closedBy = "executor_1").toDomain()

        assertEquals(CommonSessionStatus.Closed, result.status)
        assertNotNull(result.closedAt)
        assertEquals("executor_1", result.closedBy)
    }

    @Test
    fun keepsMissingClosingFieldsOfOldChats() {
        val result = summary(closedAt = null, closedBy = null).toDomain()

        assertNull(result.closedAt)
        assertNull(result.closedBy)
    }

    private fun error(code: String, correlationId: String? = null) = CommonApiErrorMessage(
        code = code,
        content = "",
        sessionId = "session-id",
        correlationId = correlationId,
    )

    private fun summary(closedAt: String?, closedBy: String?) = CommonApiChatSummary(
        sessionId = "session-id",
        status = CommonApiSessionStatus.Closed,
        participantName = "executor_1",
        lastMessageContent = "Сессия завершена",
        lastMessageTimestamp = "2026-11-05T10:00:00Z",
        lastMessageSender = "SYSTEM",
        closedAt = closedAt,
        closedBy = closedBy,
    )
}
