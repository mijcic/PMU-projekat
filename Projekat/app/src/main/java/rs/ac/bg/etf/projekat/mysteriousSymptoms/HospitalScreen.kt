package rs.ac.bg.etf.projekat.mysteriousSymptoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import rs.ac.bg.etf.projekat.mysteriousSymptoms.ui.HospitalNavigationEvent
import rs.ac.bg.etf.projekat.mysteriousSymptoms.ui.HospitalPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.viewModels.HospitalViewModel

@Composable
fun HospitalScreen(
    navController: NavController,
    viewModel: HospitalViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // Reaguj na navigacione događaje iz ViewModel-a
    LaunchedEffect(Unit) {
        viewModel.navigation
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    HospitalNavigationEvent.ToPatient -> navController.navigate("destinationPatientPage")
                    HospitalNavigationEvent.ToLocation -> navController.navigate("destinationLocationPage")
                    HospitalNavigationEvent.ToEvidence -> navController.navigate("destinationEvidencePage")
                    HospitalNavigationEvent.ToMap -> navController.navigate("destinationMapPage")
                }
            }
    }

    HospitalPage(
        onPatientClick = viewModel::onPatientClick,
        onLocationClick = viewModel::onLocationClick,
        onEvidenceClick = viewModel::onEvidenceClick,
        onTaskClick = viewModel::onTaskClick
    )
}
