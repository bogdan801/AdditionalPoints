package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.data.util.getUkrainianMonthName

@Composable
fun MonthTitle(
    modifier: Modifier = Modifier,
    month: String = "01.2022",
    value: Float = 0f
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = getUkrainianMonthName(month).uppercase(), color = MaterialTheme.colors.secondaryVariant)
        Text(text = String.format("%.2f", value))
    }
}