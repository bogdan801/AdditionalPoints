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
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomButton
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomTextField

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
        title = "Додавання студента"
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Прізвище, ім'я, по батькові",
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
                placeholder = "ПІБ"
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Форма навчання",
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
                                text = "бюджет",
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
                                text = "контракт",
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
                modifier = Modifier.padding(4.dp).height(45.dp),
                onClick = onSaveNewStudentClick
            ) {
                Text(text = "Зберегти", color = MaterialTheme.colors.onSecondary)
            }
        }
    }
}