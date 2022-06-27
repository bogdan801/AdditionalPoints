package com.bogdan801.additionalpoints.domain.model

data class StudentActivity(
    val studActID: Int,
    val studentID: Int,
    val activityInformation: ActivityInformation,
    val description: String,
    val date: String,
    val value: Float
)