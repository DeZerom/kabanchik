package ru.kabanchik.client.data.chatDetails.internal.mappers

import ru.kabanchik.client.data.chatDetails.internal.models.ApiMessage
import ru.kabanchik.client.domain.model.chatDetails.Message

internal fun Message.toApi(): ApiMessage = ApiMessage(
    login = authorLogin,
    text = text
)

internal fun ApiMessage.toDomain(): Message = Message(
    authorLogin = login,
    text = text
)