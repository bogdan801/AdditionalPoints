package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.presentation.theme.AdditionalPointsTheme

@Composable
fun GroupSelector(
    data: List<String> = listOf(),
    index: Int = 0,
    onAddGroupClick: () -> Unit = {},
    onDeleteGroupClick: () -> Unit = {},
    onGroupSelected: (index: Int, text: String) -> Unit = { _: Int, _: String -> },
    showButtons: Boolean = true
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        elevation = 8.dp,
        color = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ){
                CustomDropDownMenu(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    data = data,
                    onItemSelected = onGroupSelected,
                    index = index,
                )
            }

            if(showButtons){
                Row(modifier = Modifier
                    .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(
                        modifier = Modifier.fillMaxHeight().padding(vertical = 8.dp).width(60.dp),
                        onClick = onAddGroupClick,
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSecondary
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add group")
                    }
                    Button(
                        modifier = Modifier.fillMaxHeight().padding(8.dp).width(60.dp),
                        onClick = onDeleteGroupClick,
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary,
                            contentColor = MaterialTheme.colors.onSecondary
                        ),
                        enabled = data.isNotEmpty()
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete group")
                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun Preview() {
    AdditionalPointsTheme {
        GroupSelector()
    }
}