package com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.bogdan801.additionalpoints.domain.model.CurrentStudyYearBorders
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomButton
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomSwitch
import com.bogdan801.additionalpoints.R

@Composable
fun SettingsDialog(
    showDialogState: MutableState<Boolean>,
    allowShiftActivities: Boolean = false,
    borders: CurrentStudyYearBorders = CurrentStudyYearBorders.defaultBorders,
    onCheckBoxClick: () -> Unit = {},
    onSelectFirstSemesterStartClick: () -> Unit = {},
    onSelectFirstSemesterEndClick: () -> Unit = {},
    onSelectSecondSemesterStartClick: () -> Unit = {},
    onSelectSecondSemesterEndClick: () -> Unit = {}
){
    BasicDialogBox(
        isDialogOpen = showDialogState,
        title = stringResource(id = R.string.settings)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(MaterialTheme.colors.surface)
                .clickable(onClick = onCheckBoxClick)
                .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.shift_activities),
                    color = MaterialTheme.colors.secondaryVariant
                )
                CustomSwitch(
                    modifier = Modifier.padding(8.dp),
                    switchState = allowShiftActivities,
                    onStateChange = onCheckBoxClick
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(id = R.string.year_limits),
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.secondaryVariant
            )
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = stringResource(id = R.string.first_semester),
                color = MaterialTheme.colors.secondaryVariant
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                CustomButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = { onSelectFirstSemesterStartClick() },
                    isOutlined = true,
                    roundedCorner = 12.dp
                ) {
                    Text(text = borders.firstSemesterStart)
                }
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "-",
                    style = MaterialTheme.typography.h2
                )
                CustomButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = { onSelectFirstSemesterEndClick() },
                    isOutlined = true,
                    roundedCorner = 12.dp
                ) {
                    Text(text = borders.firstSemesterEnd)
                }
            }
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = stringResource(id = R.string.second_semester),
                color = MaterialTheme.colors.secondaryVariant
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                CustomButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = { onSelectSecondSemesterStartClick() },
                    isOutlined = true,
                    roundedCorner = 12.dp
                ) {
                    Text(text = borders.secondSemesterStart)
                }
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "-",
                    style = MaterialTheme.typography.h2
                )
                CustomButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = { onSelectSecondSemesterEndClick() },
                    isOutlined = true,
                    roundedCorner = 12.dp
                ) {
                    Text(text = borders.secondSemesterEnd)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}