package com.bogdan801.additionalpoints.domain.model

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Student @OptIn(ExperimentalMaterialApi::class) constructor(
    val studentID: Int,
    val groupID: Int,
    val fullName: String,
    val isContract: Boolean,
    var valueSum: MutableState<String> = mutableStateOf(""),
    val activities: MutableList<StudentActivity>? = null,
    val swipeableState: SwipeableState<Int> = SwipeableState(0)
)