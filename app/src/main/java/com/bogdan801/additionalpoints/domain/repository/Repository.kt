package com.bogdan801.additionalpoints.domain.repository

import android.content.Context
import com.bogdan801.additionalpoints.data.database.entities.*
import com.bogdan801.additionalpoints.data.database.entities.relations.*
import com.bogdan801.additionalpoints.data.excel.report.AdditionalReportInfo
import kotlinx.coroutines.flow.Flow
import org.apache.poi.xssf.usermodel.XSSFWorkbook

interface Repository {
    //insert
    suspend fun insertActivity(activityInformationEntity: ActivityInformationEntity)
    suspend fun insertGroup(groupEntity: GroupEntity)
    suspend fun insertStudent(studentEntity: StudentEntity)
    suspend fun insertStudentActivity(studentActivityEntity: StudentActivityEntity)

    //update
    suspend fun updateStudentActivity(studentActivityEntity: StudentActivityEntity)

    //delete
    suspend fun deleteActivity(activityID: Int)
    suspend fun deleteGroup(groupId: Int)
    suspend fun deleteGroupStudents(groupId: Int)
    suspend fun deleteStudent(studentID: Int)
    suspend fun deleteStudentActivity(studActID: Int)
    suspend fun deleteAllActivitiesOfAStudent(studentID: Int)
    suspend fun deleteAllGroupActivities(groupId: Int)

    //delete all
    suspend fun deleteAllActivities()
    suspend fun deleteAllGroup()
    suspend fun deleteAllStudents()
    suspend fun deleteAllStudentActivities()

    //select
    fun getAllActivities():Flow<List<ActivityInformationEntity>>
    suspend fun getActivityByID(activityID: Int) : ActivityInformationEntity
    fun getGroups(): Flow<List<GroupEntity>>
    suspend fun getGroupNameByID(groupId: Int): String
    fun getStudents(): Flow<List<StudentEntity>>
    fun getStudentActivities(): Flow<List<StudentActivityEntity>>
    suspend fun getStudentValueSum(studentID: Int): Float
    suspend fun getStudentsByGroup(groupID: Int) : List<StudentEntity>
    suspend fun getStudentsByGroupAndType(groupID: Int, isContract: Int) : List<StudentEntity>
    fun getGroupWithStudentsJunction(): Flow<List<GroupWithStudentsJunction>>
    suspend fun getGroupWithStudentsJunctionByID(groupID: Int): GroupWithStudentsJunction
    fun getStudentWithActivitiesJunction(): Flow<List<StudentWithActivitiesJunction>>
    fun getStudentWithActivitiesJunctionByID(studentID: Int): Flow<StudentWithActivitiesJunction>
    suspend fun getAllDatesByGroup(groupID: Int): List<String>
    suspend fun getGetStudentActivitiesByMonth(studentID: Int, month: String): List<StudentActivityEntity>

    //generate exel report
    suspend fun generateReportWorkbook(months: List<String>, groupID: Int, additionalInfo: AdditionalReportInfo?): XSSFWorkbook
}