package com.bogdan801.additionalpoints.presentation.screens.report

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.additionalpoints.R
import com.bogdan801.additionalpoints.data.datastore.readIntFromDataStore
import com.bogdan801.additionalpoints.data.datastore.readStringFromDataStore
import com.bogdan801.additionalpoints.data.excel.report.AdditionalReportInfo
import com.bogdan801.additionalpoints.data.excel.report.Degree
import com.bogdan801.additionalpoints.data.mapper.toGroup
import com.bogdan801.additionalpoints.data.mapper.toStudent
import com.bogdan801.additionalpoints.data.util.sortMonthList
import com.bogdan801.additionalpoints.di.BaseApplication
import com.bogdan801.additionalpoints.domain.model.CurrentStudyYearBorders
import com.bogdan801.additionalpoints.domain.model.Group
import com.bogdan801.additionalpoints.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import javax.inject.Inject

@HiltViewModel
class ReportViewModel
@Inject
constructor(
    private val repository: Repository,
    baseApp: BaseApplication
): ViewModel() {
    //DATA
    private val _selectedGroupIndexState =  mutableStateOf(0)
    val selectedGroupIndexState: State<Int> =  _selectedGroupIndexState

    private val _groupListState: MutableState<List<Group>> = mutableStateOf(listOf())
    val groupListState: State<List<Group>> = _groupListState

    private val selectedGroup get() = groupListState.value[selectedGroupIndexState.value]

    private val _uniqueGroupMonthsState: MutableState<List<String>> = mutableStateOf(listOf())
    val uniqueGroupMonthsState: State<List<String>> = _uniqueGroupMonthsState

    var monthsStatesMap: Map<String, MutableState<Boolean>> = mapOf()

    fun isMonthSelected(): Boolean {
        var isSelected = false
        monthsStatesMap.forEach{ entry ->
            if(entry.value.value) isSelected = true
        }
        return isSelected
    }

    fun selectGroup(index: Int, context: Context){
        _selectedGroupIndexState.value = index
        getSelectedGroupMonths(context)
    }

    private fun getSelectedGroupMonths(context: Context){
        viewModelScope.launch {

            val areActivitiesShifted = context.readIntFromDataStore("isShifted") ?: 0
            var borders: CurrentStudyYearBorders? = null
            if (areActivitiesShifted == 1){
                val bordersString = context.readStringFromDataStore("borders") ?: CurrentStudyYearBorders.defaultBorders.toString()
                borders = CurrentStudyYearBorders.fromString(bordersString)
            }

            val students = repository.getStudentWithActivitiesJunction().first().map { it.toStudent(repository) }.filter { student -> student.groupID == selectedGroup.groupID}

            val listOfMonth: MutableList<String> = mutableListOf()
            students.forEach { student ->
                val activitiesMap = student.getActivitiesByMonths(borders)
                activitiesMap.keys.forEach { month ->
                    if(!listOfMonth.contains(month)) listOfMonth.add(month)
                }
            }

            _uniqueGroupMonthsState.value = sortMonthList(listOfMonth)

            monthsStatesMap = _uniqueGroupMonthsState.value.associateWith {
                mutableStateOf(true)
            }
        }
    }

    //GENERAL STATES
    //is general selected state
    private val _generalState: MutableState<Boolean> = mutableStateOf(true)
    val generalState: State<Boolean> = _generalState

    fun switchGeneralCheckbox(){
        _generalState.value = !_generalState.value
    }

    //degree index state
    private val _degreeIndexState: MutableState<Int> = mutableStateOf(0)
    val degreeIndexState: State<Int> = _degreeIndexState

    fun degreeIndexSelected(index: Int){
        _degreeIndexState.value = index
    }

    //course state
    private val _courseState: MutableState<String> = mutableStateOf("")
    val courseState: State<String> = _courseState

    fun courseTextChanged(newText: String){
        if(newText.length>_courseState.value.length){
            if(newText.isNotEmpty() && newText.length <= 1){
                val arr = _courseState.value.toCharArray()
                var newChar = newText.last()

                for (i in 0..arr.lastIndex){
                    if(arr[i] != newText[i]) {
                        newChar = newText[i]
                        break
                    }
                }
                if("123456789".contains(newChar)){
                    _courseState.value = newText
                }
            }
        }
        else{
            _courseState.value = newText
        }
    }

    //faculty state
    private val _facultyState: MutableState<String> = mutableStateOf("")
    val facultyState: State<String> = _facultyState

    fun facultyTextChanged(newText: String){
        _facultyState.value = newText
    }

    //headOfGroup state
    private val _headOfGroupState: MutableState<String> = mutableStateOf("")
    val headOfGroupState: State<String> = _headOfGroupState

    fun headOfGroupTextChanged(newText: String){
        _headOfGroupState.value = newText
    }

    //curator state
    private val _curatorState: MutableState<String> = mutableStateOf("")
    val curatorState: State<String> = _curatorState

    fun curatorTextChanged(newText: String){
        _curatorState.value = newText
    }

    //GENERATE REPORT
    fun onGenerateReportClick(launcher: ActivityResultLauncher<String>, workbook: MutableState<XSSFWorkbook>, context: Context){
        viewModelScope.launch {
            val areActivitiesShifted = context.readIntFromDataStore("isShifted") ?: 0
            var borders: CurrentStudyYearBorders? = null
            if (areActivitiesShifted == 1){
                val bordersString = context.readStringFromDataStore("borders") ?: CurrentStudyYearBorders.defaultBorders.toString()
                borders = CurrentStudyYearBorders.fromString(bordersString)
            }
            workbook.value = repository.generateReportWorkbook(
                months = uniqueGroupMonthsState.value.filter { monthsStatesMap[it]?.value == true }, selectedGroup.groupID,
                additionalInfo = AdditionalReportInfo(Degree.values()[degreeIndexState.value], courseState.value, facultyState.value, headOfGroupState.value, curatorState.value, borders),
                withGeneralSheet = _generalState.value
            )

            launcher.launch(context.getString(R.string.report)+".xlsx")
        }
    }

    init {
        val context = baseApp as Context

        viewModelScope.launch {
            repository.getGroups().collect{ groups ->
                _groupListState.value = groups.map { it.toGroup() }
                if(_groupListState.value.isNotEmpty()) selectGroup(selectedGroupIndexState.value, context)
            }
        }
    }
}