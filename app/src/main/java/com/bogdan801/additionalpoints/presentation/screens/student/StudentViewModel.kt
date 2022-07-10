package com.bogdan801.additionalpoints.presentation.screens.student

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.additionalpoints.data.database.entities.StudentActivityEntity
import com.bogdan801.additionalpoints.data.mapper.toActivityInformation
import com.bogdan801.additionalpoints.data.mapper.toStudent
import com.bogdan801.additionalpoints.data.mapper.toStudentActivityEntity
import com.bogdan801.additionalpoints.data.util.getCurrentDate
import com.bogdan801.additionalpoints.domain.model.ActivityInformation
import com.bogdan801.additionalpoints.domain.model.Student
import com.bogdan801.additionalpoints.domain.model.StudentActivity
import com.bogdan801.additionalpoints.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
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

    //ADD ACTIVITY DIALOG
    val showAddActivityDialogState = mutableStateOf(false)

    private val _activityDescriptionState = mutableStateOf("")
    val activityDescriptionState: State<String> = _activityDescriptionState

    fun onDescriptionTextChanged(newText: String){
        _activityDescriptionState.value = newText
    }

    private val _selectedDateState = mutableStateOf(getCurrentDate())
    val selectedDateState: State<String> = _selectedDateState

    fun onSelectDateClick(context: Context){
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, { _, year, month, day ->
            _selectedDateState.value = "${day.toString().padStart(2, '0')}.${(month+1).toString().padStart(2, '0')}.${year}"
        }, startYear, startMonth, startDay).show()
    }

    private val _activityInformationListState = mutableStateOf(listOf<ActivityInformation>())
    val activityInformationListState: State<List<ActivityInformation>> = _activityInformationListState

    private val _selectedActivityIndexState = mutableStateOf(0)
    val selectedActivityIndexState: State<Int> = _selectedActivityIndexState

    private val _valueState = mutableStateOf("1.0")
    val valueState: State<String> = _valueState

    fun selectActivityInformation(index: Int){
        _selectedActivityIndexState.value = index
        _valueState.value = String.format("%.2f", _activityInformationListState.value[index].value)
    }

    fun onValueTextChange(newText: String){
        if(newText.length>_valueState.value.length){
            if(newText.isNotEmpty() && newText.length <= 4){
                val arr = _valueState.value.toCharArray()
                var newChar = newText.last()

                for (i in 0..arr.lastIndex){
                    if(arr[i] != newText[i]) {
                        newChar = newText[i]
                        break
                    }
                }

                if("1234567890".contains(newChar)){
                    if((_valueState.value.startsWith('0') && _valueState.value.contains("0.")) || !valueState.value.startsWith('0')){
                        _valueState.value = newText
                    }
                }
                else if(newChar == '.'){
                    if(!newText.startsWith('.') && !_valueState.value.contains('.')){
                        _valueState.value = newText
                    }
                }
            }

        }
        else{
            _valueState.value = newText
        }
    }

    fun saveStudentActivity(){
        viewModelScope.launch {
            repository.insertStudentActivity(
                StudentActivity(
                    studActID = 0,
                    studentID = studentState.value.studentID,
                    activityInformation = activityInformationListState.value[selectedActivityIndexState.value],
                    description = activityDescriptionState.value,
                    date = selectedDateState.value,
                    value = valueState.value.toFloat()
                ).toStudentActivityEntity()
            )
            _activityDescriptionState.value = ""
            _selectedDateState.value = getCurrentDate()
            _selectedActivityIndexState.value = 0
            _valueState.value = "0"
            showAddActivityDialogState.value = false
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

        viewModelScope.launch {
            _activityInformationListState.value = repository.getAllActivities().first().map { it.toActivityInformation() }
        }
    }
}