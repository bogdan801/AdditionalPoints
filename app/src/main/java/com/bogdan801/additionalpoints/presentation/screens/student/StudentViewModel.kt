package com.bogdan801.additionalpoints.presentation.screens.student

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.additionalpoints.data.mapper.toStudent
import com.bogdan801.additionalpoints.domain.model.Student
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
    private val _studentState = mutableStateOf(Student(0, 0, "", false, ""))
    val studentState: State<Student> = _studentState

    init {
        viewModelScope.launch {
            _studentState.value = handle.get<Int>("studentID")?.let { repository.getStudentWithActivitiesJunctionByID(it).toStudent(repository) }!!
        }
    }
}