package rs.ac.bg.etf.projekat.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.data.realm.DokazR
import rs.ac.bg.etf.projekat.data.realm.DokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeOsumnjicenogZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeSvedokaZadatakR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeOsumnjicenogR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeSvedokaR
import rs.ac.bg.etf.projekat.data.realm.PorukeZadatakR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonZadatakR
import rs.ac.bg.etf.projekat.data.realm.ZadatakR
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest
import rs.ac.bg.etf.projekat.data.retrofit.models.MessageResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.ScorePageKorisnikResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
import rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinRequest
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val MyRepository: Repository
) : ViewModel() {

    private val _uiState= MutableStateFlow(UiStateZlocin())
    val uiState : StateFlow<UiStateZlocin> = _uiState

    private val _uiStatePostZlocin = MutableStateFlow(UiStatePostZlocin())
    val uiStatePostZlocin : StateFlow<UiStatePostZlocin> = _uiStatePostZlocin

    fun insertDataZlocin(zlocin: ZlocinRequest)=viewModelScope.launch {
        try {
            val response = MyRepository.insertData(zlocin)
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
            val response = MyRepository.signUp(korisnik)
            _uiStateSignUp.value = UiStateSignUp(message = response, isRefreshing = false)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateSignUp.value = UiStateSignUp(message = null, isRefreshing = false, error = e.localizedMessage)
        }
    }

    private val _uiStateScoreKorisnika= MutableStateFlow(UiStateScoreKorisnika())
    val uiStateScoreKorisnika : StateFlow<UiStateScoreKorisnika> = _uiStateScoreKorisnika

    fun scoreKorisnika() = viewModelScope.launch {
        Log.d("SCORE","ovde")
        try {
            val response = MyRepository.scoreKorisnika()
            Log.d("SCORE",response.toString())
            _uiStateScoreKorisnika.value = UiStateScoreKorisnika(scoreList = response, isRefreshing = false)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateScoreKorisnika.value = UiStateScoreKorisnika(scoreList= emptyList(), isRefreshing = false, error = e.localizedMessage)
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

    private val _uiStateCntEvidence = MutableStateFlow(UiStateCntEvidence())
    val uiStateCntEvidence : StateFlow<UiStateCntEvidence> = _uiStateCntEvidence

    private val _uiStateCntForensicEvidence = MutableStateFlow(UiStateCntForensicEvidence())
    val uiStateCntForensicEvidence : StateFlow<UiStateCntForensicEvidence> = _uiStateCntForensicEvidence

    fun cntIncrement(cnt: Int) =viewModelScope.launch {
        try {
            _uiStateCntEvidence.value = UiStateCntEvidence(cnt=cnt+1)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateCntEvidence.value = UiStateCntEvidence(cnt=0)
        }
    }

    fun cntForensicIncrement(cnt: Int) =viewModelScope.launch {
        try {
            _uiStateCntForensicEvidence.value = UiStateCntForensicEvidence(forensicCnt = cnt+1)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateCntForensicEvidence.value = UiStateCntForensicEvidence(forensicCnt = 0)
        }
    }

    private val _uiStateForensicEvidence = MutableStateFlow(UiStateForensicEvidences())
    val uiStateForensicEvidence : StateFlow<UiStateForensicEvidences> = _uiStateForensicEvidence

    fun getForensicEvidences() = viewModelScope.launch {
        try {
            val response = selectForensicEvidences()
            val response2 =selectForensicEvidencesTasks(response)
            _uiStateForensicEvidence.value = UiStateForensicEvidences(response,response2)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateForensicEvidence.value = UiStateForensicEvidences(emptyList(), emptyList())
        }
    }

    fun updateEvidenceAndEvidenceTask(zadatakDokaz: DokazZadatakR) = viewModelScope.launch {
        zadatakDokaz.zadatakId?.idZadatak?.let { updateDokazZadatakAndZadatak(it,zadatakDokaz.idDokazZadatak) }
    }

    fun updateForensicEvidenceAndForensicEvidenceTask(zadatakDokaz: ForenzickiDokazZadatakR) = viewModelScope.launch {
        zadatakDokaz.zadatakId?.idZadatak?.let { updateForenzickiDokazZadatakAndZadatak(it,zadatakDokaz.idForenzickiDokazZadatak) }
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

    fun updatePorukeTask(poruke: PorukeZadatakR) = viewModelScope.launch {
        poruke.zadatakId?.idZadatak?.let { updatePorukeZadatak(poruke.idPorukeZadatak, it) }
    }

    private val _uiSteteSelectedAnswers = MutableStateFlow(UiSteteSelectedAnswers())
    val uiSteteSelectedAnswers : StateFlow<UiSteteSelectedAnswers> = _uiSteteSelectedAnswers

    fun updateSelectedanswes(answers:Map<Int, Int?> ) = viewModelScope.launch {
        Log.d("ANSWERS",answers.toString())
        try {
            _uiSteteSelectedAnswers.value = UiSteteSelectedAnswers(answers)
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiSteteSelectedAnswers.value = UiSteteSelectedAnswers(emptyMap())
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

data class UiStateForensicEvidences(
    val forensicEvidences: List<ForenzickiDokazR> = emptyList(),
    val forensicEvidencesTasks: List<ForenzickiDokazZadatakR> = emptyList()
)

data class UiStateCntEvidence(
    val cnt: Int =0
)

data class UiStateCntForensicEvidence(
    val forensicCnt: Int=0
)

data class UiSteteSelectedAnswers(
    var selectedAnswers: Map<Int, Int?>? = emptyMap()
)

// retrofit

data class UiStateScoreKorisnika(
    val scoreList: List<ScorePageKorisnikResponse>?= emptyList(),
    val isRefreshing: Boolean = false,
    val error: String? = null
)