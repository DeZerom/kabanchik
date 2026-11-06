package ru.kabanchik.common.data.chat.logic.api

import ru.kabanchik.common.chat.model.CommonChatError
import ru.kabanchik.common.chat.model.CommonChatErrorCode
import ru.kabanchik.common.data.chatDetails.model.CommonApiErrorCode
import ru.kabanchik.common.data.chatDetails.model.CommonApiErrorMessage

fun CommonApiErrorMessage.toDomain(): CommonChatError = CommonChatError(
    code = code.toErrorCode(),
    content = content,
    sessionId = sessionId,
)

private fun String.toErrorCode(): CommonChatErrorCode = when (this) {
    CommonApiErrorCode.ExecutorOffShift -> CommonChatErrorCode.ExecutorOffShift
    CommonApiErrorCode.Forbidden -> CommonChatErrorCode.Forbidden
    CommonApiErrorCode.SessionNotAvailable -> CommonChatErrorCode.SessionNotAvailable
    else -> CommonChatErrorCode.Other
}
