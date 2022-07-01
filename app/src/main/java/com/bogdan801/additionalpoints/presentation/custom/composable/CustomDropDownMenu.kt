package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun CustomDropDownMenu(
    data: List<String> = listOf(),
    indexState: MutableState<Int> = remember{mutableStateOf(0)},
    onItemSelected: (index: Int, text: String) -> Unit = { _: Int, _: String -> }
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    if(data.isEmpty() && indexState.value!=-1){
        indexState.value = -1
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(15.dp)
                )
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
                .clickable {
                    if(data.isNotEmpty()) expanded = !expanded
                }
        ){
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp),
                text = if(data.isEmpty()) "Груп ще не додано" else data[indexState.value],
                style = MaterialTheme.typography.h4
            )
            if(data.isNotEmpty()){
                Icon(
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp),
                    imageVector = icon,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = "Icon"
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current){textFieldSize.width.toDp()}),
            offset = DpOffset(x = 8.dp, y = (-8).dp)
        ) {
            data.forEachIndexed { index, text ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        indexState.value = index
                        onItemSelected(index, text)
                    }
                ) {
                    Text(text = text)
                }
            }
        }
    }

}