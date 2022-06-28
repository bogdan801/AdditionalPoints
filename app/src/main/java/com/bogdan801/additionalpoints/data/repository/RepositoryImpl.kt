package com.bogdan801.additionalpoints.data.repository

import android.content.Context
import com.bogdan801.additionalpoints.data.database.Dao
import com.bogdan801.additionalpoints.data.database.entities.ActivityInformationEntity
import com.bogdan801.additionalpoints.data.database.entities.GroupEntity
import com.bogdan801.additionalpoints.data.database.entities.StudentActivityEntity
import com.bogdan801.additionalpoints.data.database.entities.StudentEntity
import com.bogdan801.additionalpoints.data.database.entities.relations.GroupWithStudentsJunction
import com.bogdan801.additionalpoints.data.database.entities.relations.StudentWithActivitiesJunction
import com.bogdan801.additionalpoints.data.excel.report.AdditionalReportInfo
import com.bogdan801.additionalpoints.data.excel.report.generateReport
import com.bogdan801.additionalpoints.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class RepositoryImpl(
    private val dbDao: Dao
) : Repository {
    //insert
    override suspend fun insertActivity(activityInformationEntity: ActivityInformationEntity) {
        dbDao.insertActivity(activityInformationEntity)
    }

    override suspend fun insertGroup(groupEntity: GroupEntity) {
        dbDao.insertGroup(groupEntity)
    }

    override suspend fun insertStudent(studentEntity: StudentEntity) {
        dbDao.insertStudent(studentEntity)
    }

    override suspend fun insertStudentActivity(studentActivityEntity: StudentActivityEntity) {
        dbDao.insertStudentActivity(studentActivityEntity)
    }

    override suspend fun deleteActivity(activityID: Int) {
        dbDao.deleteActivity(activityID)
    }

    //delete
    override suspend fun deleteGroup(groupId: Int) {
        dbDao.deleteGroup(groupId)
    }

    override suspend fun deleteStudent(studentID: Int) {
        dbDao.deleteStudent(studentID)
    }

    override suspend fun deleteStudentActivity(studActID: Int) {
        dbDao.deleteStudentActivity(studActID)
    }

    //delete all
    override suspend fun deleteAllActivities() {
        dbDao.deleteAllActivities()
    }

    override suspend fun deleteAllGroup() {
        dbDao.deleteAllGroup()
    }

    override suspend fun deleteAllStudents() {
        dbDao.deleteAllStudents()
    }

    override suspend fun deleteAllStudentActivities() {
        dbDao.deleteAllStudentActivities()
    }

    //select
    override fun getAllActivities(): Flow<List<ActivityInformationEntity>> = dbDao.getActivities()

    override suspend fun getActivityByID(activityID: Int): ActivityInformationEntity = dbDao.getActivityByID(activityID)

    override fun getGroups(): Flow<List<GroupEntity>> = dbDao.getGroups()

    override suspend fun getGroupNameByID(groupId: Int): String = dbDao.getGroupNameByID(groupId)

    override fun getStudents(): Flow<List<StudentEntity>> = dbDao.getStudents()

    override fun getStudentsByGroup(groupID: Int) : Flow<List<StudentEntity>> = dbDao.getStudentsByGroup(groupID)

    override suspend fun getStudentsByGroupAndType(groupID: Int, isContract: Int) : List<StudentEntity> = dbDao.getStudentsByGroupAndType(groupID, isContract)

    override fun getGroupWithStudentsJunction(): Flow<List<GroupWithStudentsJunction>> = dbDao.getGroupWithStudents()

    override suspend fun getGroupWithStudentsJunctionByID(groupID: Int): GroupWithStudentsJunction = dbDao.getGroupWithStudentsJunctionByID(groupID)

    override fun getStudentWithActivitiesJunction(): Flow<List<StudentWithActivitiesJunction>> = dbDao.getStudentWithActivities()

    override suspend fun getStudentWithActivitiesJunctionByID(studentID: Int): StudentWithActivitiesJunction = dbDao.getStudentWithActivitiesJunctionByID(studentID)

    override suspend fun getAllDatesByGroup(groupID: Int): List<String> = dbDao.getAllDatesByGroup(groupID)

    override suspend fun getGetStudentActivitiesByMonth(studentID: Int, month: String): List<StudentActivityEntity> = dbDao.getGetStudentActivitiesByMonth(studentID, month)

    //generate exel report
    override suspend fun generateReportWorkbook(months: List<String>, groupID: Int, additionalInfo: AdditionalReportInfo): XSSFWorkbook = generateReport(months, groupID, this, additionalInfo)
}