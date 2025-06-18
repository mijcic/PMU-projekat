package rs.ac.bg.etf.projekat.navigation.sections

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import rs.ac.bg.etf.projekat.auth.LoginPage
import rs.ac.bg.etf.projekat.auth.SignUpPage

fun NavGraphBuilder.authNavigation(navController: NavHostController) {
    composable("destinationLoginPage") {
        LoginPage(navController)
    }
    composable("destinationSignUpPage") {
        SignUpPage(navController)
    }
}