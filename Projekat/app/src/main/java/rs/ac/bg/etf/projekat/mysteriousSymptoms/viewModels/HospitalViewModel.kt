package rs.ac.bg.etf.projekat.mysteriousSymptoms.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.data.MyViewModel
import rs.ac.bg.etf.projekat.mysteriousSymptoms.ui.HospitalNavigationEvent
import rs.ac.bg.etf.projekat.navigation.destinationEvidencePage
import rs.ac.bg.etf.projekat.navigation.destinationLocationPage
import rs.ac.bg.etf.projekat.navigation.destinationMapPage
import rs.ac.bg.etf.projekat.navigation.destinationPatientPage
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
   // private val navController: NavController,
   // private val viewModel: MyViewModel //promeniti u repo
) : ViewModel() {

    private val _navigation = MutableSharedFlow<HospitalNavigationEvent>()
    val navigation = _navigation.asSharedFlow()


    fun onPatientClick() {
       // viewModel.getAllDataMysteriousSymptoms()
        viewModelScope.launch {
            //repository.loadAllData()
           // navController.navigate(destinationPatientPage.route)
            _navigation.emit(HospitalNavigationEvent.ToPatient)
        }
    }

    fun onLocationClick() {
        viewModelScope.launch {
          //  viewModel.getAllDataMysteriousSymptoms()
            //repository.loadAllData()
           // navController.navigate(destinationLocationPage.route)
            _navigation.emit(HospitalNavigationEvent.ToLocation)
        }
    }

    fun onEvidenceClick() {
     //   navController.navigate(destinationEvidencePage.route)
        viewModelScope.launch {
            _navigation.emit(HospitalNavigationEvent.ToEvidence)
        }
    }

    fun onTaskClick() {
        //viewModelScope.launch {
            //viewModel.getTasks()
            //repository.loadTasks()
         //   navController.navigate(destinationMapPage.route)
       // }
        viewModelScope.launch {

            _navigation.emit(HospitalNavigationEvent.ToMap)
        }
    }
}