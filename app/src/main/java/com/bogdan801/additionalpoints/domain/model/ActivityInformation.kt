package com.bogdan801.additionalpoints.domain.model

data class ActivityInformation(
    val activityID: Int,
    val paragraph: String,
    val block: String,
    val description: String,
    val value: Float
)