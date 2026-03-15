package ru.kabanchik.common.tools.extensions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import ru.kabanchik.common.tools.tools.RussianMonthNames

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

fun LocalDate.toDayFullMonth(): String {
    val format = LocalDate.Format {
        day
        char(' ')
        monthName(RussianMonthNames.names)
    }

    return format.format(this)
}