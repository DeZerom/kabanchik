package ru.kabanchik.common.tools.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.toYearMonthDay(): String {
    val format = LocalDateTime.Format {
        byUnicodePattern("yyyy.MM.dd")
    }

    return format.format(this)
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.toHoursMinutes(): String {
    val format = LocalDateTime.Format {
        byUnicodePattern("HH:mm")
    }

    return format.format(this)
}