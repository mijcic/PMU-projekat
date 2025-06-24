package rs.ac.bg.etf.projekat.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import rs.ac.bg.etf.projekat.CultAndSectsPage
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.mysteriousSymptoms.viewModels.HospitalViewModel
import rs.ac.bg.etf.projekat.navigation.sections.authNavigation
import rs.ac.bg.etf.projekat.navigation.sections.mainNavigation
import rs.ac.bg.etf.projekat.navigation.sections.medicalNavigation
import rs.ac.bg.etf.projekat.navigation.sections.murderNavigation
import rs.ac.bg.etf.projekat.navigation.sections.phoneNavigation

@SuppressLint("NewApi")
@Composable
fun NavigationGraph(navController: NavHostController) {
    val viewModel: MyViewModel = hiltViewModel()
    val realmViewModel: RealmViewModel = hiltViewModel()
    val hospitalViewModel:HospitalViewModel = hiltViewModel()

    NavHost(navController = navController, //startDestination = "destinationMainScreen1"
//        startDestination = "destinationMainScreen2"
        startDestination = "destinationSignUpPage"
    ) {
        mainNavigation(navController)
        authNavigation(navController)
        murderNavigation(navController, viewModel, realmViewModel)
        phoneNavigation(navController)
        medicalNavigation(navController, viewModel, realmViewModel,hospitalViewModel)

        composable("destinationCultsAndSectsPage"){ CultAndSectsPage() }
    }
}