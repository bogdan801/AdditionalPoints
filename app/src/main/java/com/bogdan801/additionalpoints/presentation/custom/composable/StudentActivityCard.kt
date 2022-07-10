package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.PreviewActivity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.additionalpoints.domain.model.ActivityInformation
import com.bogdan801.additionalpoints.domain.model.StudentActivity
import com.bogdan801.additionalpoints.presentation.theme.AdditionalPointsTheme

@Composable
fun StudentActivityCard(
    modifier: Modifier = Modifier,
    activity: StudentActivity,
    onDeleteActivityClick: () -> Unit = {},
    onActivityClick: () -> Unit = {},
    shape: Shape = RoundedCornerShape(15.dp),
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.secondaryVariant,
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colors.secondaryVariant),
    elevation: Dp = 4.dp
) {
    Card(
        modifier = modifier.clip(shape).clickable(onClick = onActivityClick),
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        border = border,
        elevation = elevation
    ){
        Row(modifier = Modifier.fillMaxSize().height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(text = activity.description)
                Text(text = "Блок ${activity.activityInformation.block}")
                Text(text = "Пункт ${activity.activityInformation.paragraph}")
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 8.dp)
                    .width(1.dp)
                    .background(color = MaterialTheme.colors.secondaryVariant),
            )
            Box(
                modifier = Modifier.fillMaxHeight().width(65.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = String.format("%.2f", activity.value),
                    style = MaterialTheme.typography.h2
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    text = activity.date,
                    fontSize = 12.sp
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 8.dp)
                    .width(1.dp)
                    .background(color = MaterialTheme.colors.secondaryVariant),
            )
            Box(
                modifier = Modifier.fillMaxHeight().width(65.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    modifier = Modifier.padding(end = 3.dp),
                    onClick = onDeleteActivityClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete student activity",
                        tint = MaterialTheme.colors.secondary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewActivityCard() {
    AdditionalPointsTheme {
        StudentActivityCard(
            activity = StudentActivity(
                0,
                0,
                ActivityInformation(
                    0,
                    "0",
                    "0.0",
                    "dfdfdddf",
                    4f
                ),
                "fdfdfdfd",
                "26.04.2020",
                3.9f
            )
        )
    }
}