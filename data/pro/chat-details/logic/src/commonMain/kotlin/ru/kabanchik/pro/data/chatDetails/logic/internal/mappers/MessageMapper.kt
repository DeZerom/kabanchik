package ru.kabanchik.pro.data.chatDetails.logic.internal.mappers

import ru.kabanchik.pro.data.chatDetails.model.ApiProMessage
import ru.kabanchik.pro.domain.chatDetails.model.ProMessage

internal fun ProMessage.toApi() = ApiProMessage(
    login = authorLogin,
    text = text
)

internal fun ApiProMessage.toDomain(): ProMessage = ProMessage(
    authorLogin = login,
    text = text
)