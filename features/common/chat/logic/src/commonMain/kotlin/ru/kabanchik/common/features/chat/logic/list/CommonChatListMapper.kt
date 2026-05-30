package ru.kabanchik.common.features.chat.logic.list

import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.tools.extensions.toHoursMinutes

fun CommonChatSummary.toUiChatItem(): CommonUiChatItem {
    return CommonUiChatItem(
        id = sessionId,
        title = participantName,
        lastUpdate = lastMessageTimestamp.toHoursMinutes(),
        lastMessage = lastMessageContent,
        hasUnread = false,
        otherPersonName = participantName
    )
}

fun List<CommonUiChatItem>.updateWithMessage(message: CommonMessage): List<CommonUiChatItem> {
    val existingItem = firstOrNull { it.id == message.sessionId }
    val updatedItem = existingItem?.copy(
        lastUpdate = message.time.toHoursMinutes(),
        lastMessage = message.text,
        hasUnread = true
    ) ?: message.toUiChatItem()

    return listOf(updatedItem) + filterNot { it.id == message.sessionId }
}

private fun CommonMessage.toUiChatItem(): CommonUiChatItem {
    return CommonUiChatItem(
        id = sessionId,
        title = authorLogin,
        lastUpdate = time.toHoursMinutes(),
        lastMessage = text,
        hasUnread = true,
        otherPersonName = authorLogin
    )
}
