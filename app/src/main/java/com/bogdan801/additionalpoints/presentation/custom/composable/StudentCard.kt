package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentCard(
    modifier: Modifier = Modifier,
    studentFullName: String = "",
    value: String = "",
    onCardClick: () -> Unit = {},
    swipeableState: SwipeableState<Int> = rememberSwipeableState(0),
    onDeleteStudentClick: () -> Unit = {},
    shape: Shape = MaterialTheme.shapes.large,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.secondaryVariant,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp
) {
    val offset = 70.dp
    val sizePx = with(LocalDensity.current) { (-offset).toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    Card(
        modifier = modifier
            .padding(bottom = 1.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .height(IntrinsicSize.Min),
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        border = border,
        elevation = elevation
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp)
                .background(MaterialTheme.colors.error),
            contentAlignment = Alignment.CenterEnd
        ){
            Box(modifier = Modifier
                .fillMaxHeight()
                .width(offset),
                contentAlignment = Alignment.Center
            ){
                IconButton(
                    onClick = onDeleteStudentClick
                ) {
                    Icon(imageVector = Icons.Default.Delete, tint = MaterialTheme.colors.onError, contentDescription = "Delete student")
                }
            }
        }

        Surface(modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            .background(backgroundColor)
            .clickable(onClick = onCardClick),
            elevation = 4.dp
        ){
            Row(
                modifier = Modifier.heightIn(min = 70.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(LocalDensity.current.run { -swipeableState.offset.value.roundToInt().toDp()}))
                Box(modifier = Modifier
                    .weight(1f)
                ){
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = studentFullName,
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.secondaryVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .height(70.dp)
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
}