package com.bogdan801.additionalpoints.presentation.screens.student

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bogdan801.additionalpoints.R
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomTopAppBar
import com.bogdan801.additionalpoints.presentation.custom.composable.MonthTitle
import com.bogdan801.additionalpoints.presentation.custom.composable.StudentActivityCard
import com.bogdan801.additionalpoints.presentation.custom.composable.TotalStudentValueCard
import com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox.DeleteStudentDialog
import com.bogdan801.additionalpoints.presentation.custom.composable.dialogbox.StudentActivityDialog
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.DrawerMenuItem
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.MenuDrawer
import com.bogdan801.additionalpoints.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun StudentScreen(
    navController: NavHostController,
    viewModel: StudentViewModel = hiltViewModel()
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
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = viewModel.studentState.value.fullName,
                        style = MaterialTheme.typography.h2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                action = {
                    IconButton(
                        onClick = {
                            viewModel.showDeleteStudentDialogState.value = true
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete student")
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
                        navController.popBackStack()
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
            FloatingActionButton(
                modifier = Modifier
                    .size(60.dp, 50.dp)
                    .offset(y = (-60).dp),
                shape = RoundedCornerShape(15.dp),
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary,
                onClick = {
                    viewModel.onAddActivityDialogClick()
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
        },
        floatingActionButtonPosition = FabPosition.Center
    ){
        //add activity student dialog
        StudentActivityDialog(
            showDialogState = viewModel.showAddActivityDialogState,
            dialogType = viewModel.intention,
            description = viewModel.activityDescriptionState.value,
            onDescriptionChanged = {
                viewModel.onDescriptionTextChanged(it)
            },
            date = viewModel.selectedDateState.value,
            onDateSelectClick = {
                viewModel.onSelectDateClick(context)
            },
            activityInformationList = viewModel.activityInformationListState.value,
            index = viewModel.selectedActivityIndexState.value,
            onCheckBoxIndexChanged = { index, _ ->
                viewModel.selectActivityInformation(index)
            },
            value = viewModel.valueState.value,
            onValueChanged = {
                viewModel.onValueTextChange(it)
            },
            onSaveActivityClick = {
                if(viewModel.activityDescriptionState.value.isNotBlank() && viewModel.valueState.value.isNotBlank()){
                    viewModel.saveStudentActivity(viewModel.intention)
                    Toast.makeText(context, "Запис збережено", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Спочатку введіть дані!", Toast.LENGTH_SHORT).show()
                }
            }
        )

        //delete student dialog
        DeleteStudentDialog(
            showDialogState = viewModel.showDeleteStudentDialogState,
            onDeleteStudentClick = {
                Toast.makeText(context, "Студента видалено", Toast.LENGTH_SHORT).show()
                viewModel.deleteStudent()
                navController.popBackStack()
            }
        )

        Box(modifier = Modifier.fillMaxSize()){
            if(viewModel.studentState.value.activities?.isNotEmpty() == true){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 8.dp, end = 8.dp, bottom = 70.dp)
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    viewModel.monthStudentActivitiesMap.forEach { entry ->
                        MonthTitle(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            month = entry.key,
                            value = viewModel.getMonthSumValue(entry.key)
                        )

                        entry.value.forEach { activity ->
                            StudentActivityCard(
                                modifier = Modifier.padding(vertical = 4.dp),
                                activity = activity,
                                onActivityClick = {
                                    viewModel.onUpdateActivityDialogClick(activity)
                                },
                                onDeleteActivityClick = {
                                    viewModel.deleteActivityClick(activity.studActID)
                                    Toast.makeText(context, "Запис видалено", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(65.dp))
                }
            }
            else{
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Внесіть дані",
                    color = MaterialTheme.colors.secondaryVariant
                )
            }


            TotalStudentValueCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(8.dp),
                value = viewModel.totalStudentValue.value
            )
        }
    }
}