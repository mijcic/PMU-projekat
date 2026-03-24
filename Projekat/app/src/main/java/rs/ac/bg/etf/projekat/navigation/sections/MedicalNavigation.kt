package rs.ac.bg.etf.projekat.navigation.sections

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.mysteriousSymptoms.HospitalScreen
import rs.ac.bg.etf.projekat.mysteriousSymptoms.medicalTest.LekarskiTestPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.location.LocationPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.medicalReport.MedicalReportScreen
import rs.ac.bg.etf.projekat.mysteriousSymptoms.medicalStatement.MedicalStatementPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.patient.PatientScreen
import rs.ac.bg.etf.projekat.mysteriousSymptoms.viewModels.HospitalViewModel
import rs.ac.bg.etf.projekat.navigation.destinationLekarskiTestPage
import rs.ac.bg.etf.projekat.navigation.destinationMedicalReportPage
import rs.ac.bg.etf.projekat.navigation.destinationMedicalStatementPage
import rs.ac.bg.etf.projekat.navigation.destinationPhonePage


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
        PatientScreen(myViewModel =  viewModel,
            onDestinationMedicalReportPage= { navController.navigate(destinationMedicalReportPage.route) } ,
            onDestinationPhonePage = { navController.navigate(destinationPhonePage.route) },
            onDestinationMedicalStatementPage = { navController.navigate(destinationMedicalStatementPage.route) },
            onDestinationLekarskiTestPage = { navController.navigate(destinationLekarskiTestPage.route) }
        )
    }
    composable("destinationMedicalStatementPage"){
        MedicalStatementPage(myViewModel = viewModel)
    }
    composable("destinationLekarskiTestPage"){
        LekarskiTestPage(viewModel)
    }
    composable("destinationHospitalPage") {
        HospitalScreen(
            viewModel = hospitalViewModel,
            myViewModel = viewModel,
            onClickToPatient = {navController.navigate("destinationPatientPage")},
            onClickToLocation = { navController.navigate("destinationLocationPage") },
            onClickToEvidence = { navController.navigate("destinationEvidencePage")},
            onClickToMap = { navController.navigate("destinationMapPage") }
        )
    }
    composable("destinationLocationPage"){
        LocationPage(viewModel= viewModel, onBack = { navController.popBackStack() })
    }
}