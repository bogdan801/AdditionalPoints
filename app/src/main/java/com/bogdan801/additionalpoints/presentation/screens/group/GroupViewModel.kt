package com.bogdan801.additionalpoints.presentation.screens.group

import androidx.lifecycle.ViewModel
import com.bogdan801.additionalpoints.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupViewModel
@Inject
constructor(
    val repository: Repository
): ViewModel() {

}