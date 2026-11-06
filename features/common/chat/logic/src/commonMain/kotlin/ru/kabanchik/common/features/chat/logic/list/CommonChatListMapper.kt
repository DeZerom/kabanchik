package ru.kabanchik.common.features.chat.logic.list

import ru.kabanchik.common.chat.model.CommonChatSummary
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.feature.chat.model.CommonUiChatItem
import ru.kabanchik.common.tools.textResource.TextResource

fun CommonChatSummary.toUiChatItem(): CommonUiChatItem {
    return CommonUiChatItem(
        id = sessionId,
        title = firstMessageContent.orEmpty(),
        lastUpdate = lastMessageTimestamp?.toChatListLastUpdateText() ?: TextResource.Raw(""),
        lastMessage = lastMessageContent.orEmpty(),
        hasUnread = false,
        otherPersonName = participantName
    )
}

fun List<CommonUiChatItem>.updateWithMessage(
    message: CommonMessage,
    currentUserLogin: String,
): List<CommonUiChatItem> {
    val existingItem = firstOrNull { it.id == message.sessionId }
    val isIncomingMessage = !message.isUserAuthor(currentUserLogin)
    val updatedItem = existingItem?.copy(
        title = existingItem.title.ifEmpty {
            message.text.takeIf { message.type == CommonMessageType.Text }.orEmpty()
        },
        lastUpdate = message.time.toChatListLastUpdateText(),
        lastMessage = message.text,
        hasUnread = existingItem.hasUnread || isIncomingMessage
    ) ?: message.toUiChatItem(hasUnread = isIncomingMessage)

    return listOf(updatedItem) + filterNot { it.id == message.sessionId }
}

private fun CommonMessage.toUiChatItem(hasUnread: Boolean): CommonUiChatItem {
    return CommonUiChatItem(
        id = sessionId,
        title = text.takeIf { type == CommonMessageType.Text }.orEmpty(),
        lastUpdate = time.toChatListLastUpdateText(),
        lastMessage = text,
        hasUnread = hasUnread,
        otherPersonName = authorLogin
    )
}
