package ru.kabanchik.pro.feature.chat.internal.details

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.tools.extensions.toHoursMinutes
import ru.kabanchik.common.tools.extensions.toYearMonthDay
import ru.kabanchik.pro.feature.chat.api.details.ProChatDetailsContract
import kotlin.time.Clock

internal fun CommonMessage.toUiState(userLogin: String): ProChatDetailsContract.ProMessage {
    val now = Clock.System.now()
    val dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    return ProChatDetailsContract.ProMessage(
        date = dateTime.toYearMonthDay(),
        time = dateTime.toHoursMinutes(),
        isUserAuthor = isUserAuthor(userLogin),
        text = text,
        authorLogin = authorLogin
    )
}