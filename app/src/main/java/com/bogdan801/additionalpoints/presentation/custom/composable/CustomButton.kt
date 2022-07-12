package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    roundedCorner: Dp = 15.dp,
    isOutlined: Boolean = false,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    if(isOutlined){
        OutlinedButton(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(roundedCorner),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.secondaryVariant
            ),
            enabled = enabled,
            border = BorderStroke(1.dp, MaterialTheme.colors.primary)
        ) {
            content()
        }
    }
    else{
        Button(
            modifier = modifier,
            onClick = onClick,
            shape = RoundedCornerShape(roundedCorner),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary
            ),
            enabled = enabled,
        ) {
            content()
        }
    }
}