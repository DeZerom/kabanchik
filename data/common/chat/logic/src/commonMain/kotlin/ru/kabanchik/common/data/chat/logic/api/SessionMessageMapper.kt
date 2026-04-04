package ru.kabanchik.common.data.chat.logic.api

import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionMessage

fun CommonApiSessionMessage.toDomain(): CommonSessionMessage = CommonSessionMessage(
    sessionId = sessionId,
    participantLogin = participant.login,
    participantName = participant.displayName,
    participantRole = participant.role
)