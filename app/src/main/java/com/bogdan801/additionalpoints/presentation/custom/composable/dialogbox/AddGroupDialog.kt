package com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomButton
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomTextField
import com.bogdan801.additionalpoints.R

@Composable
fun AddGroupDialog(
    groupNameState: State<String>,
    onTextChanged: (String)->Unit = {},
    showDialogState: MutableState<Boolean>,
    onSaveClick: ()->Unit = {}
) {
    BasicDialogBox(
        isDialogOpen = showDialogState,
        title = stringResource(id = R.string.creating_a_group)
    ){
        Row(modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextField(
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp),
                value = groupNameState.value,
                onValueChanged = onTextChanged,
                placeholder = stringResource(id = R.string.enter_a_group_name),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(8.dp))
            CustomButton(
                modifier = Modifier.height(45.dp),
                onClick = onSaveClick
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}