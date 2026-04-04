package ru.kabanchik.common.tools.tools

import kotlinx.datetime.format.MonthNames

object RussianMonthNames {
    val names: MonthNames = MonthNames(
        listOf("Января", "Февраля", "Марта", "Апреля", "Мая", "Июня",
            "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря")
    )
}