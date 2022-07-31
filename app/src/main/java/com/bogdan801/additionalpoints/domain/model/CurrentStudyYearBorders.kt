package com.bogdan801.additionalpoints.domain.model

import androidx.core.text.isDigitsOnly
import com.bogdan801.additionalpoints.data.util.*
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus

data class CurrentStudyYearBorders(
    var firstSemesterStart: String,
    var firstSemesterEnd: String,
    var secondSemesterStart: String,
    var secondSemesterEnd: String
){
    override fun toString(): String {
        return "$firstSemesterStart-$firstSemesterEnd;$secondSemesterStart-$secondSemesterEnd"
    }

    private fun isInFirstSemester(date: String): Boolean = isDateBetween(date, firstSemesterStart, firstSemesterEnd)
    private fun isInSecondSemester(date: String): Boolean = isDateBetween(date, secondSemesterStart, secondSemesterEnd)
    private fun isInTheBorders(date: String): Boolean = isInFirstSemester(date) || isInSecondSemester(date)

    fun getMonthInBorders(stringDate: String): String{
        val date = stringDate.toLocalDate()
        return when{
            date<firstSemesterStart.toLocalDate() -> {
                getMonthFromDate(firstSemesterStart)
            }
            isInTheBorders(stringDate) -> {
                getMonthFromDate(stringDate)
            }
            isDateBetween(stringDate, firstSemesterEnd, secondSemesterStart, false) -> {
                getMonthFromDate(secondSemesterStart)
            }
            date>secondSemesterEnd.toLocalDate() -> {
                getMonthFromDate((firstSemesterStart.toLocalDate()+DatePeriod(years = 1)).asFormattedString())
            }
            else -> ""
        }
    }

    companion object {
        val defaultBorders: CurrentStudyYearBorders
            get() {
                val currentDate = getCurrentDate()
                return if(currentDate.monthNumber>=9){
                    CurrentStudyYearBorders(
                        "01.09.${currentDate.year}",
                        "24.12.${currentDate.year}",
                        "01.02.${currentDate.year+1}",
                        "25.05.${currentDate.year+1}"
                    )
                }
                else {
                    CurrentStudyYearBorders(
                        "01.09.${currentDate.year-1}",
                        "24.12.${currentDate.year-1}",
                        "01.02.${currentDate.year}",
                        "25.05.${currentDate.year}"
                    )
                }
            }

        fun fromString(borders: String) : CurrentStudyYearBorders{
            val semesters = borders.split(';')
            if(semesters.size != 2) return defaultBorders

            val firstSemester = semesters[0].split('-')
            if(firstSemester.size != 2) return defaultBorders

            val secondSemester = semesters[1].split('-')
            if(secondSemester.size != 2) return defaultBorders

            if(!firstSemester[0].filter { it!='.' }.isDigitsOnly()) return defaultBorders
            val firstSemesterStart = firstSemester[0]

            if(!firstSemester[1].filter { it!='.' }.isDigitsOnly()) return defaultBorders
            val firstSemesterEnd = firstSemester[1]

            if(!secondSemester[0].filter { it!='.' }.isDigitsOnly()) return defaultBorders
            val secondSemesterStart = secondSemester[0]

            if(!secondSemester[1].filter { it!='.' }.isDigitsOnly()) return defaultBorders
            val secondSemesterEnd = secondSemester[1]

            return CurrentStudyYearBorders(firstSemesterStart, firstSemesterEnd, secondSemesterStart, secondSemesterEnd)
        }
    }
}
