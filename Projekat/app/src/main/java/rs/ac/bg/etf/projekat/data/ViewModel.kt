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
import rs.ac.bg.etf.projekat.data.realm.ObdukcijaR
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
import rs.ac.bg.etf.projekat.data.retrofit.models.GeminiResponseRetrofit
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
  "prompt": "Smisli priču za detektivsku aplikaciju o ubistvu. Popuni sve podatke u tabelama kao u primeru koji dajem ispod, ali ne zelim da mi prica i podaci budu isti vec generisi neku novu pricu o ubistvu i na osnovu toga popuni tabele. Tip osumnjicenog moze biti samo pojedinac ili organizacija. Tip dokaza moze biti fizicki, digitalni ili svedok. statusSvedok moze biti 'aktivno', 'zasticen', 'nesaradnja'.  tipForenzickiDokaz moze biti 'otisak', 'DNK', 'dokument'.  os moze biti 'IOS' ili 'Android'. Mora da postoji samo jedan zlocinR, nemoj da mi pravis listu. Koristi sledeće tabele za popunjavanje podataka. Popuni mi sve tabele koje ti prosledim kao primer. Popuni mi i primere za tabelu zadatakR sa njenim poljima idZadatak, tekst, korak koji je tipa String, uradjen, next, zlocinId. Popuni mi i tabelu telefonZadatakR i obicnaPorukaR. Obavezno dodaj i jedan whatsAppKontaktR zrtve cije ce ime biti 'Me' i sa njim se obavlja komunikacija sa drugim objektima tipa whatsAppKontaktR. Obavezno dodaj i jedan oneContactR zrtve cije ce ime biti 'Me' i sa njim se obavlja komunikacija sa drugim objektima tipa oneContactR. Zelim da mi dodas vise od jednog objekta tipa whatsAppKontaktR. Popuni mi i tabelu whatsAppPorukaR. OBAVEZNO mi popuni i tabelu obicnaPorukaR. OBAVEZNO mi popuni i tabelu oneCallR. Nemoj da vracas null vrednosti za polja. Zlocin tabela  je jedna nije lista. Ali odgovor napisi samo u json obliku i ne ubacuj dodatne [].",
  "tables": {
  "zlocinR": {
    "idZlocin": 1,
    "tipZlocinaId": 1,
    "naziv": "Murder of Isabelle Moreau",
    "datum": "2025-04-17",
    "mesto": "Casino Hotel, Paris",
    "opis": "Isabelle Moreau, a high-profile gambler, was found dead in her hotel room with a knife wound. The investigation is ongoing.",
    "status": "u_istrazi"
  },
  "zrtvaR": {
    "idZrtva": 1,
    "tipZrtve": "Individual",
    "detalji": "Isabelle Moreau, a 32-year-old gambler known for her luxurious lifestyle and turbulent relationships, was found murdered in her hotel room.",
    "statusZrtva": "ziva",
    "zlocinId": 1,
    "osobaId": {
      "idOsoba": 1,
      "ime": "Isabelle Moreau",
      "kontakt": "+33612345678",
      "datum": "1993-04-12",
      "zanimanje": "Gambler",
      "pol": "zenski",
      "zlocinId": 1
    }
  },
  "osumnjicenR": [
    {
      "idOsumnjicen": 1,
      "status": 0,
      "tipOsumnjicen": "Pojedinac",
      "motiv": {
        "idMotiv": 1,
        "opis": "Financial struggles and jealousy."
      },
      "zlocinId": 1,
      "kriv": 0,
      "osobaId": {
        "idOsoba": 2,
        "ime": "Amelia Fontaine",
        "kontakt": "+33623456789",
        "datum": "1990-06-14",
        "zanimanje": "Casino Dealer",
        "pol": "zenski",
        "zlocinId": 1
      }
    },
    {
      "idOsumnjicen": 2,
      "status": 0,
      "tipOsumnjicen": "Pojedinac",
      "motiv": {
        "idMotiv": 2,
        "opis": "Financial problems linked to Isabelle's gambling habits."
      },
      "zlocinId": 1,
      "kriv": 0,
      "osobaId": {
        "idOsoba": 3,
        "ime": "Marco Bellini",
        "kontakt": "+33698765432",
        "datum": "1985-02-21",
        "zanimanje": "Gambler",
        "pol": "muski",
        "zlocinId": 1
      }
    }
  ],
  "dokazR": [
    {
      "idDokaz": 1,
      "tipDokaza": "fizicki",
      "opis": "A knife with blood traces found near the victim's room.",
      "zlocinId": 1,
      "zrtvaId": 1,
      "status": 0
    },
    {
      "idDokaz": 2,
      "tipDokaza": "digitalni",
      "opis": "Threatening messages found on Isabelle's phone.",
      "zlocinId": 1,
      "zrtvaId": 1,
      "status": 0
    }
  ],
  "svedokR": [
    {
      "idSvedok": 1,
      "izjava": "Amelia Fontaine was seen leaving Isabelle's room shortly before the body was discovered. She seemed anxious.",
      "statusSvedok": "aktivno",
      "statusIspitan": 0,
      "zlocinId": 1,
      "osobaId": {
        "idOsoba": 4,
        "ime": "Luc Moreau",
        "kontakt": "+33622334455",
        "datum": "1989-08-05",
        "zanimanje": "Hotel Staff",
        "pol": "muski",
        "zlocinId": 1
      }
    },
    {
      "idSvedok": 2,
      "izjava": "I overheard a heated argument between Isabelle and Marco, but I couldn't understand what was being said.",
      "statusSvedok": "aktivno",
      "statusIspitan": 0,
      "zlocinId": 1,
      "osobaId": {
        "idOsoba": 5,
        "ime": "Vincent Duval",
        "kontakt": "+33644455566",
        "datum": "1987-12-01",
        "zanimanje": "Casino Manager",
        "pol": "muski",
        "zlocinId": 1
      }
    }
  ],
  "obdukcijaR": {
    "idObdukcija": 1,
    "izvestaj": "The victim died from a single stab wound to the chest. There was also evidence of struggle before her death.",
    "datum": "2025-04-17",
    "uzrokSmrti": "Stab wound to the chest.",
    "zrtvaId": 1,
    "informacije": "No signs of sexual assault. The victim's hands showed defensive wounds."
  },
  "forenzickiDokazR": [
    {
      "idForenzickiDokaz": 1,
      "tipForenzickiDokaz": "DNK",
      "opis": "DNA traces found on the knife match those of Amelia Fontaine.",
      "statusS": 0,
      "veza": "The evidence strongly links Amelia Fontaine to the murder."
    }
  ],
  "telefonR": [
    {
      "idTelefon": 1,
      "model": "iPhone 12",
      "os": "IOS",
      "sifra": "123456",
      "informacije": "The phone showed messages between the victim and the suspects. Some were threatening in nature."
    },
    {
      "idTelefon": 2,
      "model": "Samsung Galaxy S20",
      "os": "Android",
      "sifra": "654321",
      "informacije": "The phone had records of Marco Bellini's calls with Isabelle the day before her death."
    }
  ],
  "oneContactR": [
    {
      "idOneContact": 1,
      "zlocinId": 1,
      "ime": "Marco Bellini",
      "broj": "+33698765432",
      "slika": 1
    },
    {
      "idOneContact": 2,
      "zlocinId": 1,
      "ime": "Amelia Fontaine",
      "broj": "+33623456789",
      "slika": 1
    }
  ],
  "beleskaR": [
    {
      "idBeleska": 1,
      "zlocinId": 1,
      "tekst": "Witnesses reported seeing Amelia Fontaine near the scene of the crime.",
      "datum": "2025-04-17"
    },
    {
      "idBeleska": 2,
      "zlocinId": 1,
      "tekst": "Security footage showed Marco Bellini near Isabelle's room earlier that evening.",
      "datum": "2025-04-17"
    }
  ],
  "whatsAppKontaktR": [
  {
    "idWhatsAppKontakt": 1,
    "zlocinId": 1,
    "ime": "Oliver Chase",
    "broj": "+12065559900",
    "slika": 1
  },
  {
    "idWhatsAppKontakt": 2,
    "zlocinId": 1,
    "ime": "Sophia Blake",
    "broj": "+12067771122",
    "slika": 1
  }],
  "whatsAppPorukaR": [
      {
        "idWhatsAppPoruka": 1,
        "kontaktKoSalje": 1,
        "kontaktKomeSalje": 2,
        "tekst": "Nathan was getting too close. We had to act.",
        "datum": "2025-04-17",
        "procitana": true
      },
      {
        "idWhatsAppPoruka": 2,
        "kontaktKoSalje": 2,
        "kontaktKomeSalje": 1,
        "tekst": "I hope nobody traces this back to us.",
        "datum": "2025-04-17",
        "procitana": false
      }
    ],
    "oneCallR": [
    {
      "idOneCall": 1,
      "kontakt": 1,
      "datum": "2025-04-17",
      "propusten": false,
      "dolazni": true
    },
    {
      "idOneCall": 2,
      "kontakt": 2,
      "datum": "2025-04-17",
      "propusten": true,
      "dolazni": false
    }
  ],
  "galleryR": [
  {
    "idPhoto": 1,
    "zlocinId": 1,
    "slika": 1,
    "datum": "2025-04-17",
    "mesto": "Casino Hotel, Paris"
  },
  {
    "idPhoto": 2,
    "zlocinId": 1,
    "slika": 2,
    "datum": "2025-04-17",
    "mesto": "Casino Hotel Lobby, Paris"
  }
],
"obicnaPorukaR": [
    {
      "idObicnaPoruka": 1,
      "kontaktKoSalje": 1,
      "kontaktKomeSalje": 2,
      "tekst": "Videli su me u hotelu. Sta da radim?",
      "datum": "2025-04-17",
      "procitana": true
    },
    {
      "idObicnaPoruka": 2,
      "kontaktKoSalje": 2,
      "kontaktKomeSalje": 1,
      "tekst": "Samo se pravi da ništa ne znaš. Sve će biti u redu.",
      "datum": "2025-04-17",
      "procitana": false
    }
  ],
  "prijavljeniKorisnikR": [
  {
    "idKorisnik": 1,
    "korisnickoIme": "detektiv.paris",
    "sifra": "securePassword123"
  },
  {
    "idKorisnik": 2,
    "korisnickoIme": "inspektor.moreau",
    "sifra": "investigate456"
  },
  {
    "idKorisnik": 3,
    "korisnickoIme": "analiticar.bellini",
    "sifra": "analyze789"
  }
],
"pitanjeR": [
  {
    "idPitanje": 1,
    "zlocinId": 1,
    "tekst": "Ko je poslednji put viđen sa Isabelle Moreau pre njene smrti?"
  },
  {
    "idPitanje": 2,
    "zlocinId": 1,
    "tekst": "Da li su pronađeni tragovi borbe u hotelskoj sobi?"
  },
  {
    "idPitanje": 3,
    "zlocinId": 1,
    "tekst": "Koji su motivi osumnjičenih Amelije Fontaine i Marca Bellinija?"
  }
],
"odnosOsumnjicenZrtvaR": [
  {
    "idOdnos": 1,
    "osumnjicenId": 1,
    "zrtvaId": 1,
    "tipOdnosa": "koleginice sa posla"
  },
  {
    "idOdnos": 2,
    "osumnjicenId": 2,
    "zrtvaId": 1,
    "tipOdnosa": "kockarski rivali"
  }
],
"odgovorR": [
  {
    "idOdogovor": 1,
    "pitanjeId": 1,
    "tekstOdgovora": "Amelia Fontaine je bila viđena kako izlazi iz sobe žrtve.",
    "tacan": true,
    "bodovi": 10
  },
  {
    "idOdogovor": 2,
    "pitanjeId": 1,
    "tekstOdgovora": "Marco Bellini je bio na drugom kraju grada.",
    "tacan": false,
    "bodovi": 0
  },
  {
    "idOdogovor": 3,
    "pitanjeId": 1,
    "tekstOdgovora": "Niko nije viđen u blizini sobe žrtve.",
    "tacan": false,
    "bodovi": 0
  }
],
"pitanjeIspitivanjeOsumnjicenogR": [
  {
    "idPitanjeIspitivanjeOsumnjicenog": 1,
    "kategorija": "Alibi",
    "tekst": "Gde ste bili u noći kada je Nathan Clarke ubijen?",
    "odgovor": "Bio sam kod kuće, sam, gledajući TV.",
    "komentar": "Nema potvrde alibija od treće strane.",
    "osumnjicenId": 2
  },
  {
    "idPitanjeIspitivanjeOsumnjicenog": 2,
    "kategorija": "Motiv",
    "tekst": "Da li ste imali neki razlog da naudite Nathanu?",
    "odgovor": "Ne, nismo imali nikakve probleme.",
    "komentar": "Svedoci tvrde da su imali žestoku raspravu nedelju dana ranije.",
    "osumnjicenId": 2
  },
  {
    "idPitanjeIspitivanjeOsumnjicenog": 3,
    "kategorija": "Pristup mestu zločina",
    "tekst": "Da li imate ključ ili način da uđete u Nathanuov stan?",
    "odgovor": "Ne, nikada nisam imao ključ.",
    "komentar": "Forenzičari nisu pronašli tragove provale.",
    "osumnjicenId": 1
  }],
  "pitanjeIspitivanjeSvedokaR": [
    {
      "idPitanjeIspitivanjeSvedoka": 1,
      "tekst": "Gde ste bili u trenutku kada je zločin izveden?",
      "odgovor": "Bio sam kod kuće.",
      "svedokId": 2,
      "nextPitanje": 3
    },
    {
      "idPitanjeIspitivanjeSvedoka": 2,
      "tekst": "Da li ste ikada imali konflikata sa osumnjičenim?",
      "odgovor": "Ne, nikada.",
      "svedokId": 2,
      "nextPitanje": 0
    },
    {
      "idPitanjeIspitivanjeSvedoka": 3,
      "tekst": "Da li možete potvrditi alibi osumnjičenog?",
      "odgovor": "Da, bio je sa mnom.",
      "svedokId": 3,
      "nextPitanje": 0
    }
  ],
  "osobaR": [
    {
      "idOsoba": 1,
      "ime": "Marko Marković",
      "kontakt": "123456789",
      "datum": "2025-04-17",
      "zanimanje": "Detektiv",
      "pol": "Muški",
      "zlocinId": 101
    },
    {
      "idOsoba": 2,
      "ime": "Jovana Jovanović",
      "kontakt": "987654321",
      "datum": "2025-04-17",
      "zanimanje": "Advokat",
      "pol": "Ženski",
      "zlocinId": 102
    },
    {
      "idOsoba": 3,
      "ime": "Nikola Nikolić",
      "kontakt": "1122334455",
      "datum": "2025-04-17",
      "zanimanje": "Novinar",
      "pol": "Muški",
      "zlocinId": 103
    }
  ],
  "zadatakR": [
  {
    "idZadatak": 1,
    "tekst": "Ispitati mesto zločina",
    "korak": "1",
    "uradjen": false,
    "nextZadatak": 2,
    "zlocinId": 101
  },
  {
    "idZadatak": 2,
    "tekst": "Pronaći svedoke",
    "korak": "2",
    "uradjen": false,
    "nextZadatak": 3,
    "zlocinId": 101
  }
],
"ispitivanjeSvedokaZadatakR":[
  {
    "idIspitivanjeSvedokaZadatak": 1,
    "svedokId": 101,
    "zadatakId": 1001,
    "uradjen": false
  },
  {
    "idIspitivanjeSvedokaZadatak": 2,
    "svedokId": 102,
    "zadatakId": 1002,
    "uradjen": true
  },
  {
    "idIspitivanjeSvedokaZadatak": 3,
    "svedokId": 103,
    "zadatakId": 1003,
    "uradjen": false
  }
],
"dokazZadatakR": [
  {
    "idDokazZadatak": 1,
    "tekst": "Analiziraj DNK tragove pronađene na nožu.",
    "dokazId": 1,
    "uradjen": false,
    "zadatakId": 2
  },
  {
    "idDokazZadatak": 2,
    "tekst": "Uporedi otiske prstiju sa čaše sa bazom osumnjičenih.",
    "dokazId": 2,
    "uradjen": false,
    "zadatakId": 3
  }],
  "ispitivanjeOsumnjicenogZadatakR":[
  {
    "idIspitivanjeOsumnjicenogZadatak": 1,
    "osumnjicenId": 42,
    "zadatakId": 7,
    "uradjen": false
  },
  {
    "idIspitivanjeOsumnjicenogZadatak": 2,
    "osumnjicenId": 43,
    "zadatakId": 8,
    "uradjen": true
  },
  {
    "idIspitivanjeOsumnjicenogZadatak": 3,
    "osumnjicenId": 42,
    "zadatakId": 9,
    "uradjen": false
  }
],
"telefonZadatakR": [
      {
        "idTelefonZadatak": 1,
        "telefonId": 10,
        "zadatakId": 3,
        "uradjen": false
      },
      {
        "idTelefonZadatak": 2,
        "telefonId": 11,
        "zadatakId": 4,
        "uradjen": true
      }
],
"forenzickiDokazZadatakR": [
  {
    "idForenzickiDokazZadatak": 1,
    "tekst": "Uporedi DNK tragove sa uzorcima osumnjičenih.",
    "forenzickiDokazId": 1,
    "uradjen": false,
    "zadatakId": 1
  },
  {
    "idForenzickiDokazZadatak": 2,
    "tekst": "Proveri da li postoji još tragova DNK na dršci noža.",
    "forenzickiDokazId": 1,
    "uradjen": false,
    "zadatakId": 2
  }
],
    "kontaktKtor":[{
        "idKontakt":0,
        "ime":"",
        "broj":"",
        "status":0,
        "zrtvaId":0
    }],
    "porukeKtor":[{
        "idPoruke":0,
        "tipPoruke":"",
        "sadrzaj":"",
        "datumVreme":"2023-11-11 8:30AM",
        "zrtvaId":0,
        "posiljalacId":0,
        "statusPoruke":"",
        "sirovana":false
    }],
    "poziviKtor":[{
        "idPoziv":0,
        "tip":0,
        "broj":"",
        "datumVreme":"2023-11-11 9:30AM",
        "zrtvaId":0,
        "status":0,
        "kontaktId":0
    }],
    "galerijaKtor":[{
        "idGalerija":0,
        "tip":0,
        "putanja":"",
        "zrtvaId":0,
        "datumVreme":"2023-11-11 9:30AM",
        "lokacija":""
    }],
    "aplikacijaKtor":[{
        "idAplikacije":0,
        "naziv": "",
        "tip": 0,
        "zrtvaId": 0,
        "aktivna": false,
        "informacije": ""
    }],
    "tragKtor":[{
        "idTrag":0,
        "forenzickiDokazId":0,
        "osumnjicenId":0
    }],
    "dokazOsumnjicenKtor":[{
        "idDokazOsumnjicen":0,
        "dokazId":0,
        "osumnjicenId":0
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
            Log.d("GEMINI ZLOCIN",response.zlocinRetrofit.toString())

            var zlocin: ZlocinR? =null

            if (response.zlocinRetrofit!=null) {
                zlocin= realmViewModel.insertZlocin(
                    tipZlocina,
                    response.zlocinRetrofit!!.naziv,
                    response.zlocinRetrofit!!.datum.toString(),
                    response.zlocinRetrofit!!.mesto,
                    response.zlocinRetrofit!!.opis,
                    response.zlocinRetrofit!!.status
                )
            }

            if(response.zrtvaRetrofit!=null){

                val millis = response.zrtvaRetrofit!!.osobaId?.datum
                val instant = millis?.let { Instant.ofEpochMilli(it) }
                val realmInstantZrtva = instant?.let { RealmInstant.from(instant.epochSecond, it.nano) }
                    ?: RealmInstant.now()

                val zrtva: ZrtvaR? = response.zrtvaRetrofit!!.osobaId?.let {
                    response.zrtvaRetrofit!!.osobaId.kontakt?.let { it1 ->
                        realmViewModel.insertZrtva(
                            response.zrtvaRetrofit!!.tipZrtve,
                            it.ime,
                            response.zrtvaRetrofit!!.detalji,
                            response.zrtvaRetrofit!!.statusZrtva,
                            zlocin,
                            it1,
                            realmInstantZrtva,
                            response.zrtvaRetrofit!!.osobaId.zanimanje,
                            polZ = response.zrtvaRetrofit!!.osobaId.pol
                        )
                    }
                }


                val dokazi:MutableList<DokazR> =mutableListOf()
                for(d in response.dokaziRetrofit!!){
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

                for(t in response.telefoniRetrofit!!){
                    realmViewModel.insertTelefon(
                        modelT = t.model,
                        osT = t.os,
                        zrtvaT = zrtva,
                        sifraT = t.sifra
                    )
                }

                for(f in response.forenzickiDokazRetrofit!!){
                    realmViewModel.insertForenzickiDokaz(
                        tipFD = f.tipForenzickiDokaz,
                        opisFD = f.opis,
                        statusFD = f.statusS,
                        zrtvaFD = zrtva,
                        vezaFD = f.veza
                    )
                }
                if(response.obdukcijaRetrofit!=null){
                    var obdukcija: ObdukcijaR? = realmViewModel.insertObdukcija(
                        response.obdukcijaRetrofit!!.izvestaj,
                        response.obdukcijaRetrofit!!.datum.toString(),
                        response.obdukcijaRetrofit!!.uzrokSmrti,
                        zrtva,
                        response.obdukcijaRetrofit!!.informacije
                    )
                }

                for(k in response.kontaktiRetrofit!!){
                    realmViewModel.insertKontakt(
                        imeK = k.ime,
                        brojK = k.broj,
                        statusK = k.status,
                        zrtvaK = zrtva
                    )
                }
            }

            //OSUMNJICENI NEKA GRESKA
            /*
            var osumnjiceni:MutableList<OsumnjicenR> = mutableListOf()
            for (o in response.osumnjiceniRetrofit!!){
                val m=o.motiv?.let { realmViewModel.insertMotiv(it.opis) }

                val millisOsumnjiceni = o.osobaId?.datum
                val instantOsumnjiceni = millisOsumnjiceni?.let { Instant.ofEpochMilli(it) }
                val realmInstantOsumnjiceni = instantOsumnjiceni?.let { RealmInstant.from(instantOsumnjiceni.epochSecond, it.nano) }
                    ?: RealmInstant.now()

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
            */
            Log.d("GEMINI",response.oneContactRetrofit.toString() )

            Log.d("GEMINI",response.galleryRetrofit.toString())

            for(s in response.svedociRetrofit!!){
                val millisSvedok = s.osobaId?.datum
                val instantSvedok = millisSvedok?.let { Instant.ofEpochMilli(it) }
                val realmInstantSvedok = instantSvedok?.let { RealmInstant.from(instantSvedok.epochSecond, it.nano) }
                    ?: RealmInstant.now()

                s.osobaId?.let {
                    s.osobaId.kontakt?.let { it1 ->
                        realmViewModel.insertSvedok(
                            imeS = it.ime,
                            kontaktS = it1,
                            izjavaS = s.izjava,
                            zlocinS = zlocin,
                            statusSvedokS = s.statusSvedok,
                            statusIspitanS = s.statusIspitan,
                            datumS = realmInstantSvedok,
                            zanimanjS = s.osobaId.zanimanje,
                            polS = s.osobaId.pol
                        )
                    }
                }
            }

            for(k in response.oneContactRetrofit!!){
                realmViewModel.insertOneContact(
                    zlocin, k.ime,k.broj, k.slika
                )
            }

            for (g in response.galleryRetrofit!!){
                val millisGallery = g.datum
                val instantGallery = millisGallery?.let { Instant.ofEpochMilli(it) }
                val realmInstantGallery = instantGallery?.let { RealmInstant.from(instantGallery.epochSecond, it.nano) }
                    ?: RealmInstant.now()
                g.slika?.let {
                    realmViewModel.insertGalleryPhoto(
                        zlocinIdG = zlocin,
                        slikaG = it,
                        datumG = realmInstantGallery,
                        mestoG = g.mesto
                    )
                }
            }

            //APLIKACIJE
            //TRAG

            
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
    val geminiData: GeminiResponseRetrofit? =null
)