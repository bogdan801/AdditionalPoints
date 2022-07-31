package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.R

@Composable
fun TotalStudentValueCard(
    modifier: Modifier = Modifier,
    value: String = "0.0",
    shape: Shape = RoundedCornerShape(15.dp),
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.secondaryVariant,
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colors.secondaryVariant),
    elevation: Dp = 4.dp
) {
    Card(
        modifier = modifier.clip(shape),
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        border = border,
        elevation = elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.total_value),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondaryVariant
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = value,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}