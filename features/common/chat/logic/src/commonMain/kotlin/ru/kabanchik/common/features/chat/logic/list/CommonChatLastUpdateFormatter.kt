package ru.kabanchik.common.features.chat.logic.list

import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_list_time_date
import kabanchik.features.common.chat.logic.generated.resources.chat_list_time_today
import kabanchik.features.common.chat.logic.generated.resources.chat_list_time_yesterday
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import ru.kabanchik.common.tools.extensions.asTextResource
import ru.kabanchik.common.tools.extensions.toDayMonthYear
import ru.kabanchik.common.tools.extensions.toHoursMinutes
import ru.kabanchik.common.tools.textResource.TextResource
import kotlin.time.Clock

fun LocalDateTime.toChatListLastUpdateText(): TextResource {
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
    val time = toHoursMinutes()

    return when (date) {
        today -> Res.string.chat_list_time_today.asTextResource(time)
        today.minus(1, DateTimeUnit.DAY) -> Res.string.chat_list_time_yesterday.asTextResource(time)
        else -> Res.string.chat_list_time_date.asTextResource(toDayMonthYear(), time)
    }
}
