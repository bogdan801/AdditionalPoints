package com.bogdan801.additionalpoints.presentation.screens.group

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.additionalpoints.data.mapper.toGroup
import com.bogdan801.additionalpoints.data.mapper.toGroupEntity
import com.bogdan801.additionalpoints.data.mapper.toStudentEntity
import com.bogdan801.additionalpoints.domain.model.Group
import com.bogdan801.additionalpoints.domain.model.Student
import com.bogdan801.additionalpoints.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel
@Inject
constructor(
    private val repository: Repository,
    //handle: SavedStateHandle
): ViewModel() {
    //DATA
    private val _selectedGroupIndexState =  mutableStateOf(0)
    val selectedGroupIndexState: State<Int> =  _selectedGroupIndexState

    private val _groupListState: MutableState<List<Group>> = mutableStateOf(listOf())
    val groupListState: State<List<Group>> = _groupListState
    val selectedGroup get() = groupListState.value[selectedGroupIndexState.value]


    private val _budgetStudentsList: MutableState<List<Student>> = mutableStateOf(listOf())
    val budgetStudentsList: State<List<Student>> = _budgetStudentsList

    private val _contractStudentsList: MutableState<List<Student>> = mutableStateOf(listOf())
    val contractStudentsList: State<List<Student>> = _contractStudentsList

    private fun updateStudentsList(){
        if (selectedGroupIndexState.value >= groupListState.value.size) _selectedGroupIndexState.value = groupListState.value.lastIndex
        _budgetStudentsList.value = _groupListState.value[selectedGroupIndexState.value].students?.filter {student -> !student.isContract }?.sortedBy { it.fullName.lowercase() } ?: listOf()
        _contractStudentsList.value = _groupListState.value[selectedGroupIndexState.value].students?.filter { student -> student.isContract }?.sortedBy { it.fullName.lowercase() } ?: listOf()
    }

    fun selectNewGroup(index: Int){
        _selectedGroupIndexState.value = index
        updateStudentsList()
    }


    //ADD GROUP DIALOG
    //show add dialog state
    val showAddGroupDialogState = mutableStateOf(false)

    //new group textField
    private val _newGroupNameState = mutableStateOf("")
    val newGroupNameState: State<String> = _newGroupNameState

    fun onGroupNameChanged(newGroupName: String){
        _newGroupNameState.value = newGroupName
    }

    fun addNewGroup(){
        viewModelScope.launch {
            repository.insertGroup(Group(0, newGroupNameState.value, null).toGroupEntity())
            onGroupNameChanged("")
            showAddGroupDialogState.value = false
        }
    }


    //DELETE GROUP DIALOG
    val showDeleteGroupDialogState = mutableStateOf(false)

    fun deleteSelectedGroup(){
        viewModelScope.launch {
            repository.deleteGroup(selectedGroup.groupID)
        }
    }

    fun deleteSelectedGroupActivities(){
        viewModelScope.launch {
            repository.deleteAllGroupActivities(selectedGroup.groupID)
        }
    }

    //ADD STUDENT DIALOG
    val showAddStudentDialogState = mutableStateOf(false)

    private val _newStudentNameState = mutableStateOf("")
    val newStudentNameState: State<String> = _newStudentNameState

    val isNewStudentContract = mutableStateOf(false)

    fun onStudentNameChanged(newStudentName: String){
        _newStudentNameState.value = newStudentName
    }

    fun onSaveNewStudentClick(){
        viewModelScope.launch {
            repository.insertStudent(
                Student(
                    studentID = 0,
                    groupID = selectedGroup.groupID,
                    fullName = newStudentNameState.value,
                    isContract = isNewStudentContract.value
                ).toStudentEntity()
            )
            isNewStudentContract.value = false
            showAddStudentDialogState.value = false
            selectNewGroup(_selectedGroupIndexState.value)
        }
    }

    init {
        viewModelScope.launch {
            repository.getGroupWithStudentsJunction().collect { list ->
                val size = _groupListState.value.size
                _groupListState.value = list.map { it.toGroup(repository) }
                if (_groupListState.value.isNotEmpty()){
                    if(size != 0 && size<_groupListState.value.size) selectNewGroup(_groupListState.value.lastIndex)
                    else selectNewGroup(_selectedGroupIndexState.value)
                }
            }
        }

        viewModelScope.launch {
            repository.getStudentActivities().collect{
                if(_groupListState.value.isNotEmpty()){
                    selectedGroup.students?.forEach { student ->
                        student.valueSum.value = String.format("%.2f", repository.getStudentValueSum(student.studentID))
                    }
                }
            }
        }

    }
}