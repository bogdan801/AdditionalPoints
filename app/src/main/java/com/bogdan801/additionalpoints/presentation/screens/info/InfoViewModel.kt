package com.bogdan801.additionalpoints.presentation.screens.info

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogdan801.additionalpoints.data.mapper.toActivityInformation
import com.bogdan801.additionalpoints.domain.model.ActivityInformation
import com.bogdan801.additionalpoints.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class InfoViewModel
@Inject constructor(
    val repository: Repository
): ViewModel() {
    private val _activityListState: MutableState<List<ActivityInformation>> = mutableStateOf(listOf())
    val activityListState: State<List<ActivityInformation>> = _activityListState

    init {
        viewModelScope.launch {
            _activityListState.value = repository.getAllActivities().first().map { it.toActivityInformation() }
        }
    }
}