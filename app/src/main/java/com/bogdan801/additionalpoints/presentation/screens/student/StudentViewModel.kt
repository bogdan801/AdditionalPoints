package com.bogdan801.additionalpoints.presentation.screens.student

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.bogdan801.additionalpoints.data.mapper.toStudent
import com.bogdan801.additionalpoints.domain.model.Student
import com.bogdan801.additionalpoints.domain.model.StudentActivity
import com.bogdan801.additionalpoints.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel
@Inject
constructor(
    private val repository: Repository,
    handle: SavedStateHandle
): ViewModel() {
    //DELETE STUDENT
    fun deleteStudent(){
        viewModelScope.launch {
            deleted = true
            repository.deleteStudent(studentState.value.studentID)
        }
    }
    private var deleted = false

    //DELETE ACTIVITY
    fun deleteActivityClick(id: Int){
        viewModelScope.launch {
            repository.deleteStudentActivity(id)
        }
    }

    //DATA
    private val _studentState = mutableStateOf(Student(0, 0, "", false, ""))
    val studentState: State<Student> = _studentState

    var uniqueMonths: List<String> = listOf()
        private set

    var monthStudentActivitiesMap: Map<String, List<StudentActivity>> = mapOf()

    fun getMonthSumValue(month: String) = monthStudentActivitiesMap[month]!!.sumOf { it.value.toDouble() }.toFloat()

    val totalStudentValue get() = studentState.value.valueSum

    init {
        viewModelScope.launch {
            repository.getStudentWithActivitiesJunctionByID(handle.get<Int>("studentID")!!).collect{ junction ->
                if(!deleted){
                    _studentState.value = junction.toStudent(repository)

                    uniqueMonths = _studentState.value.activities?.map {
                        val parts = it.date.split('.')
                        "${parts[1]}.${parts[2]}"
                    }?.distinct() ?: listOf()

                    monthStudentActivitiesMap = uniqueMonths.associateWith { month ->
                        (_studentState.value.activities?.filter { it.date.contains(month) } ?: listOf())
                    }
                }
            }
        }
    }
}