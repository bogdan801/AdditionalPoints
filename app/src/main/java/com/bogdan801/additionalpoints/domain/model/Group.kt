package com.bogdan801.additionalpoints.domain.model

data class Group(
    val groupID: Int,
    val name: String,
    val students: List<Student>? = null
)