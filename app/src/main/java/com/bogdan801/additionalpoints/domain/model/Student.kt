package com.bogdan801.additionalpoints.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Student(
    val studentID: Int,
    val groupID: Int,
    val fullName: String,
    val isContract: Boolean,
    var valueSum: MutableState<String> = mutableStateOf(""),
    val activities: MutableList<StudentActivity>? = null
)