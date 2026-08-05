package ru.kabanchik.common.features.chat.logic.details

import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import kotlin.test.Test
import kotlin.test.assertEquals

class CommonChatMessageMapperTest {
    @Test
    fun backendMessageReplacesOptimisticMessageWithSameId() {
        val optimisticMessage = message(text = "Optimistic")
        val confirmedMessage = message(text = "Confirmed")

        val result = listOf(optimisticMessage).upsert(confirmedMessage)

        assertEquals(listOf(confirmedMessage), result)
    }

    @Test
    fun dateIsNotDuplicatedWhenBackendConfirmsLocalMessage() {
        val date = CommonUiMessage.Date(id = "date-1", date = "5 августа")
        val backendDate = CommonUiMessage.Date(id = "date-2", date = "5 августа")

        val result = listOf(date).upsert(backendDate)

        assertEquals(listOf(date), result)
    }

    private fun message(text: String): CommonUiMessage.Message = CommonUiMessage.Message(
        id = "message-id",
        authorLogin = "client",
        isUserAuthor = true,
        time = "12:00",
        text = text,
    )
}
