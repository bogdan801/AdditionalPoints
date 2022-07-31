package com.bogdan801.additionalpoints.presentation.screens.info

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bogdan801.additionalpoints.R
import com.bogdan801.additionalpoints.presentation.custom.composable.ActivityInformationTable
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomTopAppBar
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.DrawerMenuItem
import com.bogdan801.additionalpoints.presentation.custom.composable.drawer.MenuDrawer
import com.bogdan801.additionalpoints.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun InfoScreen(
    navController: NavHostController,
    viewModel: InfoViewModel = hiltViewModel()
) {
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
                        text = stringResource(id = R.string.info_drawer_item),
                        style = MaterialTheme.typography.h2
                    )
                }
            )
        },
        drawerContent = {
            MenuDrawer(
                headerIconPainter = painterResource(id = R.drawable.ic_nubip_foreground),
                headerTitle = stringResource(id = R.string.title_of_drawer)
            ){
                DrawerMenuItem(
                    description = stringResource(id = R.string.main_drawer_item),
                    iconImageVector = Icons.Default.Home,
                    onItemClick = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        navController.popBackStack(route = Screen.GroupsScreen.route, inclusive = false)
                    }
                )
                DrawerMenuItem(
                    description = stringResource(id = R.string.report_drawer_item),
                    iconPainter = painterResource(id = R.drawable.baseline_description_24),
                    onItemClick = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        navController.navigate(Screen.ReportScreen.route)
                    }
                )
                DrawerMenuItem(
                    description = stringResource(id = R.string.info_drawer_item),
                    iconImageVector = Icons.Default.Info,
                    iconTint = MaterialTheme.colors.secondary,
                    onItemClick = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
            }
        }

    ) {
        ActivityInformationTable(
            data = viewModel.activityListState.value,
            headerBackgroundColor = MaterialTheme.colors.secondaryVariant,
            headerTextColor = MaterialTheme.colors.onPrimary,
            headerBorderWidth = 0.5.dp,
            headerBorderColor = MaterialTheme.colors.primary,
            contentBorderWidth = 0.5.dp,
            contentBorderColor = Color.Gray
        )
    }
}