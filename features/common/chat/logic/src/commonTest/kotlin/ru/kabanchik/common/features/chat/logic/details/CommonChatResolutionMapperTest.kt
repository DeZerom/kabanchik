package ru.kabanchik.common.features.chat.logic.details

import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_resolution_closed
import kotlinx.datetime.LocalDateTime
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.tools.textResource.TextResource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CommonChatResolutionMapperTest {
    @Test
    fun mapsSessionEndToClosedResolutionCard() {
        val result = CommonChatMessage.SessionEnd(systemMessage()).toState(userLogin = "client")

        assertIs<CommonUiMessage.Resolution>(result)
        assertEquals("system-id", result.id)
        assertEquals("18:45", result.time)
        assertEquals(Res.string.chat_resolution_closed, (result.status as TextResource.Id).value)
    }

    @Test
    fun replacesRepeatedResolutionCardById() {
        val card = CommonChatMessage.SessionEnd(systemMessage()).toState(userLogin = "client")
        val messages = listOf(CommonUiMessage.Message(id = "message-id"), card)

        val result = messages.upsert(card)

        assertEquals(listOf("message-id", "system-id"), result.map { it.id })
    }

    private fun systemMessage(): CommonMessage = CommonMessage(
        id = "system-id",
        sessionId = "session-id",
        authorLogin = "SYSTEM",
        text = "Сессия завершена",
        type = CommonMessageType.System,
        time = LocalDateTime(2026, 11, 5, 18, 45),
    )
}
