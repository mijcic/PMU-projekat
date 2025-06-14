package rs.ac.bg.etf.projekat.navigation.sections

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.mysteriousSymptoms.HospitalScreen
import rs.ac.bg.etf.projekat.mysteriousSymptoms.ui.HospitalPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.LekarskiTestPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.LocationPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.MedicalReportScreen
import rs.ac.bg.etf.projekat.mysteriousSymptoms.MedicalStatementPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.PatientScreen
import rs.ac.bg.etf.projekat.mysteriousSymptoms.viewModels.HospitalViewModel
import java.lang.reflect.Modifier


fun NavGraphBuilder.medicalNavigation(
    navController: NavHostController,
    viewModel: MyViewModel,
    realmViewModel: RealmViewModel,
    hospitalViewModel: HospitalViewModel
) {

    composable("destinationMedicalReportPage") {
        MedicalReportScreen(navController, viewModel)
    }
    composable("destinationPatientPage"){
        PatientScreen(navController,realmViewModel, viewModel)
    }
    composable("destinationMedicalStatementPage"){
        MedicalStatementPage(navController,realmViewModel,viewModel)
    }
    composable("destinationLekarskiTestPage"){
        LekarskiTestPage(viewModel)
    }
    composable("destinationHospitalPage") {
        HospitalScreen(navController = navController, viewModel = hospitalViewModel)
    }
    composable("destinationLocationPage"){
        LocationPage(navController,viewModel,realmViewModel)
    }
}