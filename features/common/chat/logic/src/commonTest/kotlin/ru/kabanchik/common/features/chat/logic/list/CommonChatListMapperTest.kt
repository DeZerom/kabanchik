package ru.kabanchik.common.features.chat.logic.list

import kotlinx.datetime.LocalDateTime
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.tools.textResource.TextResource
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CommonChatListMapperTest {
    @Test
    fun ownMessageDoesNotMarkChatAsUnread() {
        val result = emptyList<CommonUiChatItem>().updateWithMessage(
            message = message(authorLogin = CurrentUserLogin),
            currentUserLogin = CurrentUserLogin,
        )

        assertFalse(result.single().hasUnread)
    }

    @Test
    fun ownMessageDoesNotClearExistingUnreadState() {
        val chat = chatItem(hasUnread = true)

        val result = listOf(chat).updateWithMessage(
            message = message(authorLogin = CurrentUserLogin),
            currentUserLogin = CurrentUserLogin,
        )

        assertTrue(result.single().hasUnread)
    }

    @Test
    fun incomingMessageMarksChatAsUnread() {
        val result = listOf(chatItem(hasUnread = false)).updateWithMessage(
            message = message(authorLogin = "operator"),
            currentUserLogin = CurrentUserLogin,
        )

        assertTrue(result.single().hasUnread)
    }

    private fun message(authorLogin: String): CommonMessage = CommonMessage(
        id = "message-id",
        sessionId = SessionId,
        authorLogin = authorLogin,
        text = "Message",
        type = CommonMessageType.Text,
        time = LocalDateTime(2026, 8, 5, 12, 0),
    )

    private fun chatItem(hasUnread: Boolean): CommonUiChatItem = CommonUiChatItem(
        id = SessionId,
        title = "Chat",
        lastUpdate = TextResource.Raw("12:00"),
        lastMessage = "Previous message",
        hasUnread = hasUnread,
        otherPersonName = "operator",
    )

    private companion object {
        const val CurrentUserLogin = "client"
        const val SessionId = "session-id"
    }
}
