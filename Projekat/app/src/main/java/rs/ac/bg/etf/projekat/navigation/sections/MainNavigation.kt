package rs.ac.bg.etf.projekat.navigation.sections

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import rs.ac.bg.etf.projekat.MainScreen1
import rs.ac.bg.etf.projekat.MainScreen2
import rs.ac.bg.etf.projekat.SettingsPage


fun NavGraphBuilder.mainNavigation(navController: NavHostController) {
    composable("destinationMainScreen1") {
        MainScreen1(navController)
    }
    composable("destinationMainScreen2") {
        MainScreen2(navController)
    }
    composable("destinationSettingsPage") {
        SettingsPage(navController)
    }
}