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

fun StudentEntity.toStudent(): Student = Student(studentID=studentID, groupID = groupID, fullName=fullName, isContract=isContract, valueSum = "")

suspend fun StudentEntity.toStudent(repository: Repository): Student {
    val valueSum = repository.getStudentValueSum(studentID)
    return Student(studentID=studentID, groupID = groupID, fullName=fullName, isContract=isContract, valueSum = String.format("%.2f", valueSum))
}

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
    value = value
)

suspend fun GroupWithStudentsJunction.toGroup(repository: Repository): Group = Group(
    groupID = groupEntity.groupID,
    name = groupEntity.name,
    students = students.map { it.toStudent(repository) }
)

suspend fun StudentWithActivitiesJunction.toStudent(repository: Repository): Student = Student(
    studentID = studentEntity.studentID,
    groupID = studentEntity.groupID,
    fullName = studentEntity.fullName,
    isContract = studentEntity.isContract,
    activities = activities.map { it.toStudentActivity(repository) }.toMutableList(),
    valueSum = String.format("%.2f",activities.sumOf { it.value.toDouble() })
)