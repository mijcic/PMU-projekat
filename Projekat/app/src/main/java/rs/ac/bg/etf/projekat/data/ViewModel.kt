package rs.ac.bg.etf.projekat.data

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import rs.ac.bg.etf.projekat.data.realm.DokazR
import rs.ac.bg.etf.projekat.data.realm.DokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeOsumnjicenogZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeSvedokaZadatakR
import rs.ac.bg.etf.projekat.data.realm.MotivR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeOsumnjicenogR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeSvedokaR
import rs.ac.bg.etf.projekat.data.realm.PorukeZadatakR
import rs.ac.bg.etf.projekat.data.realm.StatusZrtvaR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonZadatakR
import rs.ac.bg.etf.projekat.data.realm.TipDokazaR
import rs.ac.bg.etf.projekat.data.realm.TipOsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import rs.ac.bg.etf.projekat.data.realm.ZadatakR
import rs.ac.bg.etf.projekat.data.realm.ZlocinR
import rs.ac.bg.etf.projekat.data.realm.ZrtvaR
import rs.ac.bg.etf.projekat.data.realm.stZlocinR
import rs.ac.bg.etf.projekat.data.retrofit.models.GeminiResponse2
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest
import rs.ac.bg.etf.projekat.data.retrofit.models.MessageResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.ScorePageKorisnikResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
import rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinRequest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
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


    //gemini

    private val _uiStateGeminiData = MutableStateFlow(UiStateGeminiData())
    val uiStateGeminiData : StateFlow<UiStateGeminiData> = _uiStateGeminiData

    fun getGeminiData(realmViewModel: RealmViewModel) = viewModelScope.launch {

        val jsonString = """

  {
    "prompt": "Smisli priču za detektivsku aplikaciju o ubistvu. Popuni sve podatke u tabelama vezanim za zločin i osobe. Podaci treba da obuhvate: naziv zločina, datum, mesto, opis, status zločina, ime, kontakt, zamimanje, pol. Tip osumnjicenog moze biti samo pojedinac ili organizacija. Tip dokaza moze biti fizicki, digitalni ili svedok. statusSvedok moze biti 'aktivno', 'zasticen', 'nesaradnja'.  tipForenzickiDokaz moze biti 'otisak', 'DNK', 'dokument'.  os moze biti 'IOS' ili 'Android'. Koristi sledeće tabele za popunjavanje podataka. Ali odgovor napisi samo u json obliku i ne ubacuj dodatne []. Minimalna vrednost za id je 1. Zlocin postoji samo jedan, ne vracaj listu. Nemoj stavljati null vrednosti. Osumnjiceni su lista",
    "tables": {
      "zlocinR": {
        "idZlocin": 0,
        "tipZlocinaId": 0,
        "naziv": "",
        "datum": null,
        "mesto": "",
        "opis": "",
        "status": "u_istrazi"
      },
      "zrtvaR": {
        "idZrtva": 0,
        "tipZrtve": "",
        "detalji": "",
        "statusZrtva": "ziva",
        "zlocinId": 0,
        "osobaId": {
          "idOsoba": 0,
          "ime": "",
          "kontakt": "",
          "datum": null,
          "zanimanje": "",
          "pol": "muski",
          "zlocinId": 0
        }
      },
      "osumnjicenR": [{
        "idOsumnjicen": 0,
        "status": 0,
        "tipOsumnjicen": "",
        "motiv": {
            "idMotiv":0,
  	      "opis":""
        },
        "zlocinId": 0,
        "kriv": 0,
        "osobaId": {
          "idOsoba": 0,
          "ime": "",
          "kontakt": "",
          "datum": "",
          "zanimanje": "",
          "pol": "muski",
          "zlocinId": 0
        }
      }],
      "dokazR":[{
          "idDokaz":0,
          "tipDokaza":"",
          "opis":"",
          "zlocinId":0,
          "zrtvaId":0,
          "status":0
      }],
      "svedokR":[{
        "idSvedok": 0,
        "izjava": "",
        "statusSvedok": "",
        "statusIspitan":0,
        "zlocinId": 0,
        "osobaId": {
          "idOsoba": 0,
          "ime": "",
          "kontakt": "",
          "datum": "",
          "zanimanje": "",
          "pol": "muski",
          "zlocinId": 0
        }
      }],
      "obdukcijaR":{
          "idObdukcija":0,
          "izvestaj":"",
          "datum":"",
          "uzrokSmrti":"",
          "zrtvaId":0,
          "informacije":""
      },
      "forenzickiDokazR":[{
          "idForenzickiDokaz":0,
          "tipForenzickiDokaz":"",
          "opis":"",
          "statusS":0,
          "veza":""
      }],
      "telefonR":[{
          "idTelefon":0,
          "model":"",
          "os":"",
          "sifra":"",
          "informacije":""
      }],
      "dokazOsumnjicenR":[{
        "idDokazOsumnjicen":0,
        "dokazId":0,
        "osumnjicenId":0
      }],
      "zadatakR":{
        "idZadatak":0,
        "tekst":"ispitaj ko je poslao pretecu poruku",
        "korak":"pogledaj telefon zrtve",
        "uradjen":false,
        "next":{
            "idZadatak": 0,
            "tekst": "",
            "korak": "",
            "uradjen": false,
            "next": null,
            "zlocinId": 0
        },
        "zlocinId":0
      },
      "kontaktKtor":[{
        "idKontakt":0,
        "ime":"",
        "broj":"",
        "status":0,
        "zrtvaId":0
       }]
    }
  }

""".trimIndent()

        val requestBody = jsonString.toRequestBody("application/json".toMediaType())
        try {
            val response=MyRepository.geminiData(requestBody)
            Log.d("GEMINI",response.toString())
            _uiStateGeminiData.value = UiStateGeminiData(response)

            val tipZlocina: TipZlocinaR? = realmViewModel.inserTipZlocina("Murder")
            Log.d("GEMINI ZLOCIN",response.zlocinR.toString())

            val zlocin: ZlocinR? = realmViewModel.insertZlocin(
                tipZlocina,
                response.zlocinR.naziv,
                response.zlocinR.datum.toString(),
                response.zlocinR.mesto,
                response.zlocinR.opis,
                response.zlocinR.status
            )



            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDateZrtva = LocalDate.parse(response.zrtvaR.osobaId?.datum ?: "2021-01-01", formatter)
            val instantZrtva = localDateZrtva.atStartOfDay(ZoneOffset.UTC).toInstant()
            val realmInstantZrtva = RealmInstant.from(instantZrtva.epochSecond, instantZrtva.nano)
            val zrtva: ZrtvaR? = response.zrtvaR.osobaId?.let {
                response.zrtvaR.osobaId.kontakt?.let { it1 ->
                    realmViewModel.insertZrtva(
                        response.zrtvaR.tipZrtve,
                        it.ime,
                        response.zrtvaR.detalji,
                        response.zrtvaR.statusZrtva,
                        zlocin,
                        it1,
                        realmInstantZrtva,
                        response.zrtvaR.osobaId.zanimanje,
                        polZ = response.zrtvaR.osobaId.pol
                    )
                }
            }

            var osumnjiceni:MutableList<OsumnjicenR> = mutableListOf()
            for (o in response.osumnjicenR){
                val m=o.motiv?.let { realmViewModel.insertMotiv(it.opis) }
                val localDateOsumnjiceni = LocalDate.parse(o.osobaId?.datum ?: "2021-01-01", formatter)
                val instantOsumnjiceni = localDateOsumnjiceni.atStartOfDay(ZoneOffset.UTC).toInstant()
                val realmInstantOsumnjiceni = RealmInstant.from(instantOsumnjiceni.epochSecond, instantOsumnjiceni.nano)

                var os=o.osobaId?.kontakt?.let {
                    o.osobaId?.let { it1 ->
                        realmViewModel.insertOsumnjiceni(
                            imeO = it1.ime,
                            statusO = o.status,
                            tipOsumnjicenO = o.tipOsumnjicen,
                            motivO = m,
                            zlocinO = zlocin,
                            krivO = o.kriv,
                            kontaktO = it,
                            datumO = realmInstantOsumnjiceni,
                            zanimanjO = o.osobaId.zanimanje,
                            polO = o.osobaId.pol
                        )
                    }
                }
                if (os != null) {
                    osumnjiceni.add(os)
                }
            }

            var dokazi:MutableList<DokazR> =mutableListOf()
            for(d in response.dokazR){
                val dk=realmViewModel.insertDokaz(
                    d.tipDokaza,
                    d.opis,
                    zlocin,
                    zrtva,
                    d.status
                )
                if (dk != null) {
                    dokazi.add(dk)
                }
            }

            for(s in response.svedokR){
                val localDateSvedok = LocalDate.parse(s.osobaId?.datum ?: "2021-01-01", formatter)
                val instantSvedok = localDateSvedok.atStartOfDay(ZoneOffset.UTC).toInstant()
                val realmInstantSvedok = RealmInstant.from(instantSvedok.epochSecond, instantSvedok.nano)

                s.osobaId?.let {
                    s.osobaId.kontakt?.let { it1 ->
                        realmViewModel.insertSvedok(
                            imeS = it.ime,
                            kontaktS = it1,
                            izjavaS = s.izjava,
                            zlocinS = zlocin,
                            statusSvedokS = s.statusSvedok,
                            statusIspitanS = s.statusIspitan,
                            datumS = RealmInstant.now(),
                            zanimanjS = s.osobaId.zanimanje,
                            polS = s.osobaId.pol
                        )
                    }
                }
            }

            for(f in response.forenzickiDokazR){
                realmViewModel.insertForenzickiDokaz(
                    tipFD = f.tipForenzickiDokaz,
                    opisFD = f.opis,
                    statusFD = f.statusS,
                    zrtvaFD = zrtva,
                    vezaFD = f.veza
                )
            }

            realmViewModel.insertObdukcija(
                izvestajO = response.obdukcijaR.izvestaj,
                datumO = response.obdukcijaR.datum,
                uzrokSmrtiO = response.obdukcijaR.uzrokSmrti,
                zrtvaO = zrtva,
                informacijeO = response.obdukcijaR.informacije
            )

            for(t in response.telefonR){
                realmViewModel.insertTelefon(
                    modelT = t.model,
                    osT = t.os,
                    zrtvaT = zrtva,
                    sifraT = t.sifra
                )
            }


            //izmeniti
            for(d in response.dokazOsumnjicenR){
                Log.d("GEMINI dokazOsumnjicen dokazi",dokazi.toString())
                Log.d("GEMINI dokazOsumnjicen d",d.toString())

                val dokaz = dokazi.find { it.idDokaz == d.dokazId }
                val osumnjicen = osumnjiceni.find { it.idOsumnjicen == d.osumnjicenId }

                Log.d("GEMINI dokazOsumnjicen dokaz",dokaz.toString())
                Log.d("GEMINI dokazOsumnjicen osumnjicen",osumnjicen.toString())
                if (dokaz != null) {
                    realmViewModel.insertDokazOsumnjicenog(
                        dokazIdDO = dokaz,
                        osumnjicenIdDO = osumnjicen
                    )
                } else {
                    // Nešto za slučaj da ne nađeš dokaz (logovanje, greška itd.)
                }
            }
            var z:rs.ac.bg.etf.projekat.data.retrofit.models.ZadatakR? = response.zadatakR
            while (z != null) {
                realmViewModel.insertZadatak(
                    tekstZ = z.tekst,
                    korakZ = z.korak,
                    uradjenZ = z.uradjen,
                    nextZ = null,
                    zlocinZ = zlocin
                )
                if(z.next!=null){
                    z = z.next!!
                }else{
                    z=null
                }
            }

            /*
            for(a in response.alibiR){
                realmViewModel.insertAlibi(
                    osumnjicenA = a.osumnjicenId,
                    svedokA = TODO(),
                    opisA = TODO(),
                    statusAlibijaA = TODO()
                )
            }*/

            for(k in response.kontaktKtor){
                realmViewModel.insertKontakt(k.ime, k.broj, k.status, zrtva)
            }

            realmViewModel.callGetTitleDatePlaceDescFromCrime()
        }
        catch (e:Exception){
            e.printStackTrace()
            _uiStateGeminiData.value = UiStateGeminiData(null)
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

//gemini

data class UiStateGeminiData(
    val geminiData: GeminiResponse2? =null
)