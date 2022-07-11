package com.bogdan801.additionalpoints.data.util

import kotlinx.datetime.LocalDate
import java.time.YearMonth
import java.util.*

fun getUkrainianMonthName(month: String): String = when(month.split('.')[0].toInt()) {
    1 -> "Січень"
    2 -> "Лютий"
    3 -> "Березень"
    4 -> "Квітень"
    5 -> "Травень"
    6 -> "Червень"
    7 -> "Липень"
    8 -> "Серпень"
    9 -> "Вересень"
    10 -> "Жовтень"
    11 -> "Листопад"
    12 -> "Грудень"
    else -> ""
}

fun getLastDateOfMonth(month: String): String {
    val arr = month.split('.')
    return YearMonth.of(arr[1].toInt(),arr[0].toInt()).atEndOfMonth().dayOfMonth.toString()
}

fun getCurrentDate(): String {
    val c = Calendar.getInstance()
    return "${c.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')}.${(c.get(Calendar.MONTH)+1).toString().padStart(2, '0')}.${c.get(Calendar.YEAR)}"
}

fun toLocalDate(date: String): LocalDate {
    val arr = date.split('.')
    return LocalDate(dayOfMonth = arr[0].toInt(), monthNumber = arr[1].toInt(), year = arr[2].toInt())
}