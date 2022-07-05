package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DrawerMenuItem(
    description: String,
    itemHeight: Dp = 50.dp,
    iconImageVector: ImageVector? = null,
    iconPainter: Painter? = null,
    iconTint: Color = MaterialTheme.colors.primary,
    iconSize: Dp = 24.dp,
    iconPadding: Dp = 16.dp,
    descriptionColor: Color = MaterialTheme.colors.onSurface,
    onItemClick: () -> Unit = {}
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(itemHeight)
        .clickable(onClick = onItemClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(iconImageVector!=null){
            Spacer(modifier = Modifier.width(iconPadding))
            Icon(
                modifier = Modifier
                    .size(iconSize),
                imageVector = iconImageVector,
                contentDescription = null,
                tint = iconTint
            )
            Spacer(modifier = Modifier.width(iconPadding))
        }
        else if(iconPainter!=null){
            Spacer(modifier = Modifier.width(iconPadding))
            Icon(
                modifier = Modifier
                    .size(iconSize),
                painter = iconPainter,
                contentDescription = null,
                tint = iconTint
            )
            Spacer(modifier = Modifier.width(iconPadding))
        }

        Text(text = description, color = descriptionColor, style = MaterialTheme.typography.body1)
    }
}