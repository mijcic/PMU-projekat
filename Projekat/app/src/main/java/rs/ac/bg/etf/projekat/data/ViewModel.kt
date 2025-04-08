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
import rs.ac.bg.etf.projekat.data.realm.DokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeOsumnjicenogZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeSvedokaZadatakR
import rs.ac.bg.etf.projekat.data.realm.KontaktR
import rs.ac.bg.etf.projekat.data.realm.MisijaPorukaR
import rs.ac.bg.etf.projekat.data.realm.MisijaR
import rs.ac.bg.etf.projekat.data.realm.MotivR
import rs.ac.bg.etf.projekat.data.realm.ObdukcijaR
import rs.ac.bg.etf.projekat.data.realm.OdnosOsumnjicenZrtvaR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeOsumnjicenogR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeSvedokaR
import rs.ac.bg.etf.projekat.data.realm.PorukeR
import rs.ac.bg.etf.projekat.data.realm.StatusAlibijaR
import rs.ac.bg.etf.projekat.data.realm.StatusPorukeR
import rs.ac.bg.etf.projekat.data.realm.StatusSvedokR
import rs.ac.bg.etf.projekat.data.realm.StatusZrtvaR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonR
import rs.ac.bg.etf.projekat.data.realm.TelefonZadatakR
import rs.ac.bg.etf.projekat.data.realm.TipDokazaR
import rs.ac.bg.etf.projekat.data.realm.TipForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.TipOdnosaR
import rs.ac.bg.etf.projekat.data.realm.TipOsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.TipPorukeR
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import rs.ac.bg.etf.projekat.data.realm.ZadatakR
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
            Log.d("SIGNUP", "ovde")
            val response = MyRepository.signUp(korisnik)
            Log.d("SIGNUP", response.toString())
            Log.d("SIGNUP", "Response: ${response.message}")
            _uiStateSignUp.value = UiStateSignUp(message = response, isRefreshing = false)
            Log.d("SIGNUP", "Response: ${_uiStateSignUp.value}")
        }
        catch (e:Exception){
            Log.d("SIGNUP", "greska")
            e.printStackTrace()
            _uiStateSignUp.value = UiStateSignUp(message = null, isRefreshing = false, error = e.localizedMessage)
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

    private val _uiStateZlocinData = MutableStateFlow(UiStateDataZlocin())
    val uiStateZlocinData : StateFlow<UiStateDataZlocin> = _uiStateZlocinData

    fun getAllDataZlocin() = viewModelScope.launch {
        try {
            val response = selectAllOsumnjiceni()
            val response2 = selectAllSvedoci()
            _uiStateZlocinData.value = UiStateDataZlocin(suspects = response, witnesses = response2)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateZlocinData.value = UiStateDataZlocin(suspects = emptyList(), witnesses =  emptyList())
        }
    }

    private val _uiStatePitanjaZaOsumnjicenog = MutableStateFlow(UiStatePitanjaZaOsumnjicenog())
    val uiStatePitanjaZaOsumnjicenog : StateFlow<UiStatePitanjaZaOsumnjicenog> = _uiStatePitanjaZaOsumnjicenog

    fun getPitanjaZaOsumnjicenog(osumnjicen:String) = viewModelScope.launch {
        try {
            val response1 = selectPitanjaByOsumnjicenAndCategory(osumnjicen,"opsta")
            val response2 = selectPitanjaByOsumnjicenAndCategory(osumnjicen,"alibi")
            val response3 = selectPitanjaByOsumnjicenAndCategory(osumnjicen,"dokaz")
            val response4 = selectPitanjaByOsumnjicenAndCategory(osumnjicen,"kontradikcija")
            _uiStatePitanjaZaOsumnjicenog.value = UiStatePitanjaZaOsumnjicenog(response1,response2,response3,response4)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStatePitanjaZaOsumnjicenog.value = UiStatePitanjaZaOsumnjicenog(emptyList(),emptyList(),emptyList(),emptyList())
        }
    }

    private val _uiStatePitanjaZaSvedoka = MutableStateFlow(UiStatePitanjaZaSvedoka())
    val uiStatePitanjaZaSvedoka : StateFlow<UiStatePitanjaZaSvedoka> = _uiStatePitanjaZaSvedoka

    fun getPitanjaZaSvedoka(svedok:String) = viewModelScope.launch {
        try {
            val response = selectPitanjaBySvedok(svedok)
            _uiStatePitanjaZaSvedoka.value = UiStatePitanjaZaSvedoka(response)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStatePitanjaZaSvedoka.value = UiStatePitanjaZaSvedoka(emptyList())
        }
    }

    private val _uiStateTasks = MutableStateFlow(UiStateTasks())
    val uiStateTasks : StateFlow<UiStateTasks> = _uiStateTasks

    fun getTasks() = viewModelScope.launch {
        try {
            val response = selectTasks()
            _uiStateTasks.value = UiStateTasks(response)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateTasks.value = UiStateTasks(emptyList())
        }
    }

    private val _uiStateEvidence = MutableStateFlow(UiStateEvidences())
    val uiStateEvidence : StateFlow<UiStateEvidences> = _uiStateEvidence

    fun getEvidences() = viewModelScope.launch {
        try {
            val response = selectEvidences()
            val response2 =selectEvidencesTasks(response)
            _uiStateEvidence.value = UiStateEvidences(response,response2)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateEvidence.value = UiStateEvidences(emptyList(), emptyList())
        }
    }

    fun updateEvidenceAndEvidenceTask(zadatakDokaz: DokazZadatakR) = viewModelScope.launch {
        zadatakDokaz.zadatakId?.idZadatak?.let { updateDokazZadatakAndZadatak(it,zadatakDokaz.idDokazZadatak) }
    }

    fun updateSuspectTask(zadatak: IspitivanjeOsumnjicenogZadatakR) = viewModelScope.launch {
        zadatak.zadatakId?.idZadatak?.let {
            updateIspitivanjeOsumnjicenogZadatak(zadatak.idIspitivanjeOsumnjicenogZadatak,
                it
            )
        }
    }

    fun updateWitnessTask(zadatak: IspitivanjeSvedokaZadatakR) = viewModelScope.launch {
        zadatak.zadatakId?.idZadatak?.let {
            updateIspitivanjeSvedokaZadatak(zadatak.idIspitivanjeSvedokaZadatak,
                it
            )
        }
    }

    fun updateTelefonTask(telefon: TelefonZadatakR) = viewModelScope.launch {
        telefon.zadatakId?.idZadatak?.let { updateTelefonZadatak(telefon.idTelefonZadatak, it) }
    }

}

