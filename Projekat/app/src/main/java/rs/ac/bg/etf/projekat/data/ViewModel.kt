package rs.ac.bg.etf.projekat.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val MyRepository: Repository
) : ViewModel() {

    private val _uiState= MutableStateFlow(UiStateZlocin())
    val uiState : StateFlow<UiStateZlocin> = _uiState

    fun getAllData() = viewModelScope.launch {
        Log.d("GET ZLOCIN","getall")
        try {
            Log.d("GET ZLOCIN","pokusaj")
            val response = MyRepository.getZlocin()
            Log.d("GET ZLOCIN",response.toString())
            _uiState.value = UiStateZlocin(zlocin = response)
        }
        catch (e:Exception){
            Log.e("GET ZLOCIN", "Error: ${e.message}")
            e.printStackTrace()  // Ovo će ispisati punu stazu greške u logu
            _uiState.value = UiStateZlocin(zlocin = emptyList())
        }
    }
}

data class UiStateZlocin(
    val zlocin: List<Zlocin> = emptyList()
)