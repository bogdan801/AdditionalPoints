package com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomButton

@Composable
fun CreateGroupDialog(
    groupNameState: MutableState<String>,
    showDialogState: MutableState<Boolean>,
    onSaveClick: ()->Unit = {}
) {
    BasicDialogBox(
        isDialogOpen = showDialogState,
        title = "Створення нової групи"
    ){
        Row(modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp),
                value = groupNameState.value, 
                onValueChange = {
                    groupNameState.value = it
                },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            CustomButton(onClick = onSaveClick) {
                Text(text = "Зберегти")
            }
        }
    }
}