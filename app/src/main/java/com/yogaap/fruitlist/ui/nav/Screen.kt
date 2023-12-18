package com.yogaap.fruitlist.ui.nav

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DetailFruit : Screen("home/{fruitId}") {
        fun createRoute(fruitId: String) = "home/$fruitId"
    }
    object Profile : Screen("profile")
    object Favorite : Screen("favorite")
}