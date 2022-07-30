package com.bogdan801.additionalpoints.presentation.screens.group

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.bogdan801.additionalpoints.R
import com.bogdan801.additionalpoints.presentation.custom.composable.*
import com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox.AddGroupDialog
import com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox.AddStudentDialog
import com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox.DeleteGroupDialog
import com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox.SettingsDialog
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.DrawerMenuItem
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.MenuDrawer
import com.bogdan801.additionalpoints.presentation.navigation.Screen
import com.bogdan801.additionalpoints.presentation.theme.AdditionalPointsTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupScreen(
    navController: NavHostController,
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
                },
                action = {
                    IconButton(
                        onClick = {
                            viewModel.openSettingsDialog(context)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
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
                        navController.navigate(Screen.ReportScreen.route)
                    }
                )
                DrawerMenuItem(
                    description = "Довідка",
                    iconImageVector = Icons.Default.Info,
                    onItemClick = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        navController.navigate(Screen.InfoScreen.route)
                    }
                )
            }
        },
        floatingActionButton = {
            if(viewModel.groupListState.value.isNotEmpty()){
                FloatingActionButton(
                    modifier = Modifier.size(60.dp, 50.dp),
                    shape = RoundedCornerShape(15.dp),
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSecondary,
                    onClick = {
                        viewModel.onStudentNameChanged("")
                        viewModel.showAddStudentDialogState.value = true
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(y = 2.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center

    ) {
        //add group dialog box
        AddGroupDialog(
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
                    Toast.makeText(context, "Назва групи не може бути порожньою!", Toast.LENGTH_SHORT).show()
                }
            }
        )

        //delete group dialog box
        DeleteGroupDialog(
            showDialogState = viewModel.showDeleteGroupDialogState,
            onDeleteGroupClick = {
                Toast.makeText(context, "Групу ${viewModel.selectedGroup.name} видалено", Toast.LENGTH_SHORT).show()
                viewModel.deleteSelectedGroup()
            },
            onDeleteGroupActivitiesClick = {
                viewModel.deleteSelectedGroupActivities()
                Toast.makeText(context, "Всі бали групи ${viewModel.selectedGroup.name} видалено", Toast.LENGTH_SHORT).show()
            }
        )

        //add student dialog box
        AddStudentDialog(
            showDialogState = viewModel.showAddStudentDialogState,
            newStudentName = viewModel.newStudentNameState.value,
            onNameChanged = {
                viewModel.onStudentNameChanged(it)
            },
            isContractState = viewModel.isNewStudentContract,
            onSaveNewStudentClick = {
                if(viewModel.newStudentNameState.value.isNotBlank()){
                    viewModel.onSaveNewStudentClick()
                }
                else{
                    Toast.makeText(context, "ПІБ студента не може бути порожнім!", Toast.LENGTH_SHORT).show()
                }
            }
        )

        //settings dialog box
        SettingsDialog(
            showDialogState = viewModel.showSettingsDialogState,
            allowShiftActivities = viewModel.allowShiftState.value,
            borders = viewModel.yearBordersState.value,
            onCheckBoxClick = {viewModel.onAllowShiftSwitchClicked(context)},
            onSelectFirstSemesterStartClick = {viewModel.onSelectFirstSemesterStartClick(context)},
            onSelectFirstSemesterEndClick = {viewModel.onSelectFirstSemesterEndClick(context)},
            onSelectSecondSemesterStartClick = {viewModel.onSelectSecondSemesterStartClick(context)},
            onSelectSecondSemesterEndClick = {viewModel.onSelectSecondSemesterEndClick(context)}
        )


        //contents of the screen
        Column(modifier = Modifier
            .fillMaxSize()
        ) {
            GroupSelector(
                data = viewModel.groupListState.value.map { it.name },
                onGroupSelected = { index, _ ->
                    viewModel.selectNewGroup(index)
                },
                index = viewModel.selectedGroupIndexState.value,
                onAddGroupClick = {
                    viewModel.showAddGroupDialogState.value = true
                },
                onDeleteGroupClick = {
                    viewModel.showDeleteGroupDialogState.value = true
                },
                showButtons = true
            )
            Box(modifier = Modifier.fillMaxSize()){
                if(viewModel.groupListState.value.isNotEmpty()){
                    if(viewModel.selectedGroup.students!!.isNotEmpty()){
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ){
                            if(viewModel.budgetStudentsList.value.isNotEmpty()){
                                item {
                                    Text(modifier = Modifier.padding(8.dp), text = "БЮДЖЕТ")
                                }
                                items(viewModel.budgetStudentsList.value){ student ->
                                    StudentCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        studentFullName = student.fullName,
                                        value = student.valueSum.value,
                                        swipeableState = student.swipeableState,
                                        onCardClick = {
                                            navController.navigate(Screen.StudentScreen.withArgs("${student.studentID}"))
                                        },
                                        onDeleteStudentClick = {
                                            viewModel.deleteStudent(student.studentID)
                                        }
                                    )
                                }
                            }
                            if(viewModel.contractStudentsList.value.isNotEmpty()){
                                item {
                                    Text(modifier = Modifier.padding(8.dp), text = "КОНТРАКТ")
                                }
                                items(viewModel.contractStudentsList.value){ student ->
                                    StudentCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        studentFullName = student.fullName,
                                        value = student.valueSum.value,
                                        swipeableState = student.swipeableState,
                                        onCardClick = {
                                            navController.navigate(Screen.StudentScreen.withArgs("${student.studentID}"))
                                        },
                                        onDeleteStudentClick = {
                                            viewModel.deleteStudent(student.studentID)
                                        }
                                    )
                                }
                            }
                            item{
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                    else{
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Додайте студентів",
                            color = MaterialTheme.colors.secondaryVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else{
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Створіть групу",
                        color = MaterialTheme.colors.secondaryVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}