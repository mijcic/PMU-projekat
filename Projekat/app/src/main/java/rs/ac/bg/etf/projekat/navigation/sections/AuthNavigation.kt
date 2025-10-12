package rs.ac.bg.etf.projekat.navigation.sections

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import rs.ac.bg.etf.projekat.auth.LoginPage
import rs.ac.bg.etf.projekat.auth.SignUpPage
import rs.ac.bg.etf.projekat.data.MyViewModel

fun NavGraphBuilder.authNavigation(navController: NavHostController, viewModel: MyViewModel,) {
    composable("destinationLoginPage") {
        LoginPage(navController, viewModel)
    }
    composable("destinationSignUpPage") {
        SignUpPage(navController)
    }
}