package com.bogdan801.additionalpoints.presentation.screens.student

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bogdan801.additionalpoints.R
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomTopAppBar
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.DrawerMenuItem
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.MenuDrawer
import com.bogdan801.additionalpoints.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun StudentScreen(
    navController: NavHostController,
    //selectedStudentID: Int = -1,
    viewModel: StudentViewModel = hiltViewModel()
) {
    //val context = LocalContext.current
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
                        //navController.navigate(Screen.ReportScreen.route)
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
        }
    ){

    }
}