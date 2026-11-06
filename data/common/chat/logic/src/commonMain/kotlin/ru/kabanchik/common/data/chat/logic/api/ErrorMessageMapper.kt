package ru.kabanchik.common.data.chat.logic.api

import ru.kabanchik.common.chat.model.CommonChatError
import ru.kabanchik.common.chat.model.CommonChatErrorCode
import ru.kabanchik.common.data.chatDetails.model.CommonApiErrorCode
import ru.kabanchik.common.data.chatDetails.model.CommonApiErrorMessage

fun CommonApiErrorMessage.toDomain(): CommonChatError = CommonChatError(
    code = code.toErrorCode(),
    content = content,
    sessionId = sessionId,
    correlationId = correlationId,
)

private fun String.toErrorCode(): CommonChatErrorCode = when (this) {
    CommonApiErrorCode.ExecutorOffShift -> CommonChatErrorCode.ExecutorOffShift
    CommonApiErrorCode.Forbidden -> CommonChatErrorCode.Forbidden
    CommonApiErrorCode.SessionNotAvailable -> CommonChatErrorCode.SessionNotAvailable
    CommonApiErrorCode.InvalidEndRequest -> CommonChatErrorCode.InvalidEndRequest
    CommonApiErrorCode.NotSessionExecutor -> CommonChatErrorCode.NotSessionExecutor
    CommonApiErrorCode.SessionNotOpen -> CommonChatErrorCode.SessionNotOpen
    CommonApiErrorCode.SessionEndFailed -> CommonChatErrorCode.SessionEndFailed
    CommonApiErrorCode.SessionClosed -> CommonChatErrorCode.SessionClosed
    else -> CommonChatErrorCode.Other
}
