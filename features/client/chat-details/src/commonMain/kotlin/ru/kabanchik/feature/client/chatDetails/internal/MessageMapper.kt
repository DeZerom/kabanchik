package ru.kabanchik.feature.client.chatDetails.internal

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.kabanchik.client.domain.model.chatDetails.Message
import ru.kabanchik.common.tools.extensions.toHoursMinutes
import ru.kabanchik.common.tools.extensions.toYearMonthDay
import ru.kabanchik.feature.client.chatDetails.api.ChatDetailsContract
import kotlin.time.Clock

internal fun Message.toUiState(userLogin: String): ChatDetailsContract.Message {
    val now = Clock.System.now()
    val dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    return ChatDetailsContract.Message(
        date = dateTime.toYearMonthDay(),
        time = dateTime.toHoursMinutes(),
        isUserAuthor = isUserAuthor(userLogin),
        text = text
    )
}