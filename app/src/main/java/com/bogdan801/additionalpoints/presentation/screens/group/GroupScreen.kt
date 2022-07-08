package com.bogdan801.additionalpoints.presentation.screens.group

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
import com.bogdan801.additionalpoints.presentation.theme.AdditionalPointsTheme
import kotlinx.coroutines.launch
import com.bogdan801.additionalpoints.R
import com.bogdan801.additionalpoints.presentation.custom.composable.*
import com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox.CreateGroupDialog
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.DrawerMenuItem
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.MenuDrawer

@Composable
fun GroupScreen(
    //navController: NavHostController? = null,
    viewModel: GroupViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            //top app bar
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
        drawerContent = {
            //side drawer
            MenuDrawer(
                headerIconPainter = painterResource(id = R.drawable.ic_nubip_foreground),
                headerTitle = "НУБІП. Додаткові бали"
            ){
                DrawerMenuItem(
                    description = "Головна",
                    iconImageVector = Icons.Default.Home,
                    iconTint = MaterialTheme.colors.secondary,
                    onItemClick = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
                DrawerMenuItem(
                    description = "Генерація звіту",
                    iconPainter = painterResource(id = R.drawable.baseline_description_24),
                    onItemClick = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
                DrawerMenuItem(
                    description = "Довідка",
                    iconImageVector = Icons.Default.Info,
                    onItemClick = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
            }
        }

    ) {
        //add group dialog box
        CreateGroupDialog(
            showDialogState = viewModel.showAddGroupDialogState,
            groupNameState = viewModel.newGroupNameState,
            onTextChanged = {
                viewModel.onGroupNameChanged(it)
            },
            onSaveClick = {
                if(viewModel.newGroupNameState.value.isNotBlank()){
                    viewModel.addNewGroup()
                    Toast.makeText(context, "Групу додано", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Назва групи не може бути порожньою", Toast.LENGTH_SHORT).show()
                }
            }
        )

        //contents of the screen
        Column(modifier = Modifier
            .fillMaxSize()
        ) {
            GroupSelector(
                data = viewModel.groupListState.value.map { it.name },
                onGroupSelected = { _, text ->
                    viewModel.updateStudentsList()
                    Toast.makeText(context, "${viewModel.selectedGroupIndexState.value} - $text", Toast.LENGTH_SHORT).show()
                },
                indexState = viewModel.selectedGroupIndexState,
                onAddGroupClick = {
                    viewModel.showAddGroupDialogState.value = true
                },
                onDeleteGroupClick = {
                    Toast.makeText(context, "${viewModel.groupListState.value[viewModel.selectedGroupIndexState.value].name} was deleted", Toast.LENGTH_SHORT).show()
                    viewModel.deleteSelectedGroup()
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