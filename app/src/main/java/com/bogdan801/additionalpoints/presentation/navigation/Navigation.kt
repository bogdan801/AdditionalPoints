package com.bogdan801.additionalpoints.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bogdan801.additionalpoints.presentation.screens.group.GroupScreen
import com.bogdan801.additionalpoints.presentation.screens.info.InfoScreen

import com.google.accompanist.systemuicontroller.SystemUiController

/**
 * Функція управління навігацією додатку
 */
@Composable
fun Navigation(
    navController: NavHostController,
){
    NavHost(navController = navController, startDestination = Screen.GroupsScreen.route){
        composable(Screen.GroupsScreen.route){
            GroupScreen(navController = navController)
        }

        composable(
            route = Screen.StudentScreen.route + "/{studentID}",
            arguments = listOf(
                navArgument("studentID"){
                    type = NavType.IntType
                }
            )
        ){ entry ->
            //StudentScreen(navController = navController, selectedStudentID = entry.arguments!!.getInt("studentID"))
        }

        composable(Screen.ReportScreen.route){
            //ReportScreen(navController = navController)
        }

        composable(Screen.InfoScreen.route){
            InfoScreen(navController = navController)
        }
    }
}