package com.bogdan801.additionalpoints.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.bogdan801.additionalpoints.data.database.entities.StudentActivityEntity
import com.bogdan801.additionalpoints.data.database.entities.StudentEntity

data class StudentWithActivitiesJunction(
    @Embedded
    val studentEntity: StudentEntity,
    @Relation(
        parentColumn = "studentID",
        entityColumn = "studentID"
    )
    val activities: List<StudentActivityEntity>
)