package com.bogdan801.additionalpoints.data.util

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import java.time.YearMonth

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
} + " " + month.split('.')[1]

fun getLastDateOfMonth(month: String): String {
    val arr = month.split('.')
    return YearMonth.of(arr[1].toInt(),arr[0].toInt()).atEndOfMonth().dayOfMonth.toString()
}

fun getCurrentDate(): LocalDate = Clock.System.now().toLocalDateTime(currentSystemDefault()).date

fun LocalDate.asFormattedString() = "${dayOfMonth.toString().padStart(2, '0')}.${monthNumber.toString().padStart(2, '0')}.$year"

fun getCurrentDateAsString(): String = getCurrentDate().asFormattedString()

fun String.toLocalDate(): LocalDate {
    val arr = split('.')
    return LocalDate(dayOfMonth = arr[0].toInt(), monthNumber = arr[1].toInt(), year = arr[2].toInt())
}

fun isDateBetween(date: String, start: String, end: String, inclusive: Boolean = true): Boolean {
    val targetDate = date.toLocalDate()
    val startDate = start.toLocalDate()
    val endDate = end.toLocalDate()

    return if(inclusive) targetDate in startDate..endDate else targetDate in (startDate + DatePeriod(days = 1))..(endDate - DatePeriod(days = 1))
}

fun getMonthFromDate(date: String): String {
    val d = date.toLocalDate()
    return "${d.monthNumber.toString().padStart(2, '0')}.${d.year}"
}