package ru.kabanchik.common.data.chat.logic.api

import ru.kabanchik.common.chat.model.CommonSystemMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSystemMessage

fun CommonApiSystemMessage.toDomain(): CommonSystemMessage = CommonSystemMessage(
    message = content
)