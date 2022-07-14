package com.bogdan801.additionalpoints.presentation.screens.student

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.additionalpoints.data.mapper.toActivityInformation
import com.bogdan801.additionalpoints.data.mapper.toStudent
import com.bogdan801.additionalpoints.data.mapper.toStudentActivityEntity
import com.bogdan801.additionalpoints.data.util.getCurrentDate
import com.bogdan801.additionalpoints.data.util.toLocalDate
import com.bogdan801.additionalpoints.domain.model.ActivityInformation
import com.bogdan801.additionalpoints.domain.model.Student
import com.bogdan801.additionalpoints.domain.model.StudentActivity
import com.bogdan801.additionalpoints.domain.repository.Repository
import com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox.StudentActivityIntention
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel
@Inject
constructor(
    private val repository: Repository,
    handle: SavedStateHandle
): ViewModel() {
    //DELETE STUDENT DIALOG
    val showDeleteStudentDialogState = mutableStateOf(false)

    fun deleteStudent(){
        viewModelScope.launch {
            deleted = true
            repository.deleteStudent(studentState.value.studentID)
        }
    }
    private var deleted = false



    //ADD/UPDATE ACTIVITY DIALOG
    val showAddActivityDialogState = mutableStateOf(false)

    fun onAddActivityDialogClick(){
        intention = StudentActivityIntention.Add
        showAddActivityDialogState.value = true
        _activityDescriptionState.value = ""
        _selectedDateState.value = getCurrentDate()
        _selectedActivityIndexState.value = 0
        _valueState.value = activityInformationListState.value[_selectedActivityIndexState.value].value.toString().replace(',', '.')
        println()
    }

    private var activityIdToEdit = 0
    fun onUpdateActivityDialogClick(activity: StudentActivity){
        intention = StudentActivityIntention.Update
        activityIdToEdit = activity.studActID
        showAddActivityDialogState.value = true
        _activityDescriptionState.value = activity.description
        _selectedDateState.value = activity.date
        _selectedActivityIndexState.value = activityInformationListState.value.indexOf(activityInformationListState.value.find { it.activityID == activity.activityInformation.activityID })
        _valueState.value = activity.value.toString()
    }

    private val _activityDescriptionState = mutableStateOf("")
    val activityDescriptionState: State<String> = _activityDescriptionState

    fun onDescriptionTextChanged(newText: String){
        _activityDescriptionState.value = newText
    }

    private val _selectedDateState = mutableStateOf(getCurrentDate())
    val selectedDateState: State<String> = _selectedDateState

    fun onSelectDateClick(context: Context){
        val arr = _selectedDateState.value.split('.')
        val startYear = arr[2].toInt()
        val startMonth = arr[1].toInt()-1
        val startDay = arr[0].toInt()

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
        _valueState.value = String.format("%.2f", _activityInformationListState.value[index].value).replace(',', '.')
    }

    fun onValueTextChange(newText: String){
        if(newText.length>_valueState.value.length){
            if(newText.isNotEmpty() && newText.length <= 5){
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
                else if (newChar == '-'){
                    if(_valueState.value.isBlank()){
                        _valueState.value = newText
                    }
                }
            }
        }
        else{
            _valueState.value = newText
        }
    }

    var intention: StudentActivityIntention = StudentActivityIntention.Add

    fun saveStudentActivity(intention: StudentActivityIntention){
        viewModelScope.launch {
            when(intention){
                StudentActivityIntention.Add -> {
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
                }
                StudentActivityIntention.Update -> {
                    repository.updateStudentActivity(
                        StudentActivity(
                            studActID = activityIdToEdit,
                            studentID = studentState.value.studentID,
                            activityInformation = activityInformationListState.value[selectedActivityIndexState.value],
                            description = activityDescriptionState.value,
                            date = selectedDateState.value,
                            value = valueState.value.toFloat()
                        ).toStudentActivityEntity()
                    )
                }
            }

            showAddActivityDialogState.value = false
        }
    }

    //DELETE ACTIVITY
    fun deleteActivityClick(id: Int){
        viewModelScope.launch {
            repository.deleteStudentActivity(id)
        }
    }

    //DATA
    private val _studentState = mutableStateOf(Student(0, 0, "", false, mutableStateOf("")))
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
                    _studentState.value.activities?.sortBy { toLocalDate(it.date) }

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