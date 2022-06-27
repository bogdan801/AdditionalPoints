package com.bogdan801.additionalpoints.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val studentID: Int,
    val groupID: Int,
    val fullName: String,
    val isContract: Boolean
)
