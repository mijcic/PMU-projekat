package rs.ac.bg.etf.projekat.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.type.TimeZone
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import rs.ac.bg.etf.projekat.MainActivity.Companion.realm
import rs.ac.bg.etf.projekat.data.realm.AlibiR
import rs.ac.bg.etf.projekat.data.realm.DokazOsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.DokazR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.KontaktR
import rs.ac.bg.etf.projekat.data.realm.MisijaPorukaR
import rs.ac.bg.etf.projekat.data.realm.MisijaR
import rs.ac.bg.etf.projekat.data.realm.MotivR
import rs.ac.bg.etf.projekat.data.realm.ObdukcijaR
import rs.ac.bg.etf.projekat.data.realm.OdnosOsumnjicenZrtvaR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.PorukeR
import rs.ac.bg.etf.projekat.data.realm.StatusAlibijaR
import rs.ac.bg.etf.projekat.data.realm.StatusPorukeR
import rs.ac.bg.etf.projekat.data.realm.StatusSvedokR
import rs.ac.bg.etf.projekat.data.realm.StatusZrtvaR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonR
import rs.ac.bg.etf.projekat.data.realm.TipDokazaR
import rs.ac.bg.etf.projekat.data.realm.TipForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.TipOdnosaR
import rs.ac.bg.etf.projekat.data.realm.TipOsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.TipPorukeR
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import rs.ac.bg.etf.projekat.data.realm.ZlocinR
import rs.ac.bg.etf.projekat.data.realm.ZrtvaR
import rs.ac.bg.etf.projekat.data.realm.stZlocinR
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest
import rs.ac.bg.etf.projekat.data.retrofit.models.MessageResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
import rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinRequest
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val MyRepository: Repository
) : ViewModel() {

    private val _uiState= MutableStateFlow(UiStateZlocin())
    val uiState : StateFlow<UiStateZlocin> = _uiState

//    fun getAllData() = viewModelScope.launch {
//        Log.d("GET ZLOCIN","getall")
//        try {
//            Log.d("GET ZLOCIN","pokusaj")
//            val response = MyRepository.getZlocin()
//            Log.d("GET ZLOCIN",response.toString())
//            _uiState.value = UiStateZlocin(zlocin = response)
//        }
//        catch (e:Exception){
//            Log.e("GET ZLOCIN", "Error: ${e.message}")
//            e.printStackTrace()  // Ovo će ispisati punu stazu greške u logu
//            _uiState.value = UiStateZlocin(zlocin = emptyList())
//        }
//    }

    private val _uiStatePostZlocin = MutableStateFlow(UiStatePostZlocin())
    val uiStatePostZlocin : StateFlow<UiStatePostZlocin> = _uiStatePostZlocin

    fun insertDataZlocin(zlocin: ZlocinRequest)=viewModelScope.launch {
        try {
            val response = MyRepository.insertData(zlocin)
            Log.d("POST_ZLOCIN",response.toString())
            _uiStatePostZlocin.value = UiStatePostZlocin(message = response)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStatePostZlocin.value = UiStatePostZlocin(message = null)
        }
    }

    private val _uiStateSignUp = MutableStateFlow(UiStateSignUp())
    val uiStateSignUp : StateFlow<UiStateSignUp> = _uiStateSignUp

    fun signUp(korisnik: KorisnikRequest)=viewModelScope.launch {
        try {
            Log.d("SIGNUP", korisnik.toString())
            val response = MyRepository.signUp(korisnik)
            Log.d("SIGNUP", response.toString())
            _uiStateSignUp.value = UiStateSignUp(message = response)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateSignUp.value = UiStateSignUp(message = null)
        }
    }

    private val _uiStateLogIn = MutableStateFlow(UiStateLogIn())
    val uiStateLogIn : StateFlow<UiStateLogIn> = _uiStateLogIn

    fun logIn(korisnik: KorisnikRequest) = viewModelScope.launch {
        try {
            val response = MyRepository.logIn(korisnik)
            _uiStateLogIn.value = UiStateLogIn(message = response)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateLogIn.value = UiStateLogIn(message = null)
        }
    }
}

data class UiStateZlocin(
    val zlocin: List<Zlocin> = emptyList()
)

data class UiStatePostZlocin(
    val message: MessageResponse?= null
)

data class UiStateSignUp(
    val message: MessageResponse?=null
)

data class UiStateLogIn(
    val message: MessageResponse? =null
)