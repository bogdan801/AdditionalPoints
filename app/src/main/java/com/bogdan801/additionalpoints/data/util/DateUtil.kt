package com.bogdan801.additionalpoints.data.util

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
}

fun getLastDateOfMonth(month: String): String {
    val arr = month.split('.')
    return YearMonth.of(arr[1].toInt(),arr[0].toInt()).atEndOfMonth().dayOfMonth.toString()
}