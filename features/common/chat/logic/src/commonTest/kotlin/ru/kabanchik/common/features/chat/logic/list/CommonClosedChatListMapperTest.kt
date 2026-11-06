package ru.kabanchik.common.features.chat.logic.list

import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_resolution_closed
import kotlinx.datetime.LocalDateTime
import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.tools.textResource.TextResource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CommonClosedChatListMapperTest {
    @Test
    fun marksClosedChatWithResolution() {
        val result = summary(status = CommonSessionStatus.Closed).toUiChatItem()

        assertEquals(Res.string.chat_resolution_closed, (result.resolution as TextResource.Id).value)
    }

    @Test
    fun activeChatHasNoResolution() {
        val result = summary(status = CommonSessionStatus.Open).toUiChatItem()

        assertNull(result.resolution)
    }

    @Test
    fun closedChatShowsClosingTime() {
        val result = summary(
            status = CommonSessionStatus.Closed,
            lastMessageTime = LocalDateTime(2026, 1, 1, 9, 0),
            closedAt = LocalDateTime(2026, 1, 2, 18, 45),
        ).toUiChatItem()

        assertEquals(listOf("02.01.2026", "18:45"), (result.lastUpdate as TextResource.Formatted).args)
    }

    @Test
    fun oldClosedChatFallsBackToLastMessageTime() {
        val result = summary(
            status = CommonSessionStatus.Closed,
            lastMessageTime = LocalDateTime(2026, 1, 1, 9, 0),
            closedAt = null,
        ).toUiChatItem()

        assertEquals(listOf("01.01.2026", "09:00"), (result.lastUpdate as TextResource.Formatted).args)
    }

    private fun summary(
        status: CommonSessionStatus,
        lastMessageTime: LocalDateTime? = null,
        closedAt: LocalDateTime? = null,
    ): CommonChatSummary = CommonChatSummary(
        sessionId = "session-id",
        status = status,
        participantName = "executor_1",
        firstMessageContent = "Бронь ресторана",
        lastMessageContent = "Сессия завершена",
        lastMessageTimestamp = lastMessageTime,
        lastMessageSender = "SYSTEM",
        closedAt = closedAt,
    )
}
