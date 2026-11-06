package ru.kabanchik.common.network.internal.api.chat

import kotlinx.serialization.json.Json
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatDetails
import ru.kabanchik.common.data.chatDetails.model.CommonApiChatSummary
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ChatEndModelsSerializationTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun readsClosedChatSummary() {
        val result = json.decodeFromString<List<CommonApiChatSummary>>(
            """
            [{
              "sessionId": "550e8400",
              "status": "CLOSED",
              "participantName": "executor_1",
              "lastMessageContent": "Сессия завершена",
              "lastMessageTimestamp": "2026-11-05T10:00:00Z",
              "lastMessageSender": "SYSTEM",
              "closedAt": "2026-11-05T10:00:00Z",
              "closedBy": "executor_1"
            }]
            """
        ).single()

        assertEquals(CommonApiSessionStatus.Closed, result.status)
        assertEquals("2026-11-05T10:00:00Z", result.closedAt)
        assertEquals("executor_1", result.closedBy)
    }

    @Test
    fun readsSummaryWithoutClosingFields() {
        val result = json.decodeFromString<CommonApiChatSummary>(
            """
            {
              "sessionId": "550e8400",
              "status": "OPEN",
              "participantName": null,
              "lastMessageContent": null,
              "lastMessageTimestamp": null,
              "lastMessageSender": null
            }
            """
        )

        assertNull(result.closedAt)
        assertNull(result.closedBy)
    }

    @Test
    fun readsChatDetails() {
        val result = json.decodeFromString<CommonApiChatDetails>(
            """
            {
              "sessionId": "550e8400",
              "clientRequestId": "f149b344",
              "status": "CLOSED",
              "participant": {"username": "executor_1", "displayName": "executor_1", "role": "EXECUTOR"},
              "createdAt": "2026-11-05T09:00:00Z",
              "closedAt": "2026-11-05T10:00:00Z",
              "closedBy": "executor_1"
            }
            """
        )

        assertEquals(CommonApiSessionStatus.Closed, result.status)
        assertEquals("executor_1", result.participant?.login)
        assertEquals("executor_1", result.closedBy)
    }

    @Test
    fun readsChatDetailsWithoutParticipant() {
        val result = json.decodeFromString<CommonApiChatDetails>(
            """
            {
              "sessionId": "550e8400",
              "clientRequestId": "f149b344",
              "status": "WAITING",
              "participant": null,
              "createdAt": "2026-11-05T09:00:00Z",
              "closedAt": null,
              "closedBy": null
            }
            """
        )

        assertEquals(CommonApiSessionStatus.Waiting, result.status)
        assertNull(result.participant)
        assertNull(result.closedAt)
    }
}
