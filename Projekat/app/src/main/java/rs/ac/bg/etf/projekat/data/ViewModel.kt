package rs.ac.bg.etf.projekat.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import rs.ac.bg.etf.projekat.data.realm.DokazR
import rs.ac.bg.etf.projekat.data.realm.DokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeOsumnjicenogZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeSvedokaZadatakR
import rs.ac.bg.etf.projekat.data.realm.IzjavaZaPacijentaR
import rs.ac.bg.etf.projekat.data.realm.LekarskiTestR
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR
import rs.ac.bg.etf.projekat.data.realm.MedicinskiIzvestajR
import rs.ac.bg.etf.projekat.data.realm.OneContactR
import rs.ac.bg.etf.projekat.data.realm.OsobaR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.PacijentR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeOsumnjicenogR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeSvedokaR
import rs.ac.bg.etf.projekat.data.realm.PitanjeR
import rs.ac.bg.etf.projekat.data.realm.PorukeZadatakR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonR
import rs.ac.bg.etf.projekat.data.realm.TelefonZadatakR
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppKontaktR
import rs.ac.bg.etf.projekat.data.realm.ZadatakR
import rs.ac.bg.etf.projekat.data.realm.ZlocinR
import rs.ac.bg.etf.projekat.data.realm.ZrtvaR
import rs.ac.bg.etf.projekat.data.realmViewModel.RealmViewModel
import rs.ac.bg.etf.projekat.data.retrofit.models.GeminiResponseRetrofit
import rs.ac.bg.etf.projekat.data.retrofit.models.GeminiResponseRetrofitMysteriousSymptoms
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest
import rs.ac.bg.etf.projekat.data.retrofit.models.MessageResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.ScorePageKorisnikResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val MyRepository: Repository,
    private val commonRepository: CommonRepository
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

    private val _uiStateZlocinData = MutableStateFlow(UiStateDataZlocin())
    val uiStateZlocinData: StateFlow<UiStateDataZlocin> = _uiStateZlocinData

    fun getAllDataZlocin() = viewModelScope.launch {
        try {
            val response = commonRepository.selectAllOsumnjiceni()
            val response2 = commonRepository.selectAllSvedoci()
            _uiStateZlocinData.value = UiStateDataZlocin(suspects = response, witnesses = response2)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateZlocinData.value =
                UiStateDataZlocin(suspects = emptyList(), witnesses = emptyList())
        }
    }

    private val _uiStatePitanjaZaOsumnjicenog = MutableStateFlow(UiStatePitanjaZaOsumnjicenog())
    val uiStatePitanjaZaOsumnjicenog: StateFlow<UiStatePitanjaZaOsumnjicenog> =
        _uiStatePitanjaZaOsumnjicenog

    fun getPitanjaZaOsumnjicenog(osumnjicen: String) = viewModelScope.launch {
        try {
            val response1 = commonRepository.selectPitanjaByOsumnjicenAndCategory(osumnjicen, "opsta")
            val response2 = commonRepository.selectPitanjaByOsumnjicenAndCategory(osumnjicen, "alibi")
            val response3 = commonRepository.selectPitanjaByOsumnjicenAndCategory(osumnjicen, "dokaz")
            val response4 = commonRepository.selectPitanjaByOsumnjicenAndCategory(osumnjicen, "kontradikcija")
            _uiStatePitanjaZaOsumnjicenog.value =
                UiStatePitanjaZaOsumnjicenog(response1, response2, response3, response4)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStatePitanjaZaOsumnjicenog.value =
                UiStatePitanjaZaOsumnjicenog(emptyList(), emptyList(), emptyList(), emptyList())
        }
    }

    private val _uiStatePitanjaZaSvedoka = MutableStateFlow(UiStatePitanjaZaSvedoka())
    val uiStatePitanjaZaSvedoka: StateFlow<UiStatePitanjaZaSvedoka> = _uiStatePitanjaZaSvedoka

    fun getPitanjaZaSvedoka(svedok: String) = viewModelScope.launch {
        try {
            val response = commonRepository.selectPitanjaBySvedok(svedok)
            _uiStatePitanjaZaSvedoka.value = UiStatePitanjaZaSvedoka(response)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStatePitanjaZaSvedoka.value = UiStatePitanjaZaSvedoka(emptyList())
        }
    }

    private val _uiStateTasks = MutableStateFlow(UiStateTasks())
    val uiStateTasks: StateFlow<UiStateTasks> = _uiStateTasks

    fun getTasks() = viewModelScope.launch {
        try {
            val response = commonRepository.selectTasks()
            _uiStateTasks.value = UiStateTasks(response)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateTasks.value = UiStateTasks(emptyList())
        }
    }

    private val _uiStateEvidence = MutableStateFlow(UiStateEvidences())
    val uiStateEvidence: StateFlow<UiStateEvidences> = _uiStateEvidence

    fun getEvidences() = viewModelScope.launch {
        try {
            val response = commonRepository.selectEvidences()
            val response2 = commonRepository.selectEvidencesTasks(response)
            _uiStateEvidence.value = UiStateEvidences(response, response2)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateEvidence.value = UiStateEvidences(emptyList(), emptyList())
        }
    }

    private val _uiStateCntEvidence = MutableStateFlow(UiStateCntEvidence())
    val uiStateCntEvidence: StateFlow<UiStateCntEvidence> = _uiStateCntEvidence

    private val _uiStateCntForensicEvidence = MutableStateFlow(UiStateCntForensicEvidence())
    val uiStateCntForensicEvidence: StateFlow<UiStateCntForensicEvidence> =
        _uiStateCntForensicEvidence

    fun cntIncrement(cnt: Int) = viewModelScope.launch {
        try {
            _uiStateCntEvidence.value = UiStateCntEvidence(cnt = cnt + 1)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateCntEvidence.value = UiStateCntEvidence(cnt = 0)
        }
    }

    fun cntForensicIncrement(cnt: Int) = viewModelScope.launch {
        try {
            _uiStateCntForensicEvidence.value = UiStateCntForensicEvidence(forensicCnt = cnt + 1)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateCntForensicEvidence.value = UiStateCntForensicEvidence(forensicCnt = 0)
        }
    }

    private val _uiStateForensicEvidence = MutableStateFlow(UiStateForensicEvidences())
    val uiStateForensicEvidence: StateFlow<UiStateForensicEvidences> = _uiStateForensicEvidence

    fun getForensicEvidences() = viewModelScope.launch {
        try {
            val response = commonRepository.selectForensicEvidences()
            val response2 = commonRepository.selectForensicEvidencesTasks(response)
            _uiStateForensicEvidence.value = UiStateForensicEvidences(response, response2)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateForensicEvidence.value = UiStateForensicEvidences(emptyList(), emptyList())
        }
    }

    fun updateEvidenceAndEvidenceTask(zadatakDokaz: DokazZadatakR) = viewModelScope.launch {
        zadatakDokaz.zadatakId?.idZadatak?.let {
            commonRepository.updateDokazZadatakAndZadatak(
                it,
                zadatakDokaz.idDokazZadatak
            )
        }
    }

    fun updateForensicEvidenceAndForensicEvidenceTask(zadatakDokaz: ForenzickiDokazZadatakR) =
        viewModelScope.launch {
            zadatakDokaz.zadatakId?.idZadatak?.let {
                commonRepository.updateForenzickiDokazZadatakAndZadatak(
                    it,
                    zadatakDokaz.idForenzickiDokazZadatak
                )
            }

        }

    fun updateSuspectTask(zadatak: IspitivanjeOsumnjicenogZadatakR) = viewModelScope.launch {
        zadatak.zadatakId?.idZadatak?.let {
            commonRepository.updateIspitivanjeOsumnjicenogZadatak(
                zadatak.idIspitivanjeOsumnjicenogZadatak,
                it
            )
        }
    }

    fun updateWitnessTask(zadatak: IspitivanjeSvedokaZadatakR) = viewModelScope.launch {
        zadatak.zadatakId?.idZadatak?.let {
            commonRepository.updateIspitivanjeSvedokaZadatak(
                zadatak.idIspitivanjeSvedokaZadatak,
                it
            )
        }
    }

    fun updateTelefonTask(telefon: TelefonZadatakR) = viewModelScope.launch {
        telefon.zadatakId?.idZadatak?.let { commonRepository.updateTelefonZadatak(telefon.idTelefonZadatak, it) }
    }

    fun updatePorukeTask(poruke: PorukeZadatakR) = viewModelScope.launch {
        poruke.zadatakId?.idZadatak?.let { commonRepository.updatePorukeZadatak(poruke.idPorukeZadatak, it) }
    }

    private val _uiSteteSelectedAnswers = MutableStateFlow(UiSteteSelectedAnswers())
    val uiSteteSelectedAnswers: StateFlow<UiSteteSelectedAnswers> = _uiSteteSelectedAnswers

    fun updateSelectedanswes(answers: Map<Int, Int?>) = viewModelScope.launch {
        Log.d("ANSWERS", answers.toString())
        try {

            _uiSteteSelectedAnswers.value = UiSteteSelectedAnswers(answers)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiSteteSelectedAnswers.value = UiSteteSelectedAnswers(emptyMap())
        }
    }


    //gemini

    private val _uiStateGeminiData = MutableStateFlow(UiStateGeminiData())
    val uiStateGeminiData: StateFlow<UiStateGeminiData> = _uiStateGeminiData

    fun getGeminiData(realmViewModel: RealmViewModel, onSuccess: () -> Unit, onError: () -> Unit) = viewModelScope.launch {
        val jsonString = getJsonMurder()

        val requestBody = jsonString.toRequestBody("application/json".toMediaType())
        try {
            val response = MyRepository.geminiMurder()
            //val response2 = MyRepository.geminiData(requestBody)
            Log.d("GEMINI", response.toString())
            _uiStateGeminiData.value = UiStateGeminiData(response)

            val tipZlocina: TipZlocinaR? = realmViewModel.inserTipZlocina("Murder")
            Log.d("GEMINI ZLOCIN", response.zlocinRetrofit.toString())

            var zlocin: ZlocinR? = null
            val dokazi: MutableList<DokazR> = mutableListOf()
            val telefonLista: MutableList<TelefonR> = mutableListOf()
            val forenzickiDokazLista: MutableList<ForenzickiDokazR> = mutableListOf()

            if (response.zlocinRetrofit != null) {
                zlocin = realmViewModel.insertZlocin(
                    response.zlocinRetrofit!!.idZlocin,
                    tipZlocina,
                    response.zlocinRetrofit!!.naziv,
                    response.zlocinRetrofit!!.datum.toString(),
                    response.zlocinRetrofit!!.mesto,
                    response.zlocinRetrofit!!.opis,
                    response.zlocinRetrofit!!.status
                )
            }
            var zrtva: ZrtvaR? = null
            if (response.zrtvaRetrofit != null) {
                val millis = response.zrtvaRetrofit!!.osobaId?.datum
                val instant = millis?.let { Instant.ofEpochMilli(it) }
                val realmInstantZrtva =
                    instant?.let { RealmInstant.from(instant.epochSecond, it.nano) }
                        ?: RealmInstant.now()

                zrtva = response.zrtvaRetrofit!!.osobaId?.let {
                    response.zrtvaRetrofit!!.osobaId.kontakt?.let { it1 ->
                        realmViewModel.insertZrtva(
                            response.zrtvaRetrofit!!.idZrtva,
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

                for (d in response.dokaziRetrofit!!) {
                    val dk = realmViewModel.insertDokaz(
                        d.idDokaz,
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

                for (t in response.telefoniRetrofit!!) {
                    val tl = realmViewModel.insertTelefon(
                        idTelefonT = t.idTelefon,
                        modelT = t.model,
                        osT = t.os,
                        zrtvaT = zrtva,
                        sifraT = t.sifra
                    )
                    if (tl != null) {
                        telefonLista.add(tl)
                    }
                }


                for (f in response.forenzickiDokazRetrofit!!) {
                    val foren = realmViewModel.insertForenzickiDokaz(
                        idForenzickiDokazFD = f.idForenzickiDokaz,
                        tipFD = f.tipForenzickiDokaz,
                        opisFD = f.opis,
                        statusFD = f.statusS,
                        zrtvaFD = zrtva,
                        vezaFD = f.veza
                    )

                    if (foren != null) {
                        forenzickiDokazLista.add(foren)
                    }
                }
                if (response.obdukcijaRetrofit != null) {
                    realmViewModel.insertObdukcija(
                        response.obdukcijaRetrofit!!.idObdukcija,
                        response.obdukcijaRetrofit!!.izvestaj,
                        response.obdukcijaRetrofit!!.datum.toString(),
                        response.obdukcijaRetrofit!!.uzrokSmrti,
                        zrtva,
                        response.obdukcijaRetrofit!!.informacije
                    )
                }

                for (k in response.kontaktiRetrofit!!) {
                    realmViewModel.insertKontakt(
                        idKontaktK = k.idKontakt,
                        imeK = k.ime,
                        brojK = k.broj,
                        statusK = k.status,
                        zrtvaK = zrtva
                    )
                }

                for (a in response.aplikacijeRetrofit!!) {
                    realmViewModel.insertAplikacija(
                        idAplikacijeA = a.idAplikacije,
                        zrtvaA = zrtva,
                        nazivA = a.naziv,
                        tipA = a.tip,
                        aktivnaA = a.aktivna,
                        informacijeA = a.informacije
                    )
                }
            }

            var osumnjiceni: MutableList<OsumnjicenR> = mutableListOf()
            for (o in response.osumnjiceniRetrofit!!) {
                val m = o.motiv?.let { realmViewModel.insertMotiv(it.opis) }

                val millisOsumnjiceni = o.osobaId?.datum
                val instantOsumnjiceni = millisOsumnjiceni?.let { Instant.ofEpochMilli(it) }
                val realmInstantOsumnjiceni = instantOsumnjiceni?.let {
                    RealmInstant.from(
                        instantOsumnjiceni.epochSecond,
                        it.nano
                    )
                }
                    ?: RealmInstant.now()

                realmViewModel.insertOsoba(
                    idOsobaO = o.osobaId.idOsoba,
                    imeZ = o.osobaId.ime,
                    kontaktZ = o.osobaId.kontakt,
                    datumZ = realmInstantOsumnjiceni,
                    zanimanjeZ = o.osobaId.zanimanje,
                    polZ = o.osobaId.pol,
                    zlocinZ = zlocin
                )

                var os = realmViewModel.insertOsumnjiceni(
                    idOsumnjicenO = o.idOsumnjicen,
                    imeO = o.osobaId.ime,
                    statusO = o.status,
                    tipOsumnjicenO = o.tipOsumnjicen,
                    motivO = m,
                    zlocinO = zlocin,
                    krivO = o.kriv,
                    kontaktO = o.osobaId.kontakt,
                    datumO = realmInstantOsumnjiceni,
                    zanimanjO = o.osobaId.zanimanje,
                    polO = o.osobaId.pol
                )

                if (os != null) {
                    osumnjiceni.add(os)
                }
            }

            val svedokLista = mutableListOf<SvedokR>()
            for (s in response.svedociRetrofit!!) {
                val millisSvedok = s.osobaId?.datum
                val instantSvedok = millisSvedok?.let { Instant.ofEpochMilli(it) }
                val realmInstantSvedok =
                    instantSvedok?.let { RealmInstant.from(instantSvedok.epochSecond, it.nano) }
                        ?: RealmInstant.now()


                realmViewModel.insertOsoba(
                    idOsobaO = s.osobaId.idOsoba,
                    imeZ = s.osobaId.ime,
                    kontaktZ = s.osobaId.kontakt,
                    datumZ = realmInstantSvedok,
                    zanimanjeZ = s.osobaId.zanimanje,
                    polZ = s.osobaId.pol,
                    zlocinZ = zlocin
                )

                val sv = realmViewModel.insertSvedok(
                    idSvedokS = s.idSvedok,
                    imeS = s.osobaId.ime,
                    kontaktS = s.osobaId.kontakt,
                    izjavaS = s.izjava,
                    zlocinS = zlocin,
                    statusSvedokS = s.statusSvedok,
                    statusIspitanS = s.statusIspitan,
                    datumS = realmInstantSvedok,
                    zanimanjS = s.osobaId.zanimanje,
                    polS = s.osobaId.pol
                )
                if (sv != null) {
                    svedokLista.add(sv)
                }
            }

            val oneContactLista: MutableList<OneContactR> = mutableListOf()
            for (k in response.oneContactRetrofit!!) {
                val o = realmViewModel.insertOneContact(
                    k.idOneContact, zlocin, k.ime, k.broj, k.slika
                )
                if (o != null) {
                    oneContactLista.add(o)
                }
            }

            for (o in response.obicnePorukeRetrofit!!) {
                val millisPoruka = o.datum
                val instantPoruka = millisPoruka?.let { Instant.ofEpochMilli(it) }
                val realmInstantPoruka =
                    instantPoruka?.let { RealmInstant.from(instantPoruka.epochSecond, it.nano) }
                        ?: RealmInstant.now()

                realmViewModel.insertObicnaPoruka(
                    kontaktKoSaljeO = oneContactLista.find { it.idOneContact == o.kontaktKoSalje },
                    kontaktKomeSaljeO = oneContactLista.find { it.idOneContact == o.kontaktKomeSalje },
                    tekstO = o.tekst,
                    datumO = realmInstantPoruka,
                    procitanaO = o.procitana
                )
            }

            for (g in response.galleryRetrofit!!) {
                val millisGallery = g.datum
                val instantGallery = millisGallery?.let { Instant.ofEpochMilli(it) }
                val realmInstantGallery =
                    instantGallery?.let { RealmInstant.from(instantGallery.epochSecond, it.nano) }
                        ?: RealmInstant.now()
                g.slika?.let {
                    realmViewModel.insertGalleryPhoto(
                        idPhotoG = g.idPhoto,
                        zlocinIdG = zlocin,
                        slikaG = it,
                        datumG = realmInstantGallery,
                        mestoG = g.mesto
                    )
                }
            }

            for (t in response.tragoviRetrofit!!) {
                val osum = osumnjiceni.find { it.idOsumnjicen == t.osumnjicenId.idOsumnjicen }
                val foren =
                    forenzickiDokazLista.find { it.idForenzickiDokaz == t.forenzickiDokazId.idForenzickiDokaz }
                if (osum != null && foren != null) {
                    realmViewModel.insertTrag(
                        idTragT = t.idTrag,
                        forenzickiDokazIdT = foren,
                        osumnjicenIdT = osum
                    )
                }
            }

            for (d in response.dokaziOsumnjiceniRetrofit!!) {
                val osum = osumnjiceni.find { it.idOsumnjicen == d.osumnjicenId.idOsumnjicen }
                val dokaz = dokazi.find { it.idDokaz == d.dokazId.idDokaz }

                realmViewModel.insertDokazOsumnjicenog(
                    idDokazOsumnjicenDO = d.idDokazOsumnjicen,
                    dokazIdDO = dokaz,
                    osumnjicenIdDO = osum
                )
            }

            for (b in response.beleskeRetrofit!!) {
                val millisBeleska = b.datum
                val instantBeleska = millisBeleska?.let { Instant.ofEpochMilli(it) }
                val realmInstantBeleska =
                    instantBeleska?.let { RealmInstant.from(instantBeleska.epochSecond, it.nano) }
                        ?: RealmInstant.now()
                realmViewModel.insertBeleska(
                    idBeleskaB = b.idBeleska,
                    zlocinIdB = zlocin,
                    tekstB = b.tekst,
                    datumB = realmInstantBeleska
                )
            }

            val whatsAppLista: MutableList<WhatsAppKontaktR> = mutableListOf()
            for (wa in response.whatsappKontaktRetrofit!!) {
                wa.slika?.let {
                    val w = realmViewModel.insertWhatsAppKontakt(
                        idWhatsAppKontaktW = wa.idWhatsAppKontakt,
                        zlocinIdW = zlocin,
                        imeW = wa.ime,
                        brojW = wa.broj,
                        slikaW = it
                    )
                    if (w != null) {
                        whatsAppLista.add(w)
                    }
                }
            }

            for (waP in response.whatsappPorukaRetrofit!!) {
                val kontKoSalje = whatsAppLista.find { it.idWhatsAppKontakt == waP.kontaktKoSalje }
                val kontKomeSalje =
                    whatsAppLista.find { it.idWhatsAppKontakt == waP.kontaktKomeSalje }

                val millisWhatsappPoruka = waP.datum
                val instantWhatsappPoruka = millisWhatsappPoruka?.let { Instant.ofEpochMilli(it) }
                val realmInstantWhatsappPoruka = instantWhatsappPoruka?.let {
                    RealmInstant.from(
                        instantWhatsappPoruka.epochSecond,
                        it.nano
                    )
                }
                    ?: RealmInstant.now()

                if (kontKoSalje != null && kontKomeSalje != null) {
                    realmViewModel.insertWhatsAppPoruka(
                        idWhatsAppPorukaW = waP.idWhatsAppPoruka,
                        kontaktKoSalje = kontKoSalje,
                        kontaktKomeSalje = kontKomeSalje,
                        tekstW = waP.tekst,
                        datumW = realmInstantWhatsappPoruka,
                        procitanaW = waP.procitana
                    )
                }
            }

            for (oC in response.oneCallRetrofit!!) {
                val oneCont = oneContactLista.find { it.idOneContact == oC.idOneCall }

                val millisOneCall = oC.datum
                val instantOneCall = millisOneCall?.let { Instant.ofEpochMilli(it) }
                val realmInstantOneCall =
                    instantOneCall?.let { RealmInstant.from(instantOneCall.epochSecond, it.nano) }
                        ?: RealmInstant.now()

                realmViewModel.insertOneCall(
                    idOneCallC = oC.idOneCall,
                    kontaktC = oneCont,
                    datumC = realmInstantOneCall,
                    propustenC = oC.propusten,
                    dolazniC = oC.dolazni
                )
            }

            for (odnosOZ in response.odnosiOsumnjiceniZrtvaRetrofit!!) {
                val osum = osumnjiceni.find { it.idOsumnjicen == odnosOZ.osumnjicenId }

                realmViewModel.insertOdnosOsumnjicenZrtva(
                    idOdnosOOZ = odnosOZ.idOdnos,
                    osumnjicenOOZ = osum,
                    zrtvaOOZ = zrtva,
                    tipOdnosaOOZ = odnosOZ.tipOdnosa
                )
            }

            val pitanjaLista: MutableList<PitanjeR> = mutableListOf()
            for (p in response.pitanjaRetrofit!!) {
                val pit = realmViewModel.insertPitanje(
                    idPitanjeP = p.idPitanje,
                    zlocinIdP = zlocin,
                    tekstP = p.tekst
                )
                if (pit != null) {
                    pitanjaLista.add(pit)
                }
            }

            for (o in response.odgovoriRetrofit!!) {
                val pit = pitanjaLista.find { it.idPitanje == o.pitanjeId }
                realmViewModel.insertOdogovor(
                    idOdogovorO = o.idOdogovor,
                    pitanjeIdO = pit,
                    tekstOdgovoraO = o.tekstOdgovora,
                    tacanO = o.tacan,
                    bodoviO = o.bodovi
                )
            }

            withContext(Dispatchers.IO) {
                for (p in response.pitanjeIspitivanjeOsumnjicenogRetrofit!!) {
                    val osum = osumnjiceni.find { it.idOsumnjicen == p.osumnjicenId }

                    realmViewModel.insertPitanjeIspitivanjeOsumnjicenog(
                        idPitanjeIspitivanjeOsumnjicenogZ = p.idPitanjeIspitivanjeOsumnjicenog,
                        osumnjicenIdZ = osum?.idOsumnjicen ?: -1,
                        kategorijaZ = p.kategorija,
                        tekstZ = p.tekst,
                        odgovorZ = p.odgovor,
                        komentarZ = p.komentar
                    )
                }
            }

            for (pIS in response.pitanjeIspitivanjeSvedokaRetrofit!!) {
                val sv = svedokLista.find { it.idSvedok == pIS.svedokId }
                realmViewModel.insertPitanjeIspitivanjeSvedoka(
                    idPitanjeIspitivanjeSvedokaP = pIS.idPitanjeIspitivanjeSvedoka,
                    svedokZ = sv,
                    tekstZ = pIS.tekst,
                    odgovorZ = pIS.odgovor
                )
            }

            //ISPRAVITI nextZ=null
            val zadatakLista = mutableListOf<ZadatakR>()
            for (z in response.zadaciRetrofit!!) {
                val zad = realmViewModel.insertZadatak(
                    idZadatakZ = z.idZadatak,
                    tekstZ = z.tekst,
                    korakZ = z.korak,
                    uradjenZ = z.uradjen,
                    nextZ = null,
                    zlocinZ = zlocin
                )
                if (zad != null) {
                    zadatakLista.add(zad)
                }
            }

            for (i in 0 until zadatakLista.size - 1) {
                val trenutniZadatak = zadatakLista[i]
                val naredniZadatak = zadatakLista[i + 1]
                trenutniZadatak.next = naredniZadatak
                realmViewModel.updateZadatak(trenutniZadatak.idZadatak, naredniZadatak.idZadatak)
            }

            for (dokZ in response.dokaziZadaciRetrofit!!) {
                val zad = zadatakLista.find { it.idZadatak == dokZ.zadatakId }
                val dok = dokazi.find { it.idDokaz == dokZ.dokazId }

                if (zad != null && dok != null) {
                    realmViewModel.insertDokazZadatak(
                        idDokazZadatakZ = dokZ.idDokazZadatak,
                        tekstZ = dokZ.tekst,
                        dokazIdZ = dok,
                        uradjenZ = dokZ.uradjen,
                        zadatakIdZ = zad
                    )
                }
            }

            for (isp in response.ispitivanjeOsumnjicenogZadaciRetrofit!!) {
                val osum = osumnjiceni.find { it.idOsumnjicen == isp.osumnjicenId }
                val zad = zadatakLista.find { it.idZadatak == isp.zadatakId }
                realmViewModel.insertIspitivanjeOsumnjicenogZadatak(
                    idIspitivanjeOsumnjicenogZadatakZ = isp.idIspitivanjeOsumnjicenogZadatak,
                    osumnjicenIdZ = osum,
                    zadatakIdZ = zad,
                    uradjenZ = isp.uradjen
                )
            }

            for (isp in response.ispitivanjeSvedokaZadaciRetrofit!!) {
                val zad = zadatakLista.find { it.idZadatak == isp.zadatakId }
                val sv = svedokLista.find { it.idSvedok == isp.svedokId }

                realmViewModel.insertIspitivanjeSvedokaZadatak(
                    idIspitivanjeSvedokaZadatakZ = isp.idIspitivanjeSvedokaZadatak,
                    svedokIdZ = sv,
                    zadatakIdZ = zad,
                    uradjenZ = isp.uradjen
                )
            }

            for (tel in response.telefonZadaciRetrofit!!) {
                val zad = zadatakLista.find { it.idZadatak == tel.zadatakId }
                val t = telefonLista.find { it.idTelefon == tel.idTelefonZadatak }
                realmViewModel.insertTelefonZadatak(
                    idTelefonZadatakZ = tel.idTelefonZadatak,
                    telefonZ = t,
                    zadatakIdZ = zad,
                    uradjenZ = tel.uradjen
                )
            }

            for (forenz in response.forenzickiDokazZadaciRetrofit!!) {
                val zad = zadatakLista.find { it.idZadatak == forenz.zadatakId }
                val f =
                    forenzickiDokazLista.find { it.idForenzickiDokaz == forenz.forenzickiDokazId }
                realmViewModel.insertForenzickiDokazZadatak(
                    idForenzickiDokazZadatakZ = forenz.idForenzickiDokazZadatak,
                    tekstZ = forenz.tekst,
                    forenzickiDokazIdZ = f,
                    uradjenZ = forenz.uradjen,
                    zadatakIdZ = zad
                )
            }
            onSuccess()
            realmViewModel.callGetTitleDatePlaceDescFromCrime()
            Log.d("G","SUCCESS")
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateGeminiData.value = UiStateGeminiData(null)
            onError()
        }
    }


    // RETROFIT 2

    private val _uiStateGeminiDataMS = MutableStateFlow(UiStateGeminiDataMS())
    val uiStateGeminiDataMS: StateFlow<UiStateGeminiDataMS> = _uiStateGeminiDataMS

    fun getGeminiDataMS(realmViewModel: RealmViewModel) = viewModelScope.launch {
        val jsonString = getJsonMysteriousSymptoms()
        val requestBody = jsonString.toRequestBody("application/json".toMediaType())
        try {
            val response = MyRepository.geminiDataMS(requestBody)
            Log.d("GEMINI2", response.toString())
            _uiStateGeminiDataMS.value = UiStateGeminiDataMS(response)

            val tipZlocina: TipZlocinaR? = realmViewModel.inserTipZlocina("Mysterious Symptoms")

            var zlocin: ZlocinR? = null
            val dokazi: MutableList<DokazR> = mutableListOf()
            val telefonLista: MutableList<TelefonR> = mutableListOf()
            val forenzickiDokazLista: MutableList<ForenzickiDokazR> = mutableListOf()
            var pacijent: PacijentR? = null
            var osoba: OsobaR? = null
            var zrtva: ZrtvaR? = null

            if (response.zlocinRetrofit != null) {
                zlocin = realmViewModel.insertZlocin(
                    response.zlocinRetrofit!!.idZlocin,
                    tipZlocina,
                    response.zlocinRetrofit!!.naziv,
                    response.zlocinRetrofit!!.datum.toString(),
                    response.zlocinRetrofit!!.mesto,
                    response.zlocinRetrofit!!.opis,
                    response.zlocinRetrofit!!.status
                )
            }

            if (response.pacijentRetrofit != null && zlocin != null) {
                val millisOsoba = response.pacijentRetrofit!!.zrtvaId.osobaId.datum
                val instantOsoba = millisOsoba?.let { Instant.ofEpochMilli(it) }
                val realmInstantOsoba = instantOsoba?.let {
                    RealmInstant.from(
                        instantOsoba.epochSecond,
                        it.nano
                    )
                }
                    ?: RealmInstant.now()

                osoba = realmViewModel.insertOsoba(
                    idOsobaO = response.pacijentRetrofit!!.zrtvaId.osobaId.idOsoba,
                    imeZ = response.pacijentRetrofit!!.zrtvaId.osobaId.ime,
                    kontaktZ = response.pacijentRetrofit!!.zrtvaId.osobaId.kontakt,
                    datumZ = realmInstantOsoba,
                    zanimanjeZ = response.pacijentRetrofit!!.zrtvaId.osobaId.zanimanje,
                    polZ = response.pacijentRetrofit!!.zrtvaId.osobaId.pol,
                    zlocinZ = zlocin
                )

                if (osoba != null) {
                    val millisZrtva = response.pacijentRetrofit!!.zrtvaId.osobaId.datum
                    val instantZrtva = millisZrtva?.let { Instant.ofEpochMilli(it) }
                    val realmInstantZrtva = instantZrtva?.let {
                        RealmInstant.from(
                            instantZrtva.epochSecond,
                            it.nano
                        )
                    }
                        ?: RealmInstant.now()

                    zrtva = realmViewModel.insertZrtva(
                        idZrtvaZ = response.pacijentRetrofit!!.zrtvaId.idZrtva,
                        tipZ = response.pacijentRetrofit!!.zrtvaId.tipZrtve,
                        imeZ = response.pacijentRetrofit!!.zrtvaId.osobaId.ime,
                        detaljiZ = response.pacijentRetrofit!!.zrtvaId.detalji,
                        statusZ = response.pacijentRetrofit!!.zrtvaId.statusZrtva,
                        zlocinZ = zlocin,
                        kontaktZ = response.pacijentRetrofit!!.zrtvaId.osobaId.kontakt,
                        datumZ = realmInstantZrtva,
                        zanimanjeZ = response.pacijentRetrofit!!.zrtvaId.osobaId.zanimanje,
                        polZ = response.pacijentRetrofit!!.zrtvaId.osobaId.pol
                    )

                    if (zrtva != null) {
                        pacijent = realmViewModel.insertPacijent(
                            idPacijentP = response.pacijentRetrofit!!.idPacijent,
                            simptomiP = response.pacijentRetrofit!!.simptomi,
                            statusPacijentaP = response.pacijentRetrofit!!.statusPacijenta,
                            datumPrijaveP = realmInstantZrtva,
                            prijavioP = response.pacijentRetrofit!!.prijavio.ime,
                            zlocinP = zlocin,
                            zrtvaP = zrtva
                        )
                    }
                }
            }

            val osobeList = mutableListOf<OsobaR>()
            for (o in response.osobeRetrofit!!) {
                val millisOsoba = o.datum
                val instantOsoba = millisOsoba?.let { Instant.ofEpochMilli(it) }
                val realmInstantOsoba =
                    instantOsoba?.let { RealmInstant.from(instantOsoba.epochSecond, it.nano) }
                        ?: RealmInstant.now()

                val osoba = realmViewModel.insertOsoba(
                    idOsobaO = o.idOsoba,
                    imeZ = o.ime,
                    kontaktZ = o.kontakt,
                    datumZ = realmInstantOsoba,
                    zanimanjeZ = o.zanimanje,
                    polZ = o.pol,
                    zlocinZ = zlocin
                )
                if (osoba != null) {
                    osobeList.add(osoba)
                }
            }

            for (d in response.dokaziRetrofit!!) {
                val dk = realmViewModel.insertDokaz(
                    d.idDokaz,
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

            for (t in response.telefoniRetrofit!!) {
                val tl = realmViewModel.insertTelefon(
                    idTelefonT = t.idTelefon,
                    modelT = t.model,
                    osT = t.os,
                    zrtvaT = zrtva,
                    sifraT = t.sifra
                )
                if (tl != null) {
                    telefonLista.add(tl)
                }
            }

            for (f in response.forenzickiDokazRetrofit!!) {
                val foren = realmViewModel.insertForenzickiDokaz(
                    idForenzickiDokazFD = f.idForenzickiDokaz,
                    tipFD = f.tipForenzickiDokaz,
                    opisFD = f.opis,
                    statusFD = f.statusS,
                    zrtvaFD = zrtva,
                    vezaFD = f.veza
                )

                if (foren != null) {
                    forenzickiDokazLista.add(foren)
                }
            }

            for (a in response.aplikacijeRetrofit!!) {
                realmViewModel.insertAplikacija(
                    idAplikacijeA = a.idAplikacije,
                    zrtvaA = zrtva,
                    nazivA = a.naziv,
                    tipA = a.tip,
                    aktivnaA = a.aktivna,
                    informacijeA = a.informacije
                )
            }

            val oneContactLista: MutableList<OneContactR> = mutableListOf()
            for (k in response.oneContactRetrofit!!) {
                val o = realmViewModel.insertOneContact(
                    k.idOneContact, zlocin, k.ime, k.broj, k.slika
                )
                if (o != null) {
                    oneContactLista.add(o)
                }
            }

            for (o in response.obicnePorukeRetrofit!!) {
                val millisPoruka = o.datum
                val instantPoruka = millisPoruka?.let { Instant.ofEpochMilli(it) }
                val realmInstantPoruka =
                    instantPoruka?.let { RealmInstant.from(instantPoruka.epochSecond, it.nano) }
                        ?: RealmInstant.now()

                realmViewModel.insertObicnaPoruka(
                    kontaktKoSaljeO = oneContactLista.find { it.idOneContact == o.kontaktKoSalje },
                    kontaktKomeSaljeO = oneContactLista.find { it.idOneContact == o.kontaktKomeSalje },
                    tekstO = o.tekst,
                    datumO = realmInstantPoruka,
                    procitanaO = o.procitana
                )
            }

            for (g in response.galleryRetrofit!!) {
                val millisGallery = g.datum
                val instantGallery = millisGallery?.let { Instant.ofEpochMilli(it) }
                val realmInstantGallery =
                    instantGallery?.let { RealmInstant.from(instantGallery.epochSecond, it.nano) }
                        ?: RealmInstant.now()
                g.slika?.let {
                    realmViewModel.insertGalleryPhoto(
                        idPhotoG = g.idPhoto,
                        zlocinIdG = zlocin,
                        slikaG = it,
                        datumG = realmInstantGallery,
                        mestoG = g.mesto
                    )
                }
            }

            for (b in response.beleskeRetrofit!!) {
                val millisBeleska = b.datum
                val instantBeleska = millisBeleska?.let { Instant.ofEpochMilli(it) }
                val realmInstantBeleska =
                    instantBeleska?.let { RealmInstant.from(instantBeleska.epochSecond, it.nano) }
                        ?: RealmInstant.now()
                realmViewModel.insertBeleska(
                    idBeleskaB = b.idBeleska,
                    zlocinIdB = zlocin,
                    tekstB = b.tekst,
                    datumB = realmInstantBeleska
                )
            }

            val whatsAppLista: MutableList<WhatsAppKontaktR> = mutableListOf()
            for (wa in response.whatsappKontaktRetrofit!!) {
                wa.slika?.let {
                    val w = realmViewModel.insertWhatsAppKontakt(
                        idWhatsAppKontaktW = wa.idWhatsAppKontakt,
                        zlocinIdW = zlocin,
                        imeW = wa.ime,
                        brojW = wa.broj,
                        slikaW = it
                    )
                    if (w != null) {
                        whatsAppLista.add(w)
                    }
                }
            }

            for (waP in response.whatsappPorukaRetrofit!!) {
                val kontKoSalje = whatsAppLista.find { it.idWhatsAppKontakt == waP.kontaktKoSalje }
                val kontKomeSalje =
                    whatsAppLista.find { it.idWhatsAppKontakt == waP.kontaktKomeSalje }

                val millisWhatsappPoruka = waP.datum
                val instantWhatsappPoruka = millisWhatsappPoruka?.let { Instant.ofEpochMilli(it) }
                val realmInstantWhatsappPoruka = instantWhatsappPoruka?.let {
                    RealmInstant.from(
                        instantWhatsappPoruka.epochSecond,
                        it.nano
                    )
                }
                    ?: RealmInstant.now()

                if (kontKoSalje != null && kontKomeSalje != null) {
                    realmViewModel.insertWhatsAppPoruka(
                        idWhatsAppPorukaW = waP.idWhatsAppPoruka,
                        kontaktKoSalje = kontKoSalje,
                        kontaktKomeSalje = kontKomeSalje,
                        tekstW = waP.tekst,
                        datumW = realmInstantWhatsappPoruka,
                        procitanaW = waP.procitana
                    )
                }
            }

            for (oC in response.oneCallRetrofit!!) {
                val oneCont = oneContactLista.find { it.idOneContact == oC.idOneCall }

                val millisOneCall = oC.datum
                val instantOneCall = millisOneCall?.let { Instant.ofEpochMilli(it) }
                val realmInstantOneCall =
                    instantOneCall?.let { RealmInstant.from(instantOneCall.epochSecond, it.nano) }
                        ?: RealmInstant.now()

                realmViewModel.insertOneCall(
                    idOneCallC = oC.idOneCall,
                    kontaktC = oneCont,
                    datumC = realmInstantOneCall,
                    propustenC = oC.propusten,
                    dolazniC = oC.dolazni
                )
            }

            val pitanjaLista: MutableList<PitanjeR> = mutableListOf()
            for (p in response.pitanjaRetrofit!!) {
                val pit = realmViewModel.insertPitanje(
                    idPitanjeP = p.idPitanje,
                    zlocinIdP = zlocin,
                    tekstP = p.tekst
                )
                if (pit != null) {
                    pitanjaLista.add(pit)
                }
            }

            for (o in response.odgovoriRetrofit!!) {
                val pit = pitanjaLista.find { it.idPitanje == o.pitanjeId }
                realmViewModel.insertOdogovor(
                    idOdogovorO = o.idOdogovor,
                    pitanjeIdO = pit,
                    tekstOdgovoraO = o.tekstOdgovora,
                    tacanO = o.tacan,
                    bodoviO = o.bodovi
                )
            }

            val zadatakLista = mutableListOf<ZadatakR>()
            for (z in response.zadaciRetrofit!!) {
                val zad = realmViewModel.insertZadatak(
                    idZadatakZ = z.idZadatak,
                    tekstZ = z.tekst,
                    korakZ = z.korak,
                    uradjenZ = z.uradjen,
                    nextZ = null,
                    zlocinZ = zlocin
                )
                if (zad != null) {
                    zadatakLista.add(zad)
                }
            }

            for (i in 0 until zadatakLista.size - 1) {
                val trenutniZadatak = zadatakLista[i]
                val naredniZadatak = zadatakLista[i + 1]
                trenutniZadatak.next = naredniZadatak
                realmViewModel.updateZadatak(trenutniZadatak.idZadatak, naredniZadatak.idZadatak)
            }

            for (dokZ in response.dokaziZadaciRetrofit!!) {
                val zad = zadatakLista.find { it.idZadatak == dokZ.zadatakId }
                val dok = dokazi.find { it.idDokaz == dokZ.dokazId }

                if (zad != null && dok != null) {
                    realmViewModel.insertDokazZadatak(
                        idDokazZadatakZ = dokZ.idDokazZadatak,
                        tekstZ = dokZ.tekst,
                        dokazIdZ = dok,
                        uradjenZ = dokZ.uradjen,
                        zadatakIdZ = zad
                    )
                }
            }

            for (tel in response.telefonZadaciRetrofit!!) {
                val zad = zadatakLista.find { it.idZadatak == tel.zadatakId }
                val t = telefonLista.find { it.idTelefon == tel.idTelefonZadatak }
                realmViewModel.insertTelefonZadatak(
                    idTelefonZadatakZ = tel.idTelefonZadatak,
                    telefonZ = t,
                    zadatakIdZ = zad,
                    uradjenZ = tel.uradjen
                )
            }

            for (forenz in response.forenzickiDokazZadaciRetrofit!!) {
                val zad = zadatakLista.find { it.idZadatak == forenz.zadatakId }
                val f =
                    forenzickiDokazLista.find { it.idForenzickiDokaz == forenz.forenzickiDokazId }
                realmViewModel.insertForenzickiDokazZadatak(
                    idForenzickiDokazZadatakZ = forenz.idForenzickiDokazZadatak,
                    tekstZ = forenz.tekst,
                    forenzickiDokazIdZ = f,
                    uradjenZ = forenz.uradjen,
                    zadatakIdZ = zad
                )
            }

            if (response.medicinskiIzvestajRetrofit != null && pacijent != null) {
                realmViewModel.insertMedicinskiIzvestaj(
                    idMedicinskiIzvestajM = response.medicinskiIzvestajRetrofit!!.idMedicinskiIzvestaj,
                    rezimeM = response.medicinskiIzvestajRetrofit!!.rezime,
                    CTnalazM = response.medicinskiIzvestajRetrofit!!.CTnalaz,
                    MRInalazM = response.medicinskiIzvestajRetrofit!!.MRInalaz,
                    krvnaSlikaM = response.medicinskiIzvestajRetrofit!!.krvnaSlika,
                    toksikoloskeAnalizeM = response.medicinskiIzvestajRetrofit!!.toksikoloskeAnalize,
                    zakljucakM = response.medicinskiIzvestajRetrofit!!.zakljucak,
                    pacijentIdM = pacijent
                )
            }

            if (response.lekarskiTestRetrofit != null && pacijent != null) {
                realmViewModel.insertLekarskiTest(
                    idLekarskiTestL = response.lekarskiTestRetrofit!!.idLekarskiTest,
                    pacijentIdL = pacijent,
                    izvestajL = response.lekarskiTestRetrofit!!.izvestaj
                )
            }

            for (l in response.lokacijeIstrageRetrofit!!) {
                if (zlocin != null) {
                    realmViewModel.insertLokacijeIstrage(
                        idLokacijeIstrageL = l.idLokacijeIstrage,
                        mestoL = l.mesto,
                        nazivL = l.naziv,
                        opisL = l.opis,
                        zlocinIdL = zlocin,
                        geoTackaALatitudeL = l.geoTackaALatitude,
                        geoTackaALongitudeL = l.geoTackaALongitude
                    )
                }
            }

            if (response.izjavaZaPacijentaRetrofit != null && pacijent != null) {
                val millisOsoba = response.izjavaZaPacijentaRetrofit!!.osobaId.datum
                val instantOsoba = millisOsoba?.let { Instant.ofEpochMilli(it) }
                val realmInstantOsoba = instantOsoba?.let {
                    RealmInstant.from(
                        instantOsoba.epochSecond,
                        it.nano
                    )
                }
                    ?: RealmInstant.now()

                var osobaIzjava = realmViewModel.insertOsoba(
                    idOsobaO = response.izjavaZaPacijentaRetrofit!!.osobaId.idOsoba,
                    imeZ = response.izjavaZaPacijentaRetrofit!!.osobaId.ime,
                    kontaktZ = response.izjavaZaPacijentaRetrofit!!.osobaId.kontakt,
                    datumZ = realmInstantOsoba,
                    zanimanjeZ = response.izjavaZaPacijentaRetrofit!!.osobaId.zanimanje,
                    polZ = response.izjavaZaPacijentaRetrofit!!.osobaId.pol,
                    zlocinZ = zlocin
                )

                if (osobaIzjava != null) {
                    realmViewModel.insertIzjavaZaPacijenta(
                        idIzjavaZaPacijentaI = response.izjavaZaPacijentaRetrofit!!.idIzjavaZaPacijenta,
                        izjavaI = response.izjavaZaPacijentaRetrofit!!.izjava,
                        pacijentIdI = pacijent,
                        osobaP = osobaIzjava
                    )
                }
            }

            // realmViewModel.callGetTitleDatePlaceDescFromCrime()
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateGeminiDataMS.value = UiStateGeminiDataMS(null)
        }
    }

    //Mysterious Symptoms

    private val _uiStateMysteriousSymptomsData = MutableStateFlow(UiStateDataMysteriousSymptoms())
    val uiStateMysteriousSymptomsData: StateFlow<UiStateDataMysteriousSymptoms> = _uiStateMysteriousSymptomsData

    fun getAllDataMysteriousSymptoms() = viewModelScope.launch {
        try {
            val response = commonRepository.selectPacijent()
            val responseMed = commonRepository.selectMedicinskiIzvestaj()
            val responseIzjava = commonRepository.selectIzjavaZaPacijenta()
            val responseLekarskiTest= commonRepository.selectLekarskiTest()
            val responseLokacije = commonRepository.selectLokacijeIstrageR()
            Log.d("GEMINI GET LOKACIJE",responseLokacije.toString())
            _uiStateMysteriousSymptomsData.value =
                UiStateDataMysteriousSymptoms(patient = response, medicalReport = responseMed, statement = responseIzjava, tests = responseLekarskiTest, locations = responseLokacije)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateMysteriousSymptomsData.value =
                UiStateDataMysteriousSymptoms(patient = null,medicalReport = null, statement = null, tests = null, locations = emptyList())
        }
    }

    fun selectTelefonZadatakViewModel(): TelefonZadatakR? {
        return commonRepository.selectTelefonZadatak()
    }

    fun selectPorukeZadatakViewModel(): PorukeZadatakR? {
        return commonRepository.selectPorukeZadatak()
    }

    fun selectIspitivanjeOsumnjicenogZadatakViewModel(osumnjicenZ: OsumnjicenR?): IspitivanjeOsumnjicenogZadatakR? {
        return commonRepository.selectIspitivanjeOsumnjicenogZadatak(osumnjicenZ)
    }

    fun selectIspitivanjeSvedokaZadatakViewModel(svedokZ:SvedokR?): IspitivanjeSvedokaZadatakR? {
        return commonRepository.selectIspitivanjeSvedokaZadatak(svedokZ)
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

// RETROFIT 2

data class UiStateGeminiDataMS(
    val geminiDataMS: GeminiResponseRetrofitMysteriousSymptoms? =null
)

//Mysterious Symptoms

data class UiStateDataMysteriousSymptoms(
    val patient: PacijentR? =null,
    val medicalReport:MedicinskiIzvestajR?=null,
    val tests:LekarskiTestR?=null,
    val statement:IzjavaZaPacijentaR?=null,
    val locations: List<LokacijeIstrageR> = emptyList()
)