package ru.kabanchik.pro.data.chat.logic.internal.mappers

import ru.kabanchik.pro.data.chatDetails.model.ProApiIncoming
import ru.kabanchik.pro.domain.chatDetails.model.ProIncoming

fun ProApiIncoming.toDomain() = ProIncoming(
    clientLogin = userName
)