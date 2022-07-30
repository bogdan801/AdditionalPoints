package com.bogdan801.additionalpoints.presentation.screens.group

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.additionalpoints.data.datastore.readStringFromDataStore
import com.bogdan801.additionalpoints.data.datastore.saveIntToDataStore
import com.bogdan801.additionalpoints.data.datastore.saveStringToDataStore
import com.bogdan801.additionalpoints.data.mapper.toGroup
import com.bogdan801.additionalpoints.data.mapper.toGroupEntity
import com.bogdan801.additionalpoints.data.mapper.toStudentEntity
import com.bogdan801.additionalpoints.data.util.toLocalDate
import com.bogdan801.additionalpoints.domain.model.CurrentStudyYearBorders
import com.bogdan801.additionalpoints.domain.model.Group
import com.bogdan801.additionalpoints.domain.model.Student
import com.bogdan801.additionalpoints.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
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

    fun deleteStudent(studentID: Int){
        viewModelScope.launch {
            repository.deleteStudent(studentID)
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

    //SETTINGS DIALOG
    val showSettingsDialogState = mutableStateOf(false)

    fun openSettingsDialog(context: Context){
        viewModelScope.launch {
            val borders = context.readStringFromDataStore("borders")
            if(context.readStringFromDataStore("borders") == null){
                context.saveStringToDataStore("borders", CurrentStudyYearBorders.defaultBorders.toString())
                _yearBordersState.value = CurrentStudyYearBorders.defaultBorders
            }
            else{
                _yearBordersState.value = CurrentStudyYearBorders.fromString(borders!!)
                println()
            }
        }
        showSettingsDialogState.value = true
    }

    private val _allowShiftState = mutableStateOf(false)
    val allowShiftState: State<Boolean> = _allowShiftState

    private val _yearBordersState = mutableStateOf(CurrentStudyYearBorders.defaultBorders)
    val yearBordersState: State<CurrentStudyYearBorders> = _yearBordersState

    fun onAllowShiftSwitchClicked(context: Context){
        _allowShiftState.value = !_allowShiftState.value
        if(_allowShiftState.value){
            viewModelScope.launch {
                context.saveIntToDataStore("isShifted", 1)
            }
        }
        else {
            viewModelScope.launch {
                context.saveIntToDataStore("isShifted", 0)
            }
        }
    }

    fun onSelectFirstSemesterStartClick(context: Context){
        val arr = _yearBordersState.value.firstSemesterStart.split('.')
        val startYear = arr[2].toInt()
        val startMonth = arr[1].toInt()-1
        val startDay = arr[0].toInt()

        DatePickerDialog(context, { _, year, month, day ->
            val date = LocalDate(year = year, monthNumber = month+1, dayOfMonth = day)
            if(date >= _yearBordersState.value.firstSemesterEnd.toLocalDate()){
                Toast.makeText(context, "Дата початку першого семестру не може бути пізніша за дату його кінця", Toast.LENGTH_SHORT).show()
            }
            else{
                _yearBordersState.value = _yearBordersState.value.copy(firstSemesterStart = "${day.toString().padStart(2, '0')}.${(month+1).toString().padStart(2, '0')}.${year}")
                viewModelScope.launch{
                    context.saveStringToDataStore("borders", _yearBordersState.value.toString())
                }
            }
        }, startYear, startMonth, startDay).show()
    }

    fun onSelectFirstSemesterEndClick(context: Context){
        val arr = _yearBordersState.value.firstSemesterEnd.split('.')
        val startYear = arr[2].toInt()
        val startMonth = arr[1].toInt()-1
        val startDay = arr[0].toInt()

        DatePickerDialog(context, { _, year, month, day ->
            val date = LocalDate(year = year, monthNumber = month+1, dayOfMonth = day)
            if(date <= _yearBordersState.value.firstSemesterStart.toLocalDate() || date >= _yearBordersState.value.secondSemesterStart.toLocalDate()){
                Toast.makeText(context, "Дата кінця першого семестру має бути пізніша за початок першого семестру та раніша за початок другого", Toast.LENGTH_SHORT).show()
            }
            else {
                _yearBordersState.value = _yearBordersState.value.copy(firstSemesterEnd = "${day.toString().padStart(2, '0')}.${(month+1).toString().padStart(2, '0')}.${year}")
                viewModelScope.launch{
                    context.saveStringToDataStore("borders", _yearBordersState.value.toString())
                }
            }
        }, startYear, startMonth, startDay).show()
    }

    fun onSelectSecondSemesterStartClick(context: Context){
        val arr = _yearBordersState.value.secondSemesterStart.split('.')
        val startYear = arr[2].toInt()
        val startMonth = arr[1].toInt()-1
        val startDay = arr[0].toInt()

        DatePickerDialog(context, { _, year, month, day ->
            val date = LocalDate(year = year, monthNumber = month+1, dayOfMonth = day)
            if(date <= _yearBordersState.value.firstSemesterEnd.toLocalDate() || date >= _yearBordersState.value.secondSemesterEnd.toLocalDate()){
                Toast.makeText(context, "Дата початку другого семестру має бути пізніша за кінець першого семестру та раніша за кінець другого", Toast.LENGTH_SHORT).show()
            }
            else{
                _yearBordersState.value = _yearBordersState.value.copy(secondSemesterStart = "${day.toString().padStart(2, '0')}.${(month+1).toString().padStart(2, '0')}.${year}")
                viewModelScope.launch{
                    context.saveStringToDataStore("borders", _yearBordersState.value.toString())
                }
            }

        }, startYear, startMonth, startDay).show()
    }

    fun onSelectSecondSemesterEndClick(context: Context){
        val arr = _yearBordersState.value.secondSemesterEnd.split('.')
        val startYear = arr[2].toInt()
        val startMonth = arr[1].toInt()-1
        val startDay = arr[0].toInt()

        DatePickerDialog(context, { _, year, month, day ->
            val date = LocalDate(year = year, monthNumber = month+1, dayOfMonth = day)
            if(date <= _yearBordersState.value.secondSemesterStart.toLocalDate()){
                Toast.makeText(context, "Дата кінця другого семестру не може бути раніша за дату його початку", Toast.LENGTH_SHORT).show()
            }
            else {
                _yearBordersState.value = _yearBordersState.value.copy(secondSemesterEnd = "${day.toString().padStart(2, '0')}.${(month+1).toString().padStart(2, '0')}.${year}")
                viewModelScope.launch{
                    context.saveStringToDataStore("borders", _yearBordersState.value.toString())
                }
            }
        }, startYear, startMonth, startDay).show()
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