data class UiStateZlocin(
    val zlocin: List<Zlocin> = emptyList()
)

data class UiStatePostZlocin(
    val message: MessageResponse?= null
)

data class UiStateSignUp(
    val message: MessageResponse?=null,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

data class UiStateLogIn(
    val message: MessageResponse? =null
)

data class UiStateDataZlocin(
    val suspects: List<OsumnjicenR> = emptyList(),
    val witnesses: List<SvedokR> = emptyList(),
)

data class UiStatePitanjaZaOsumnjicenog(
    val generalQuestions: List<PitanjeIspitivanjeOsumnjicenogR> = emptyList(),
    val alibiQuestions: List<PitanjeIspitivanjeOsumnjicenogR> = emptyList(),
    val evidenceQuestions: List<PitanjeIspitivanjeOsumnjicenogR> = emptyList(),
    val passingQuestions: List<PitanjeIspitivanjeOsumnjicenogR> = emptyList(),
)

data class UiStatePitanjaZaSvedoka(
    val questions: List<PitanjeIspitivanjeSvedokaR> = emptyList()
)

data class UiStateTasks(
    val tasks: List<ZadatakR> = emptyList()
)

data class UiStateEvidences(
    val evidences: List<DokazR> = emptyList(),
    val evidencesTasks: List<DokazZadatakR> = emptyList()
)