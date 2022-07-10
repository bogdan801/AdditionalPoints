package com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.data.util.getCurrentDate
import com.bogdan801.additionalpoints.domain.model.ActivityInformation
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomButton
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomDropDownMenu
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomTextField

@Composable
fun AddStudentActivityDialog(
    showDialogState: MutableState<Boolean>,
    description: String = "",
    onDescriptionChanged: (newText: String) -> Unit = {},
    date: String = getCurrentDate(),
    onDateSelectClick: () -> Unit = {},
    activityInformationList: List<ActivityInformation> = listOf(),
    index: Int = 0,
    onCheckBoxIndexChanged: (index: Int, text: String) -> Unit = { _: Int, _: String -> },
    value: String = "1.0",
    onValueChanged: (String) -> Unit = {},
    onSaveActivityClick: () -> Unit = {}
) {
    BasicDialogBox(
        isDialogOpen = showDialogState,
        title = "Додавання балу"
    ){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "Захід",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondaryVariant
            )
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .height(45.dp),
                value = description,
                onValueChanged = onDescriptionChanged,
                placeholder = "Назва заходу"
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "Дата проведення",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondaryVariant
            )
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .height(43.dp),
                onClick = onDateSelectClick,
                isOutlined = true
            ) {
                Text(text = date)
            }
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "Тип заходу",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondaryVariant
            )
            CustomDropDownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .height(45.dp),
                data = activityInformationList.map { "${it.block}.${it.paragraph} ${it.description}" },
                index = index,
                onItemSelected = onCheckBoxIndexChanged
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(45.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                    text = "Бал",
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.secondaryVariant
                )
                CustomTextField(
                    modifier = Modifier
                        .weight(1f)
                        .height(45.dp),
                    value = value,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    onValueChanged = onValueChanged
                )
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ){
                CustomButton(
                    modifier = Modifier.height(45.dp),
                    onClick = onSaveActivityClick
                ) {
                    Text(text = "Зберегти", color = MaterialTheme.colors.onSecondary)
                }
            }

        }
    }
}