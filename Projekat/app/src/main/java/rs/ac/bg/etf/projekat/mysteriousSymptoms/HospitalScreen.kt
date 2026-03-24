package rs.ac.bg.etf.projekat.mysteriousSymptoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.mysteriousSymptoms.ui.HospitalNavigationEvent
import rs.ac.bg.etf.projekat.mysteriousSymptoms.ui.HospitalPage
import rs.ac.bg.etf.projekat.mysteriousSymptoms.viewModels.HospitalViewModel

@Composable
fun HospitalScreen(
    viewModel: HospitalViewModel = hiltViewModel(),
    myViewModel:MyViewModel = hiltViewModel(),
    onClickToPatient: () -> Unit,
    onClickToLocation: () -> Unit,
    onClickToEvidence: () -> Unit,
    onClickToMap: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        myViewModel.getAllDataMysteriousSymptoms()
    }

    LaunchedEffect(Unit) {

        viewModel.navigation
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { event ->
                when (event) {
                    HospitalNavigationEvent.ToPatient -> onClickToPatient()
                    HospitalNavigationEvent.ToLocation -> onClickToLocation()
                    HospitalNavigationEvent.ToEvidence -> onClickToEvidence()
                    HospitalNavigationEvent.ToMap -> onClickToMap()
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
