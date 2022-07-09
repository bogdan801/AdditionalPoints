package com.bogdan801.additionalpoints.domain.model

data class Student(
    val studentID: Int,
    val groupID: Int,
    val fullName: String,
    val isContract: Boolean,
    val valueSum: String = "",
    val activities: List<StudentActivity>? = null
)