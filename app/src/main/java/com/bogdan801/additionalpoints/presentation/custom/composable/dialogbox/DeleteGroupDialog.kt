package com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.domain.model.Group
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomButton

@Composable
fun DeleteGroupDialog(
    showDialogState: MutableState<Boolean>,
    onDeleteGroupClick: ()->Unit = {},
    onDeleteGroupActivitiesClick: ()->Unit = {},
    onCancelClick: ()->Unit = {showDialogState.value = false}
) {
    BasicDialogBox(
        isDialogOpen = showDialogState,
        title = "Видалити"
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButton(
                    modifier= Modifier.width(150.dp).height(55.dp),
                    onClick = {
                        onDeleteGroupClick()
                        showDialogState.value = false
                    },
                    isOutlined = true
                ) {
                    Text(text = "Групу", textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.width(8.dp))
                CustomButton(
                    modifier= Modifier.width(150.dp).height(55.dp),
                    onClick = {
                        onDeleteGroupActivitiesClick()
                        showDialogState.value = false
                    },
                    isOutlined = true) {
                    Text(text = "Бали студентів", textAlign = TextAlign.Center)
                }
            }
            CustomButton(
                modifier = Modifier.height(45.dp),
                onClick = {
                    onCancelClick()
                    showDialogState.value = false
                }
            ) {
                Text(text = "Скасувати", textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
    }
}