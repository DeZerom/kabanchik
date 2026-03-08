package ru.kabanchik.common.data.chat.logic.api

import kotlinx.datetime.LocalDateTime
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessageType
import ru.kabanchik.common.data.chatDetails.model.CommonApiSendMessage

fun String.toApiSendMessage(): CommonApiSendMessage = CommonApiSendMessage(
    content = this
)

fun CommonApiMessage.toDomain(): CommonMessage = CommonMessage(
    id = id,
    authorLogin = sender,
    text = content,
    type = type.toDomain(),
    time = LocalDateTime.parse(timestamp)
)

fun CommonApiMessageType.toDomain(): CommonMessageType = when (this) {
    CommonApiMessageType.Text -> CommonMessageType.Text
    CommonApiMessageType.System -> CommonMessageType.System
}