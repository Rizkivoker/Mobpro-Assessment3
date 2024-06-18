package org.d3if3168.appassessment3.system.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if3168.appassessment3.ui.screen.BaseApp
import org.d3if3168.appassessment3.ui.screen.DetailScreen
import java.lang.reflect.Modifier

@Composable
fun NavigationGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "Base"
    ) {

        composable(
            route = "Base"
        ) { navBackStackEntry ->
            BaseApp(navController, onNavigateToScreen = { nama, alamat, nomor ->
                navController.navigate("DetailScreen/$nama/$alamat/$nomor")
            })
        }

        // ini adalah navigasi ke IsiBuku dari InputScreen dengan mengirimkan paramater
        composable(
            route = "DetailScreen/{nama}/{alamat}/{nomor}",
            arguments = listOf(
                navArgument("nama") { type = NavType.StringType },
                navArgument("alamat") { type = NavType.StringType },
                navArgument("nomor") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nama = backStackEntry.arguments?.getString("nama") ?: ""
            val alamat = backStackEntry.arguments?.getString("alamat") ?: ""
            val nomor = backStackEntry.arguments?.getString("nomor") ?: ""

            DetailScreen(nama = nama, alamat = alamat, nomor = nomor, navController)
        }


    }
}