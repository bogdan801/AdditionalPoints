package com.bogdan801.additionalpoints.data.database.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.bogdan801.additionalpoints.data.database.entities.GroupEntity
import com.bogdan801.additionalpoints.data.database.entities.StudentEntity

data class GroupWithStudentsJunction(
    @Embedded
    val groupEntity: GroupEntity,
    @Relation(
        parentColumn = "groupID",
        entityColumn = "groupID"
    )
    val students: List<StudentEntity>
)
