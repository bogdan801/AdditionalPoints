package com.bogdan801.additionalpoints.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bogdan801.additionalpoints.data.database.entities.GroupEntity
import com.bogdan801.additionalpoints.data.database.entities.ActivityInformationEntity
import com.bogdan801.additionalpoints.data.database.entities.StudentActivityEntity
import com.bogdan801.additionalpoints.data.database.entities.StudentEntity

@Database(
    entities = [ActivityInformationEntity::class, GroupEntity::class, StudentEntity::class, StudentActivityEntity::class],
    exportSchema = true,
    version = 1
)
abstract class Database : RoomDatabase(){
    abstract val dbDao: Dao
}