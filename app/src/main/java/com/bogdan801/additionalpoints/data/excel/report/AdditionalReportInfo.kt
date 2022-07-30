package com.bogdan801.additionalpoints.data.excel.report

import com.bogdan801.additionalpoints.domain.model.CurrentStudyYearBorders


enum class Degree(val degreeName: String){
    Bachelor("Бакалавр"),
    Master("Магістр")
}

data class AdditionalReportInfo(
    val degree: Degree,
    val course: String,
    val faculty: String,
    val headOfGroup: String,
    val curator: String,
    val borders: CurrentStudyYearBorders? = null
)