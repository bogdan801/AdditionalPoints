package com.bogdan801.additionalpoints.domain.model

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.bogdan801.additionalpoints.data.util.getMonthFromDate
import com.bogdan801.additionalpoints.data.util.toLocalDate

data class Student @OptIn(ExperimentalMaterialApi::class) constructor(
    val studentID: Int,
    val groupID: Int,
    val fullName: String,
    val isContract: Boolean,
    var valueSum: MutableState<String> = mutableStateOf(""),
    var activities: MutableList<StudentActivity>? = null,
    val swipeableState: SwipeableState<Int> = SwipeableState(0)
){
    fun getActivitiesByMonths(borders: CurrentStudyYearBorders? = null) : Map<String, List<StudentActivity>> {
        activities?.sortBy { it.date.toLocalDate() }

        val monthWithActivitiesMap: MutableMap<String, MutableList<StudentActivity>> = mutableMapOf()

        if (borders != null){
            activities?.forEach{ activity ->
                monthWithActivitiesMap.getOrPut(borders.getMonthInBorders(activity.date)){
                    mutableListOf()
                }.add(activity)
            }
        }
        else{
            activities?.forEach{ activity ->
                monthWithActivitiesMap.getOrPut(getMonthFromDate(activity.date)){
                    mutableListOf()
                }.add(activity)
            }
        }

        return monthWithActivitiesMap.mapValues{ it.value.toList() }
    }
}