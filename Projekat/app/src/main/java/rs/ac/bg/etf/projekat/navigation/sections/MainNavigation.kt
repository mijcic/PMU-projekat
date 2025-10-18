package rs.ac.bg.etf.projekat.navigation.sections

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import rs.ac.bg.etf.projekat.MainScreen1
import rs.ac.bg.etf.projekat.MainScreen2
import rs.ac.bg.etf.projekat.SettingsPage
import rs.ac.bg.etf.projekat.data.MyViewModel


fun NavGraphBuilder.mainNavigation(navController: NavHostController,viewModel: MyViewModel) {
    composable("destinationMainScreen1") {
        MainScreen1(navController)
    }
    composable("destinationMainScreen2") {
        MainScreen2(navController,viewModel)
    }
    composable("destinationSettingsPage") {
        SettingsPage(navController)
    }
}