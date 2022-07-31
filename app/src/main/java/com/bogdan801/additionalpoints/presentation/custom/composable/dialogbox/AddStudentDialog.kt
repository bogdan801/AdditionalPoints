package com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomButton
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomTextField
import com.bogdan801.additionalpoints.R

@Composable
fun AddStudentDialog(
    showDialogState: MutableState<Boolean>,
    newStudentName: String = "",
    onNameChanged: (newText: String) -> Unit = {},
    isContractState: MutableState<Boolean> = mutableStateOf(false),
    onSaveNewStudentClick: () -> Unit = {}
) {
    BasicDialogBox(
        isDialogOpen = showDialogState,
        title = stringResource(id = R.string.adding_student)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.full_name),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondaryVariant
            )
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(45.dp),
                value = newStudentName,
                onValueChanged = onNameChanged,
                placeholder = stringResource(id = R.string.initials)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.form_of_education),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondaryVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                CustomButton(
                    modifier = Modifier
                        .height(45.dp)
                        .weight(1f),
                    onClick = {
                        isContractState.value = false
                    },
                    isOutlined = true
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.budget).lowercase(),
                                color = MaterialTheme.colors.secondaryVariant
                            )
                            Checkbox(
                                checked = !isContractState.value,
                                onCheckedChange = {
                                    isContractState.value = !isContractState.value
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colors.primary
                                )
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                CustomButton(
                    modifier = Modifier
                        .height(45.dp)
                        .weight(1f),
                    onClick = {
                        isContractState.value = true
                    },
                    isOutlined = true
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.contract).lowercase(),
                                color = MaterialTheme.colors.secondaryVariant
                            )
                            Checkbox(
                                checked = isContractState.value,
                                onCheckedChange = {
                                    isContractState.value = !isContractState.value
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colors.primary
                                )
                            )
                        }
                    }
                }
            }

            CustomButton(
                modifier = Modifier
                    .padding(4.dp)
                    .height(45.dp),
                onClick = onSaveNewStudentClick
            ) {
                Text(text = stringResource(id = R.string.save), color = MaterialTheme.colors.onSecondary)
            }
        }
    }
}