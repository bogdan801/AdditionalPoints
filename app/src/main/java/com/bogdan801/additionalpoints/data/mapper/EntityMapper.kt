package com.bogdan801.additionalpoints.data.mapper

import com.bogdan801.additionalpoints.data.database.entities.*
import com.bogdan801.additionalpoints.data.database.entities.relations.*
import com.bogdan801.additionalpoints.domain.model.*
import com.bogdan801.additionalpoints.domain.repository.Repository

fun ActivityInformationEntity.toActivityInformation(): ActivityInformation = ActivityInformation(
    activityID = activityID,
    paragraph = paragraph,
    block = block,
    description = description,
    value = value
)
fun ActivityInformation.toActivityInformationEntity(): ActivityInformationEntity = ActivityInformationEntity(
    activityID = activityID,
    paragraph = paragraph,
    block = block,
    description = description,
    value = value
)

fun GroupEntity.toGroup(): Group = Group(groupID = groupID, name = name)
fun Group.toGroupEntity(): GroupEntity = GroupEntity(groupID = groupID, name = name)

fun StudentEntity.toStudent(): Student = Student(studentID=studentID, groupID = groupID, fullName=fullName, isContract=isContract)
fun Student.toStudentEntity(): StudentEntity = StudentEntity(studentID=studentID, groupID = groupID, fullName=fullName, isContract=isContract)

suspend fun StudentActivityEntity.toStudentActivity(repository: Repository): StudentActivity = StudentActivity(
    studActID = studActID,
    studentID = studentID,
    activityInformation = repository.getActivityByID(activityID).toActivityInformation(),
    description = description,
    date = date,
    value = value
)

fun StudentActivity.toStudentActivityEntity(): StudentActivityEntity = StudentActivityEntity(
    studActID = studActID,
    studentID = studentID,
    activityID = activityInformation.activityID,
    description = description,
    date = date,
    value = activityInformation.value
)

fun GroupWithStudentsJunction.toGroup(): Group = Group(
    groupID = groupEntity.groupID,
    name = groupEntity.name,
    students = students.map { it.toStudent() }
)

suspend fun StudentWithActivitiesJunction.toStudent(repository: Repository): Student = Student(
    studentID = studentEntity.studentID,
    groupID = studentEntity.groupID,
    fullName = studentEntity.fullName,
    isContract = studentEntity.isContract,
    activities = activities.map { it.toStudentActivity(repository) }
)