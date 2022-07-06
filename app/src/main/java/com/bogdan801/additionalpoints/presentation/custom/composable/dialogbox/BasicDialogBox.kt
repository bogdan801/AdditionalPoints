package com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun BasicDialogBox(
    isDialogOpen: MutableState<Boolean>,
    title: String = "",
    width: Dp = 350.dp,
    roundingAmount: Dp = 15.dp,
    content: @Composable (BoxScope.()->Unit)? = null
) {
    if(isDialogOpen.value) {
        Dialog(onDismissRequest = { isDialogOpen.value = false }) {
            Column(
                modifier = Modifier
                    .width(width)
                    .background(
                        color = MaterialTheme.colors.primaryVariant,
                        shape = RoundedCornerShape(roundingAmount)
                    )
                    .clip(RoundedCornerShape(roundingAmount)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = title,
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.h2,
                        textAlign = TextAlign.Center
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()){
                    content?.invoke(this)
                }
            }
        }
    }
}