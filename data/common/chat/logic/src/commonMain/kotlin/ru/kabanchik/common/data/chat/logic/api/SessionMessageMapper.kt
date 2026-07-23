package ru.kabanchik.common.data.chat.logic.api

import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.chat.model.CommonSessionStatus
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionMessage
import ru.kabanchik.common.data.chatDetails.model.CommonApiSessionStatus

fun CommonApiSessionMessage.toDomain(): CommonSessionMessage = CommonSessionMessage(
    sessionId = sessionId,
    status = status.toDomain(),
    participantLogin = participant?.login,
    participantName = participant?.displayName,
    participantRole = participant?.role,
    clientRequestId = clientRequestId,
)

internal fun CommonApiSessionStatus.toDomain(): CommonSessionStatus = when (this) {
    CommonApiSessionStatus.Waiting -> CommonSessionStatus.Waiting
    CommonApiSessionStatus.Open -> CommonSessionStatus.Open
    CommonApiSessionStatus.Closed -> CommonSessionStatus.Closed
}
