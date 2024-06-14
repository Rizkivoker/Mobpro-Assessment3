package org.d3if3168.appassessment3.system.navigation

sealed class Screen(val route: String) {
    data object Base : Screen("home")
}