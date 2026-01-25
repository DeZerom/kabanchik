package ru.kabanchik.client.data.chatDetails.logic.internal.mappers

import ru.kabanchik.client.data.chatDetails.model.ApiMessage
import ru.kabanchik.client.domain.model.chatDetails.Message

internal fun Message.toApi(): ApiMessage = ApiMessage(
    login = authorLogin,
    text = text
)

internal fun ApiMessage.toDomain(): Message = Message(
    authorLogin = login,
    text = text
)