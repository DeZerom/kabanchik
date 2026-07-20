package ru.kabanchik.common.data.chat.logic.api

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessageType
import kotlin.time.Instant

fun CommonApiMessage.toDomain(): CommonMessage = CommonMessage(
    id = id,
    sessionId = sessionId,
    authorLogin = sender,
    text = content,
    type = type.toDomain(),
    time = Instant.parse(timestamp).toLocalDateTime(TimeZone.currentSystemDefault())
)

fun CommonApiMessageType.toDomain(): CommonMessageType = when (this) {
    CommonApiMessageType.Text -> CommonMessageType.Text
    CommonApiMessageType.System -> CommonMessageType.System
}
