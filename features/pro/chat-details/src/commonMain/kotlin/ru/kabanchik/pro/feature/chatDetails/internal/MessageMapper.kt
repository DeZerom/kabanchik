package ru.kabanchik.pro.feature.chatDetails.internal

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.kabanchik.common.tools.extensions.toHoursMinutes
import ru.kabanchik.common.tools.extensions.toYearMonthDay
import ru.kabanchik.pro.domain.chatDetails.model.ProMessage
import ru.kabanchik.pro.feature.chatDetails.api.ProChatDetailsContract
import kotlin.time.Clock

internal fun ProMessage.toUiState(userLogin: String): ProChatDetailsContract.ProMessage {
    val now = Clock.System.now()
    val dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    return ProChatDetailsContract.ProMessage(
        date = dateTime.toYearMonthDay(),
        time = dateTime.toHoursMinutes(),
        isUserAuthor = isUserAuthor(userLogin),
        text = text
    )
}