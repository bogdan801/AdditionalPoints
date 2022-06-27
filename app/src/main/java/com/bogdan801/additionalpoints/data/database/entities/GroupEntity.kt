package com.bogdan801.additionalpoints.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val groupID: Int,
    val name: String
)
