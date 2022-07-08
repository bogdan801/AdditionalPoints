package com.bogdan801.additionalpoints.presentation.screens.group

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.additionalpoints.data.mapper.toGroup
import com.bogdan801.additionalpoints.data.mapper.toGroupEntity
import com.bogdan801.additionalpoints.data.mapper.toStudent
import com.bogdan801.additionalpoints.domain.model.Group
import com.bogdan801.additionalpoints.domain.model.Student
import com.bogdan801.additionalpoints.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel
@Inject
constructor(
    private val repository: Repository
): ViewModel() {
    //DATA
    val selectedGroupIndexState =  mutableStateOf(0)

    private val _groupListState: MutableState<List<Group>> = mutableStateOf(listOf())
    val groupListState: State<List<Group>> = _groupListState
    val selectedGroup get() = if(groupListState.value.isNotEmpty())groupListState.value[selectedGroupIndexState.value] else null

    private val _studentGroupStudentsList: MutableState<List<Student>> = mutableStateOf(listOf())
    val groupStudentsList: State<List<Student>> = _studentGroupStudentsList

    fun updateStudentsList(){
        viewModelScope.launch {
            selectedGroup?.let { group ->
                repository.getStudentsByGroup(group.groupID).collect{ studentEntitiesList ->
                    _studentGroupStudentsList.value = studentEntitiesList.map { it.toStudent() }
                }
            }
        }
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
            selectedGroup?.let {
                repository.deleteGroup(it.groupID)
            }
        }
    }

    fun deleteSelectedGroupActivities(){
        viewModelScope.launch {
            selectedGroup?.let {
                repository.deleteAllGroupActivities(it.groupID)
            }
        }
    }


    init {
        viewModelScope.launch {
            repository.getGroups().collect { list ->
                _groupListState.value = list.map { it.toGroup() }
            }
        }
    }
}