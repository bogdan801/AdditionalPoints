package com.bogdan801.additionalpoints.presentation.screens.group

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomTopAppBar
import com.bogdan801.additionalpoints.presentation.custom.composable.GroupSelector
import com.bogdan801.additionalpoints.presentation.theme.AdditionalPointsTheme
import kotlinx.coroutines.launch

@Composable
fun GroupScreen(
    navController: NavHostController? = null,
    viewModel: GroupViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopAppBar(
                iconButton = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Open Menu")
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "NULES",
                        style = MaterialTheme.typography.h1
                    )
                }
            )
        },

    ) {
        Column(modifier = Modifier
            .fillMaxSize()
        ) {
            val indexState = remember {
                mutableStateOf(0)
            }

            GroupSelector(
                data = listOf("КН19001б", "КН19002б"),
                onGroupSelected = { _, text ->
                    Toast.makeText(context, "${indexState.value} - $text", Toast.LENGTH_SHORT).show()
                },
                indexState = indexState,
                onAddGroupClick = {
                    Toast.makeText(context, "Adding group", Toast.LENGTH_SHORT).show()
                },
                onDeleteGroupClick = {
                    Toast.makeText(context, "Deleting group", Toast.LENGTH_SHORT).show()
                },
                showButtons = true
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    AdditionalPointsTheme {
        GroupScreen()
    }
}