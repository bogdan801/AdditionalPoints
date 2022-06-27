package com.bogdan801.additionalpoints.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityInformationEntity(
    @PrimaryKey(autoGenerate = true)
    val activityID: Int,
    val paragraph: String,
    val block: String,
    val description: String,
    val value: Float
)
