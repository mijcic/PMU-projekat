package rs.ac.bg.etf.projekat.navigation.sections

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import rs.ac.bg.etf.projekat.MainScreen1
import rs.ac.bg.etf.projekat.MainScreen2
import rs.ac.bg.etf.projekat.settings.SettingsPage
import rs.ac.bg.etf.projekat.data.MyViewModel

fun NavGraphBuilder.mainNavigation(navController: NavHostController,viewModel: MyViewModel) {
    composable("destinationMainScreen1") {
        MainScreen1(
            onDestinationLoginPage = { navController.navigate("destinationLoginPage") },
            onDestinationMainScreen2 = { navController.navigate("destinationMainScreen2") }
        )
    }
    composable("destinationMainScreen2") {
        MainScreen2(navController,viewModel)
    }
    composable("destinationSettingsPage") {
        SettingsPage(onClick = {
            navController.navigate("destinationLoginPage") {
                popUpTo("destinationMainScreen2") { inclusive = true }
            }
        })
    }
}