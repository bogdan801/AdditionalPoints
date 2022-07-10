package com.bogdan801.additionalpoints.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bogdan801.additionalpoints.presentation.screens.group.GroupScreen
import com.bogdan801.additionalpoints.presentation.screens.info.InfoScreen
import com.bogdan801.additionalpoints.presentation.screens.student.StudentScreen

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

        /*composable(
            route = Screen.GroupsScreen.route + "/{doesNeedUpdate}",
            arguments = listOf(
                navArgument("doesNeedUpdate"){
                    type = NavType.IntType
                }
            )
        ){
            GroupScreen(navController = navController)
        }*/

        composable(
            route = Screen.StudentScreen.route + "/{studentID}",
            arguments = listOf(
                navArgument("studentID"){
                    type = NavType.IntType
                }
            )
        ){
            StudentScreen(navController = navController)
        }

        composable(Screen.ReportScreen.route){
            //ReportScreen(navController = navController)
        }

        composable(Screen.InfoScreen.route){
            InfoScreen(navController = navController)
        }
    }
}