package ru.kabanchik.client.data.chatDetails.logic.internal.mappers

import ru.kabanchik.client.domain.model.chatDetails.Message
import ru.kabanchik.common.data.chatDetails.model.CommonApiMessage

internal fun Message.toApi(): CommonApiMessage = CommonApiMessage(
    login = authorLogin,
    text = text
)

internal fun CommonApiMessage.toDomain(): Message = Message(
    authorLogin = login,
    text = text
)