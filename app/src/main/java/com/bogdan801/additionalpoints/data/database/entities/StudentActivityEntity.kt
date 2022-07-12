package com.bogdan801.additionalpoints.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StudentActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val studActID: Int,
    val studentID: Int,
    val activityID: Int,
    val description: String,
    val date: String,
    val value: Float
)