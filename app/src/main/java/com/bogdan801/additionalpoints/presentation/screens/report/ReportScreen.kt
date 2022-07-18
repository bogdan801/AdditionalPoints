package com.bogdan801.additionalpoints.presentation.screens.report

import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bogdan801.additionalpoints.R
import com.bogdan801.additionalpoints.data.excel.report.Degree
import com.bogdan801.additionalpoints.data.util.getUkrainianMonthName
import com.bogdan801.additionalpoints.presentation.custom.composable.*
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.DrawerMenuItem
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.MenuDrawer
import com.bogdan801.additionalpoints.presentation.navigation.Screen
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFWorkbook

@Composable
fun ReportScreen(
    navController: NavHostController,
    launcher: ActivityResultLauncher<String>,
    workbook: MutableState<XSSFWorkbook>,
    viewModel: ReportViewModel = hiltViewModel()
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
                        text = "Звіт",
                        style = MaterialTheme.typography.h2
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
                    onItemClick = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        navController.popBackStack(route = Screen.GroupsScreen.route, inclusive = false)
                    }
                )
                DrawerMenuItem(
                    description = "Генерація звіту",
                    iconPainter = painterResource(id = R.drawable.baseline_description_24),
                    iconTint = MaterialTheme.colors.secondary,
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
                        navController.navigate(Screen.InfoScreen.route)
                    }
                )
            }
        },
        floatingActionButton = {
            if(viewModel.groupListState.value.isNotEmpty()){
                if(viewModel.uniqueGroupMonthsState.value.isNotEmpty()){
                    if(viewModel.isMonthSelected()){
                        CustomButton(
                            modifier = Modifier.height(45.dp),
                            onClick = {
                                if(viewModel.generalState.value){
                                    if(
                                        viewModel.courseState.value.isNotBlank()      &&
                                        viewModel.facultyState.value.isNotBlank()     &&
                                        viewModel.headOfGroupState.value.isNotBlank() &&
                                        viewModel.curatorState.value.isNotBlank()
                                    ){
                                        viewModel.onGenerateReportClick(launcher, workbook)
                                    }
                                    else{
                                        Toast.makeText(context, "Спочатку заповніть всі поля!", Toast.LENGTH_LONG).show()
                                    }
                                }
                                else{
                                    viewModel.onGenerateReportClick(launcher, workbook)
                                }
                            }
                        ) {
                            Text(text = "Згенерувати", color = MaterialTheme.colors.onSecondary)
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ){
        Column(modifier = Modifier.fillMaxSize()){
            GroupSelector(
                data = viewModel.groupListState.value.map { it.name },
                onGroupSelected = { index, _ ->
                    viewModel.selectGroup(index)
                },
                index = viewModel.selectedGroupIndexState.value,
                showButtons = false
            )
            if(viewModel.groupListState.value.isNotEmpty()){
                if(viewModel.uniqueGroupMonthsState.value.isNotEmpty()){
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        text = "Оберіть місяці",
                        color = MaterialTheme.colors.secondaryVariant,
                        textAlign = TextAlign.Center
                    )
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        columns = GridCells.Adaptive(150.dp)
                    ){
                        items(viewModel.uniqueGroupMonthsState.value){ month ->
                            val state = viewModel.monthsStatesMap[month]!!
                            CustomButton(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .height(45.dp),
                                onClick = {
                                    state.value = !state.value
                                },
                                isOutlined = true
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = getUkrainianMonthName(month).lowercase(),
                                        color = MaterialTheme.colors.secondaryVariant
                                    )
                                    Checkbox(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .offset(x = 15.dp),
                                        checked = state.value,
                                        onCheckedChange = {
                                            state.value = !state.value
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colors.primary
                                        )
                                    )
                                }
                            }
                        }

                        item {
                            CustomButton(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .height(45.dp),
                                onClick = {
                                    viewModel.switchGeneralCheckbox()
                                },
                                isOutlined = true
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = "загальний",
                                        color = MaterialTheme.colors.secondaryVariant
                                    )
                                    Checkbox(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .offset(x = 15.dp),
                                        checked = viewModel.generalState.value,
                                        onCheckedChange = {
                                            viewModel.switchGeneralCheckbox()
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colors.primary
                                        )
                                    )
                                }
                            }
                        }
                    }

                    AnimatedVisibility(visible = viewModel.generalState.value) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 4.dp),
                            elevation = 8.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(
                                        rememberScrollState()
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(start = 4.dp),
                                        text = "Освітній ступінь",
                                        style = MaterialTheme.typography.h2,
                                        color = MaterialTheme.colors.secondaryVariant
                                    )
                                    CustomDropDownMenu(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .height(45.dp),
                                        data = Degree.values().map{ it.degreeName },
                                        index = viewModel.degreeIndexState.value,
                                        onItemSelected = { index, _ ->
                                            viewModel.degreeIndexSelected(index)
                                        }
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 4.dp),
                                        text = "Курс",
                                        style = MaterialTheme.typography.h2,
                                        color = MaterialTheme.colors.secondaryVariant
                                    )
                                    CustomTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .height(45.dp),
                                        value = viewModel.courseState.value,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        onValueChanged = {
                                            viewModel.courseTextChanged(it)
                                        },
                                        placeholder = "Курс"
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 4.dp),
                                        text = "Факультет",
                                        style = MaterialTheme.typography.h2,
                                        color = MaterialTheme.colors.secondaryVariant
                                    )
                                    CustomTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .height(45.dp),
                                        value = viewModel.facultyState.value,
                                        onValueChanged = {
                                            viewModel.facultyTextChanged(it)
                                        },
                                        placeholder = "Факультет"
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 4.dp),
                                        text = "Прізвище та ініціали старости",
                                        style = MaterialTheme.typography.h2,
                                        color = MaterialTheme.colors.secondaryVariant
                                    )
                                    CustomTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .height(45.dp),
                                        value = viewModel.headOfGroupState.value,
                                        onValueChanged = {
                                            viewModel.headOfGroupTextChanged(it)
                                        },
                                        placeholder = "ПІБ"
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 4.dp),
                                        text = "Прізвище та ініціали куратора",
                                        style = MaterialTheme.typography.h2,
                                        color = MaterialTheme.colors.secondaryVariant
                                    )
                                    CustomTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .height(45.dp),
                                        value = viewModel.curatorState.value,
                                        onValueChanged = {
                                            viewModel.curatorTextChanged(it)
                                        },
                                        placeholder = "ПІБ"
                                    )
                                    Spacer(modifier = Modifier.height(65.dp))
                                }

                            }
                        }
                    }





                }
                else{
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(
                            text = "Записів не додано",
                            color = MaterialTheme.colors.secondaryVariant
                        )
                    }
                }
            }
            else{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(
                        text = "Груп не додано",
                        color = MaterialTheme.colors.secondaryVariant
                    )
                }
            }
        }
    }
}