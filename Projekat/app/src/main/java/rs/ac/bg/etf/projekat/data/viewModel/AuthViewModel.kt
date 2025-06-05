package rs.ac.bg.etf.projekat.data.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.data.Repository
import rs.ac.bg.etf.projekat.data.UiStateLogIn
import rs.ac.bg.etf.projekat.data.UiStateScoreKorisnika
import rs.ac.bg.etf.projekat.data.UiStateSignUp
import rs.ac.bg.etf.projekat.data.UiStateZlocin
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val MyRepository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateZlocin())
    val uiState: StateFlow<UiStateZlocin> = _uiState

    private val _uiStateSignUp = MutableStateFlow(UiStateSignUp())
    val uiStateSignUp: StateFlow<UiStateSignUp> = _uiStateSignUp

    fun signUp(korisnik: KorisnikRequest) = viewModelScope.launch {
        try {
            val response = MyRepository.signUp(korisnik)
            _uiStateSignUp.value = UiStateSignUp(message = response, isRefreshing = false)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateSignUp.value =
                UiStateSignUp(message = null, isRefreshing = false, error = e.localizedMessage)
        }
    }

    private val _uiStateScoreKorisnika = MutableStateFlow(UiStateScoreKorisnika())
    val uiStateScoreKorisnika: StateFlow<UiStateScoreKorisnika> = _uiStateScoreKorisnika

    fun scoreKorisnika() = viewModelScope.launch {
        Log.d("SCORE", "ovde")
        try {
            val response = MyRepository.scoreKorisnika()
            Log.d("SCORE", response.toString())
            _uiStateScoreKorisnika.value =
                UiStateScoreKorisnika(scoreList = response, isRefreshing = false)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateScoreKorisnika.value = UiStateScoreKorisnika(
                scoreList = emptyList(),
                isRefreshing = false,
                error = e.localizedMessage
            )
        }
    }

    private val _uiStateLogIn = MutableStateFlow(UiStateLogIn())
    val uiStateLogIn: StateFlow<UiStateLogIn> = _uiStateLogIn

    fun logIn(korisnik: KorisnikRequest) = viewModelScope.launch {
        try {
            val response = MyRepository.logIn(korisnik)
            _uiStateLogIn.value = UiStateLogIn(message = response)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateLogIn.value = UiStateLogIn(message = null)
        }
    }
}