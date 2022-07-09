package com.bogdan801.additionalpoints.presentation.navigation

sealed class Screen(val route: String) {
    object GroupsScreen  : Screen("groups" )
    object StudentScreen : Screen("student")
    object ReportScreen  : Screen("report" )
    object InfoScreen    : Screen("info"   )

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}