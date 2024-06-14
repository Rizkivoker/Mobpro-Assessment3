package org.d3if3168.appassessment3.system.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import java.lang.reflect.Modifier

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Base.route
    ) {
            composable(route = Screen.Base.route) {

            }
    }
}