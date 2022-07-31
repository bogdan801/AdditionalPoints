package com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomButton
import com.bogdan801.additionalpoints.R

@Composable
fun DeleteStudentDialog(
    showDialogState: MutableState<Boolean>,
    onDeleteStudentClick: ()->Unit = {},
    onCancelClick: ()->Unit = {}
) {
    BasicDialogBox(
        isDialogOpen = showDialogState,
        title = stringResource(id = R.string.delete_student)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                text = stringResource(id = R.string.delete_student_confirm),
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.secondaryVariant,
                textAlign = TextAlign.Center
            )
            Row(modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButton(
                    modifier= Modifier
                        .width(150.dp)
                        .height(45.dp),
                    onClick = {
                        onDeleteStudentClick()
                        showDialogState.value = false
                    },
                    isOutlined = true
                ) {
                    Text(text = stringResource(id = R.string.delete), textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.width(8.dp))
                CustomButton(
                    modifier= Modifier
                        .width(150.dp)
                        .height(45.dp),
                    onClick = {
                        onCancelClick()
                        showDialogState.value = false
                    },
                ) {
                    Text(text = stringResource(id = R.string.cancel), textAlign = TextAlign.Center)
                }
            }
        }

    }
}