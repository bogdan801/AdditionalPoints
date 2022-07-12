package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StudentCard(
    modifier: Modifier = Modifier,
    studentFullName: String = "",
    value: String = "",
    onCardClick: () -> Unit = {},
    shape: Shape = MaterialTheme.shapes.large,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.secondaryVariant,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp
) {
    Card(
        modifier = modifier.padding(bottom = 1.dp).clickable(onClick = onCardClick),
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        border = border,
        elevation = elevation
    ) {
        Row(
            modifier = Modifier.height(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .weight(1f)
            ){
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = studentFullName,
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 8.dp)
                    .width(1.dp)
                    .background(color = MaterialTheme.colors.secondaryVariant),
            )
            Text(
                modifier = Modifier
                    .width(100.dp),
                text = value,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondaryVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}