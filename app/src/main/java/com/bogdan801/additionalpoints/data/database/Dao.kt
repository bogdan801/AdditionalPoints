package com.bogdan801.additionalpoints.data.database

import androidx.room.*
import androidx.room.Dao
import com.bogdan801.additionalpoints.data.database.entities.*
import com.bogdan801.additionalpoints.data.database.entities.relations.*
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    //insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activityInformationEntity: ActivityInformationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(groupEntity: GroupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(studentEntity: StudentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentActivity(studentActivityEntity: StudentActivityEntity)

    //delete
    @Query("DELETE FROM activityinformationentity WHERE activityID == :activityID")
    suspend fun deleteActivity(activityID: Int)

    @Query("DELETE FROM groupentity WHERE groupID == :groupId")
    suspend fun deleteGroup(groupId: Int)

    @Query("DELETE FROM studententity WHERE studentID == :studentID")
    suspend fun deleteStudent(studentID: Int)

    @Query("DELETE FROM studentactivityentity WHERE studActID == :studActID")
    suspend fun deleteStudentActivity(studActID: Int)

    //delete all
    @Query("DELETE FROM activityinformationentity")
    suspend fun deleteAllActivities()

    @Query("DELETE FROM groupentity")
    suspend fun deleteAllGroup()

    @Query("DELETE FROM studententity")
    suspend fun deleteAllStudents()

    @Query("DELETE FROM studentactivityentity")
    suspend fun deleteAllStudentActivities()

    //select
    @Query("SELECT * FROM activityinformationentity")
    fun getActivities() : Flow<List<ActivityInformationEntity>>

    @Query("SELECT * FROM activityinformationentity WHERE activityID == :activityID")
    suspend fun getActivityByID(activityID: Int) : ActivityInformationEntity

    @Query("SELECT * FROM groupentity")
    fun getGroups() : Flow<List<GroupEntity>>

    @Transaction
    @Query("SELECT * FROM groupentity")
    fun getGroupWithStudents() : Flow<List<GroupWithStudentsJunction>>

    @Transaction
    @Query("SELECT * FROM groupentity WHERE groupID == :groupID")
    suspend fun getGroupWithStudentsJunctionByID(groupID: Int): GroupWithStudentsJunction

    @Query("SELECT * FROM studententity")
    fun getStudents() : Flow<List<StudentEntity>>

    @Query("SELECT * FROM studententity WHERE groupID == :groupID")
    fun getStudentsByGroup(groupID: Int) : Flow<List<StudentEntity>>

    @Query("SELECT * FROM studententity WHERE groupID == :groupID AND isContract == :isContract")
    suspend fun getStudentsByGroupAndType(groupID: Int, isContract: Int) : List<StudentEntity>

    @Transaction
    @Query("SELECT * FROM studententity")
    fun getStudentWithActivities() : Flow<List<StudentWithActivitiesJunction>>

    @Transaction
    @Query("SELECT * FROM studententity WHERE studentID == :studentID")
    suspend fun getStudentWithActivitiesJunctionByID(studentID: Int): StudentWithActivitiesJunction

    @Query("SELECT date FROM studentactivityentity INNER JOIN studententity ON studentactivityentity.studentID=studententity.studentID WHERE groupID == :groupID")
    suspend fun getAllDatesByGroup(groupID: Int): List<String>

    @Query("SELECT * FROM studentactivityentity WHERE studentID == :studentID AND date LIKE '%.' || :month")
    suspend fun getGetStudentActivitiesByMonth(studentID: Int, month: String): List<StudentActivityEntity>
}