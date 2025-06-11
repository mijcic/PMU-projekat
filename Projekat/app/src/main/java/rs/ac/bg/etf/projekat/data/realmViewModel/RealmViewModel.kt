package rs.ac.bg.etf.projekat.data.realmViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.R
import rs.ac.bg.etf.projekat.data.realm.AlibiR
import rs.ac.bg.etf.projekat.data.realm.AplikacijaR
import rs.ac.bg.etf.projekat.data.realm.BeleskaR
import rs.ac.bg.etf.projekat.data.realm.DokazOsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.DokazR
import rs.ac.bg.etf.projekat.data.realm.DokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.GalleryR
import rs.ac.bg.etf.projekat.data.realm.ForenzickiDokazZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeOsumnjicenogZadatakR
import rs.ac.bg.etf.projekat.data.realm.IspitivanjeSvedokaZadatakR
import rs.ac.bg.etf.projekat.data.realm.IzjavaZaPacijentaR
import rs.ac.bg.etf.projekat.data.realm.KontaktR
import rs.ac.bg.etf.projekat.data.realm.LekarskiTestR
import rs.ac.bg.etf.projekat.data.realm.LokacijeIstrageR
import rs.ac.bg.etf.projekat.data.realm.MedicinskiIzvestajR
import rs.ac.bg.etf.projekat.data.realm.MisijaPorukaR
import rs.ac.bg.etf.projekat.data.realm.MisijaR
import rs.ac.bg.etf.projekat.data.realm.MotivR
import rs.ac.bg.etf.projekat.data.realm.ObdukcijaR
import rs.ac.bg.etf.projekat.data.realm.ObicnaPorukaR
import rs.ac.bg.etf.projekat.data.realm.OdgovorR
import rs.ac.bg.etf.projekat.data.realm.OdnosOsumnjicenZrtvaR
import rs.ac.bg.etf.projekat.data.realm.OneCallR
import rs.ac.bg.etf.projekat.data.realm.OneContactR
import rs.ac.bg.etf.projekat.data.realm.OsobaR
import rs.ac.bg.etf.projekat.data.realm.OsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.PacijentR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeOsumnjicenogR
import rs.ac.bg.etf.projekat.data.realm.PitanjeR
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeSvedokaR
import rs.ac.bg.etf.projekat.data.realm.PorukeR
import rs.ac.bg.etf.projekat.data.realm.PorukeZadatakR
import rs.ac.bg.etf.projekat.data.realm.PrijavljeniKorisnikR
import rs.ac.bg.etf.projekat.data.realm.StatusAlibijaR
import rs.ac.bg.etf.projekat.data.realm.StatusSvedokR
import rs.ac.bg.etf.projekat.data.realm.StatusZrtvaR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonR
import rs.ac.bg.etf.projekat.data.realm.TelefonZadatakR
import rs.ac.bg.etf.projekat.data.realm.TipDokazaR
import rs.ac.bg.etf.projekat.data.realm.TipForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.TipOdnosaR
import rs.ac.bg.etf.projekat.data.realm.TipOsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import rs.ac.bg.etf.projekat.data.realm.TragR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppKontaktR
import rs.ac.bg.etf.projekat.data.realm.WhatsAppPorukaR
import rs.ac.bg.etf.projekat.data.realm.ZadatakR
import rs.ac.bg.etf.projekat.data.realm.ZlocinR
import rs.ac.bg.etf.projekat.data.realm.ZrtvaR
import rs.ac.bg.etf.projekat.data.realm.stZlocinR
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RealmViewModel @Inject constructor(
    //private val MyRepository: Repository,
    private val repo: RepositoryImplRealmViewModel
) : ViewModel() {

    val realm = repo.getRealm()

    private val _uiState = MutableStateFlow(UiStateUserData())
    val uiState : StateFlow<UiStateUserData> = _uiState

    private val _uiStateCrimeData = MutableStateFlow(UiStateCrimeData())
    val uiStateCrimeData : StateFlow<UiStateCrimeData> = _uiStateCrimeData

    private var _uiStateZlocinSave = MutableStateFlow(UiStateZlocinSave())
    val uiStateZlocinSave : StateFlow<UiStateZlocinSave> = _uiStateZlocinSave

    var selectedWhatsappContact: WhatsAppKontaktR? = null

    suspend fun inserTipZlocina(nazivTZ: String): TipZlocinaR? {
        return repo.insertTipZlocina(nazivTZ)
    }

    suspend fun insertZlocin(idZlocinZ:Int, tipZlocina: TipZlocinaR?, nazivZ: String, datumZ: String, mestoZ: String, opisZ: String, statusZ: String): ZlocinR? {
        return repo.insertZlocin(idZlocinZ,tipZlocina,nazivZ,datumZ,mestoZ,opisZ,statusZ)
    }

    suspend fun insertOsoba(idOsobaO:Int,imeZ: String, kontaktZ:String, datumZ: RealmInstant, zanimanjeZ: String,polZ: String,zlocinZ: ZlocinR?): OsobaR?{
        return repo.insertOsoba(idOsobaO, imeZ, kontaktZ, datumZ, zanimanjeZ, polZ, zlocinZ)
    }

    suspend fun insertZrtva(idZrtvaZ:Int,
        tipZ: String, imeZ: String, detaljiZ: String, statusZ: String,
        zlocinZ: ZlocinR?, kontaktZ: String, datumZ: RealmInstant,
        zanimanjeZ: String, polZ: String
    ): ZrtvaR? {
        return repo.insertZrtva(idZrtvaZ, tipZ, imeZ, detaljiZ, statusZ, zlocinZ, kontaktZ, datumZ, zanimanjeZ, polZ)
    }

    suspend fun insertMotiv(opisM: String): MotivR? {
        return repo.insertMotiv(opisM)
    }

    suspend fun insertOsumnjiceni(
        idOsumnjicenO:Int,imeO: String, statusO: Int, tipOsumnjicenO: String, motivO: MotivR?, zlocinO: ZlocinR?, krivO: Int,
        kontaktO: String, datumO: RealmInstant, zanimanjO: String, polO: String
    ): OsumnjicenR? {
        return repo.insertOsumnjiceni(idOsumnjicenO,imeO, statusO, tipOsumnjicenO, motivO, zlocinO, krivO,
            kontaktO, datumO, zanimanjO, polO)
    }

    suspend fun insertDokaz(idDokazD:Int,tipDokazaD: String, opisD: String, zlocinD: ZlocinR?, zrtvaD: ZrtvaR?, statusD: Int): DokazR? {
        return repo.insertDokaz(idDokazD,tipDokazaD, opisD, zlocinD, zrtvaD, statusD)
    }

    suspend fun insertDokazOsumnjicenog(idDokazOsumnjicenDO:Int,dokazIdDO: DokazR?, osumnjicenIdDO: OsumnjicenR?): DokazOsumnjicenR? {
        return repo.insertDokazOsumnjicenog(idDokazOsumnjicenDO,dokazIdDO, osumnjicenIdDO)
    }

    suspend fun insertSvedok(
        idSvedokS:Int, imeS: String, kontaktS: String, izjavaS: String, zlocinS: ZlocinR?,
        statusSvedokS: String, statusIspitanS: Int, datumS: RealmInstant, zanimanjS: String, polS: String
    ): SvedokR? {
        return repo.insertSvedok(idSvedokS, imeS, kontaktS, izjavaS, zlocinS,
            statusSvedokS, statusIspitanS, datumS, zanimanjS, polS)
    }

    suspend fun insertAlibi(osumnjicenA: OsumnjicenR?, svedokA: SvedokR?, opisA: String, statusAlibijaA: String): AlibiR? {
        return repo.insertAlibi(osumnjicenA, svedokA, opisA, statusAlibijaA)
    }

    suspend fun insertMisija(zlocinM: ZlocinR?, nazivM: String, opisM: String, statusM: Int): MisijaR? {
        return repo.insertMisija(zlocinM, nazivM, opisM, statusM)
    }

    suspend fun insertKontakt(idKontaktK:Int,imeK: String, brojK: String, statusK: Int, zrtvaK: ZrtvaR?): KontaktR? {
        return repo.insertKontakt(idKontaktK,imeK, brojK, statusK, zrtvaK)
    }

    suspend fun insertPoruka(tipP: String, sadrzajP: String, datumVremeP: RealmInstant?, zrtvaP: ZrtvaR?, posiljalacP: KontaktR?, statusP: String, sifrovanaP: Boolean): PorukeR? {
        return repo.insertPoruka(tipP, sadrzajP, datumVremeP, zrtvaP, posiljalacP, statusP, sifrovanaP)
    }

    suspend fun insertMisijaPoruka(zlocinMP: ZlocinR?, nazivMP: String, porukaMP: PorukeR?, statusMP: Int, posiljalacMP: String): MisijaPorukaR? {
        return repo.insertMisijaPoruka(zlocinMP, nazivMP, porukaMP, statusMP, posiljalacMP)
    }

    suspend fun insertObdukcija(idObdukcijaO:Int,izvestajO: String, datumO: String, uzrokSmrtiO: String, zrtvaO: ZrtvaR?, informacijeO: String): ObdukcijaR? {
        return repo.insertObdukcija(idObdukcijaO,izvestajO, datumO, uzrokSmrtiO, zrtvaO, informacijeO)
    }

    suspend fun insertForenzickiDokaz(idForenzickiDokazFD:Int,tipFD: String, opisFD: String, statusFD: Int, zrtvaFD: ZrtvaR?, vezaFD: String): ForenzickiDokazR? {
        return repo.insertForenzickiDokaz(idForenzickiDokazFD,tipFD, opisFD, statusFD, zrtvaFD, vezaFD)
    }

    suspend fun insertTelefon(idTelefonT:Int,modelT: String, osT: String, zrtvaT: ZrtvaR?, sifraT: String): TelefonR? {
        return repo.insertTelefon(idTelefonT,modelT, osT, zrtvaT, sifraT)
    }

    suspend fun insertOdnosOsumnjicenZrtva(idOdnosOOZ:Int,osumnjicenOOZ: OsumnjicenR?, zrtvaOOZ: ZrtvaR?, tipOdnosaOOZ: String): OdnosOsumnjicenZrtvaR? {
        return repo.insertOdnosOsumnjicenZrtva(idOdnosOOZ,osumnjicenOOZ, zrtvaOOZ, tipOdnosaOOZ)
    }

    fun checkIfUserExists() {
        val userExists = realm.query<PrijavljeniKorisnikR>().count().find() > 0
        _uiState.value = UiStateUserData(userExists)
    }

    suspend fun insertPrijavljeniKorisnik(korisnickoImePK: String, sifraPK: String) {
        repo.insertPrijavljeniKorisnik(korisnickoImePK, sifraPK)
    }

    suspend fun insertPitanjeIspitivanjeOsumnjicenog(idPitanjeIspitivanjeOsumnjicenogZ:Int, osumnjicenIdZ: Int, kategorijaZ: String, tekstZ: String, odgovorZ: String, komentarZ: String): PitanjeIspitivanjeOsumnjicenogR? {
        return repo.insertPitanjeIspitivanjeOsumnjicenog(idPitanjeIspitivanjeOsumnjicenogZ, osumnjicenIdZ, kategorijaZ, tekstZ, odgovorZ, komentarZ)
    }

    suspend fun insertPitanjeIspitivanjeSvedoka(idPitanjeIspitivanjeSvedokaP:Int,svedokZ: SvedokR?, tekstZ: String, odgovorZ: String): PitanjeIspitivanjeSvedokaR? {
        return repo.insertPitanjeIspitivanjeSvedoka(idPitanjeIspitivanjeSvedokaP,svedokZ, tekstZ, odgovorZ)
    }

    suspend fun insertZadatak(
        idZadatakZ:Int,tekstZ: String, korakZ: String, uradjenZ: Boolean,
        nextZ: ZadatakR?, zlocinZ: ZlocinR?
    ): ZadatakR? {
        return repo.insertZadatak(idZadatakZ,tekstZ, korakZ, uradjenZ, nextZ, zlocinZ)
    }

    suspend fun updateZadatak(idZadatakZ: Int, idNextZadatak: Int) {
        repo.updateZadatak(idZadatakZ, idNextZadatak)
    }

    suspend fun insertDokazZadatak(
        idDokazZadatakZ:Int,tekstZ: String, dokazIdZ: DokazR?, uradjenZ: Boolean,
        zadatakIdZ: ZadatakR?
    ): DokazZadatakR? {
        return repo.insertDokazZadatak(idDokazZadatakZ,tekstZ, dokazIdZ, uradjenZ,zadatakIdZ)
    }

    suspend fun insertForenzickiDokazZadatak(
        idForenzickiDokazZadatakZ:Int,tekstZ: String, forenzickiDokazIdZ: ForenzickiDokazR?, uradjenZ: Boolean,
        zadatakIdZ: ZadatakR?
    ): ForenzickiDokazZadatakR? {
        return repo.insertForenzickiDokazZadatak(idForenzickiDokazZadatakZ,tekstZ, forenzickiDokazIdZ,
            uradjenZ, zadatakIdZ)
    }

    suspend fun insertIspitivanjeOsumnjicenogZadatak(
        idIspitivanjeOsumnjicenogZadatakZ:Int,osumnjicenIdZ: OsumnjicenR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean
    ): IspitivanjeOsumnjicenogZadatakR? {
        return repo.insertIspitivanjeOsumnjicenogZadatak(
            idIspitivanjeOsumnjicenogZadatakZ,osumnjicenIdZ, zadatakIdZ, uradjenZ)
    }

    suspend fun insertIspitivanjeSvedokaZadatak(
        idIspitivanjeSvedokaZadatakZ:Int,svedokIdZ: SvedokR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean
    ): IspitivanjeSvedokaZadatakR? {
        return repo.insertIspitivanjeSvedokaZadatak(idIspitivanjeSvedokaZadatakZ,svedokIdZ, zadatakIdZ, uradjenZ)
    }

    suspend fun insertTelefonZadatak(
        idTelefonZadatakZ:Int,telefonZ: TelefonR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean
    ): TelefonZadatakR? {
        return repo.insertTelefonZadatak(idTelefonZadatakZ,telefonZ, zadatakIdZ, uradjenZ)
    }

    suspend fun insertPorukeZadatak(porukeIdZ: PorukeR?, zadatakIdZ: ZadatakR?, uradjenZ: Boolean): PorukeZadatakR? {
        return repo.insertPorukeZadatak(porukeIdZ, zadatakIdZ, uradjenZ)
    }

    suspend fun insertPacijent(
        idPacijentP: Int, simptomiP: String, statusPacijentaP: String, datumPrijaveP: RealmInstant,
        prijavioP: String, zlocinP: ZlocinR, zrtvaP: ZrtvaR
    ): PacijentR? {
        return repo.insertPacijent(
            idPacijentP, simptomiP, statusPacijentaP, datumPrijaveP, prijavioP, zlocinP, zrtvaP)
    }

    suspend fun insertIzjavaZaPacijenta(
        idIzjavaZaPacijentaI: Int, izjavaI: String, pacijentIdI: PacijentR, osobaP: OsobaR
    ): IzjavaZaPacijentaR? {
        return repo.insertIzjavaZaPacijenta(idIzjavaZaPacijentaI, izjavaI, pacijentIdI, osobaP)
    }

    suspend fun insertLekarskiTest(idLekarskiTestL: Int, pacijentIdL: PacijentR, izvestajL: String): LekarskiTestR? {
        return repo.insertLekarskiTest(idLekarskiTestL, pacijentIdL, izvestajL)
    }

    suspend fun insertLokacijeIstrage(idLokacijeIstrageL: Int, mestoL: String, nazivL: String, opisL: String, zlocinIdL: ZlocinR,geoTackaALatitudeL:Double, geoTackaALongitudeL:Double): LokacijeIstrageR? {
        return repo.insertLokacijeIstrage(idLokacijeIstrageL, mestoL, nazivL, opisL, zlocinIdL,geoTackaALatitudeL, geoTackaALongitudeL)
    }

    suspend fun insertMedicinskiIzvestaj(idMedicinskiIzvestajM: Int, rezimeM: String, CTnalazM: String, MRInalazM: String, krvnaSlikaM: String, toksikoloskeAnalizeM: String, zakljucakM: String, pacijentIdM: PacijentR): MedicinskiIzvestajR? {
        return repo.insertMedicinskiIzvestaj(idMedicinskiIzvestajM, rezimeM, CTnalazM, MRInalazM, krvnaSlikaM, toksikoloskeAnalizeM, zakljucakM, pacijentIdM)
    }

    suspend fun getTitleDatePlaceDescFromCrime() {
        var title: String? = ""
        var date: RealmInstant? = null
        var dateString: String? = ""
        var place: String? = ""
        var description: String? = ""
        realm.write {
            var currentID: Int? = realm.query<ZlocinR>().max("idZlocin", Int::class).find() ?: 0
            var currentCrime = realm.query<ZlocinR>("idZlocin == $0", currentID).first().find()
            title = currentCrime?.naziv

            date = currentCrime?.datum

            val instant = date?.let { Instant.ofEpochSecond(it.epochSeconds, it.nanosecondsOfSecond.toLong()) }
            val safeInstant = instant ?: Instant.now()
            val localDateTime = LocalDateTime.ofInstant(safeInstant, ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            dateString = localDateTime?.format(formatter) + "."

            place = currentCrime?.mesto
            description = currentCrime?.opis
        }
        _uiStateCrimeData.value = UiStateCrimeData(title, dateString, place, description)
        Log.d("CrimeData", uiStateCrimeData.value.toString())
    }

    fun callGetTitleDatePlaceDescFromCrime() {
        viewModelScope.launch {
            getTitleDatePlaceDescFromCrime()
        }
    }

    suspend fun insertPitanje(idPitanjeP:Int,zlocinIdP: ZlocinR?, tekstP: String): PitanjeR? {
        return repo.insertPitanje(idPitanjeP,zlocinIdP, tekstP)
    }

    suspend fun insertOdogovor(idOdogovorO:Int,pitanjeIdO: PitanjeR?, tekstOdgovoraO: String, tacanO: Boolean, bodoviO: Int) {
        repo.insertOdogovor(idOdogovorO,pitanjeIdO, tekstOdgovoraO, tacanO, bodoviO)
    }

    suspend fun getAllPitanje(): List<PitanjeR>? {
        val currentID: Int = realm.query<ZlocinR>().max("idZlocin", Int::class).find() ?: 0
        val currentCrime = realm.query<ZlocinR>("idZlocin == $0", currentID).first().find()
        return realm.query<PitanjeR>("zlocinId == $0", currentCrime).find()
    }

    suspend fun getAllOdgovorForPitanje(pitanjeO: PitanjeR?): List<OdgovorR>? {
        return repo.getAllOdgovorForPitanje(pitanjeO)
    }

    suspend fun insertBeleska(idBeleskaB:Int,zlocinIdB: ZlocinR?, tekstB: String, datumB: RealmInstant?): BeleskaR? {
        return repo.insertBeleska(idBeleskaB,zlocinIdB, tekstB, datumB)
    }

    suspend fun insertAplikacija(idAplikacijeA:Int, zrtvaA: ZrtvaR?, nazivA: String, tipA:Int, aktivnaA:Boolean,informacijeA:String): AplikacijaR? {
        return repo.insertAplikacija(idAplikacijeA, zrtvaA, nazivA, tipA, aktivnaA,informacijeA)
    }

    suspend fun insertTrag(idTragT:Int, forenzickiDokazIdT: ForenzickiDokazR,osumnjicenIdT:OsumnjicenR): TragR? {
        return repo.insertTrag(idTragT, forenzickiDokazIdT, osumnjicenIdT)
    }

    suspend fun insertWhatsAppKontakt(idWhatsAppKontaktW:Int,zlocinIdW: ZlocinR?, imeW: String, brojW: String, slikaW: Int): WhatsAppKontaktR? {
        return repo.insertWhatsAppKontakt(idWhatsAppKontaktW,zlocinIdW, imeW, brojW, slikaW)
    }

    suspend fun insertWhatsAppPoruka(idWhatsAppPorukaW:Int,kontaktKoSalje: WhatsAppKontaktR, kontaktKomeSalje: WhatsAppKontaktR, tekstW: String, datumW: RealmInstant?, procitanaW: Boolean): WhatsAppPorukaR? {
        return repo.insertWhatsAppPoruka(idWhatsAppPorukaW,kontaktKoSalje, kontaktKomeSalje, tekstW, datumW, procitanaW)
    }

    suspend fun insertOneContact(idOneContactC:Int,zlocinIdC: ZlocinR?, imeC: String, brojC: String, slikaC: Int?): OneContactR? {
        return repo.insertOneContact(idOneContactC,zlocinIdC, imeC, brojC, slikaC)
    }

    suspend fun insertOneCall(idOneCallC:Int,kontaktC: OneContactR?, datumC: RealmInstant?, propustenC: Boolean, dolazniC: Boolean): OneCallR? {
        return repo.insertOneCall(idOneCallC,kontaktC, datumC, propustenC, dolazniC)
    }

    suspend fun insertGalleryPhoto(idPhotoG:Int,zlocinIdG: ZlocinR?, slikaG: Int, datumG: RealmInstant?, mestoG: String): GalleryR? {
        return repo.insertGalleryPhoto(idPhotoG, zlocinIdG, slikaG, datumG, mestoG)
    }

    suspend fun insertObicnaPoruka(kontaktKoSaljeO: OneContactR?, kontaktKomeSaljeO: OneContactR?, tekstO: String, datumO: RealmInstant?, procitanaO: Boolean): ObicnaPorukaR? {
        return repo.insertObicnaPoruka(kontaktKoSaljeO, kontaktKomeSaljeO, tekstO, datumO, procitanaO)
    }

    suspend fun getAllBeleska(): List<BeleskaR>? {
        val currentID: Int = realm.query<ZlocinR>().max("idZlocin", Int::class).find() ?: 0
        val currentCrime = realm.query<ZlocinR>("idZlocin == $0", currentID).first().find()
        return realm.query<BeleskaR>("zlocinId == $0", currentCrime).find()
    }

    suspend fun getAllCalls(): List<OneCallR>? {
        val currentID: Int = realm.query<ZlocinR>().max("idZlocin", Int::class).find() ?: 0
        val currentCrime = realm.query<ZlocinR>("idZlocin == $0", currentID).first().find()
        return realm.query<OneCallR>("kontakt.zlocinId.idZlocin == $0", currentCrime?.idZlocin).find()
    }

    suspend fun getAllContacts(): List<OneContactR> {
        val currentID: Int = realm.query<ZlocinR>().max("idZlocin", Int::class).find() ?: 0
        val currentCrime = realm.query<ZlocinR>("idZlocin == $0", currentID).first().find()
        return realm.query<OneContactR>("zlocinId == $0", currentCrime).find()
    }

    suspend fun getContactsLastMessages(): List<OneContactPreviewItem> {
        val kontaktMe = realm.query<OneContactR>("ime == $0", "Me").first().find()
            ?: return emptyList()

        val contacts = getAllContacts().filter { it.idOneContact != kontaktMe.idOneContact }

        return contacts.map { kontakt ->
            val lastMessage = realm.query<ObicnaPorukaR>(
                """
                (kontaktKoSalje == $0 AND kontaktKomeSalje == $1) OR
                (kontaktKoSalje == $2 AND kontaktKomeSalje == $3)
                """.trimIndent(),
                kontaktMe, kontakt,
                kontakt, kontaktMe
            )
                .sort("datum", Sort.DESCENDING)
                .first()
                .find()

            OneContactPreviewItem(kontakt, lastMessage)
        }
    }

    suspend fun getMessagesWithContact(contactId: Int): List<ObicnaPorukaR> {
        val kontaktMe = realm.query<OneContactR>("ime == $0", "Me").first().find()
            ?: return emptyList()

        val kontakt = realm.query<OneContactR>("idOneContact == $0", contactId).first().find()
            ?: return emptyList()

        return realm.query<ObicnaPorukaR>(
            """
        (kontaktKoSalje == $0 AND kontaktKomeSalje == $1) OR 
        (kontaktKoSalje == $1 AND kontaktKomeSalje == $0)
        """.trimIndent(),
            kontaktMe, kontakt
        )
            .sort("datum", Sort.DESCENDING)
            .find()
    }

    suspend fun getAllWhatsAppContacts(): List<WhatsAppKontaktR> {
        val currentID: Int = realm.query<ZlocinR>().max("idZlocin", Int::class).find() ?: 0
        val currentCrime = realm.query<ZlocinR>("idZlocin == $0", currentID).first().find()
        return realm.query<WhatsAppKontaktR>("zlocinId == $0", currentCrime).find()
    }

    suspend fun getContactsLastWhatsappMessages(): List<WhatsAppPreviewItem> {
        val kontaktMe = realm.query<WhatsAppKontaktR>("ime == $0", "Me").first().find()
            ?: return emptyList()

        val contacts = getAllWhatsAppContacts().filter { it.idWhatsAppKontakt != kontaktMe.idWhatsAppKontakt }

        return contacts.map { kontakt ->
            val lastMessage = realm.query<WhatsAppPorukaR>(
                """
                (kontaktKoSalje == $0 AND kontaktKomeSalje == $1) OR
                (kontaktKoSalje == $2 AND kontaktKomeSalje == $3)
                """.trimIndent(),
                kontaktMe, kontakt,
                kontakt, kontaktMe
            )
                .sort("datum", Sort.DESCENDING)
                .first()
                .find()

            WhatsAppPreviewItem(kontakt, lastMessage)
        }
    }

    suspend fun getWhatsappMessagesWithContact(contactId: Int): List<WhatsAppPorukaR> {
        val kontaktMe = realm.query<WhatsAppKontaktR>("ime == $0", "Me").first().find()
            ?: return emptyList()

        val kontakt = realm.query<WhatsAppKontaktR>("idWhatsAppKontakt == $0", contactId).first().find()
            ?: return emptyList()

        return realm.query<WhatsAppPorukaR>(
            """
        (kontaktKoSalje == $0 AND kontaktKomeSalje == $1) OR 
        (kontaktKoSalje == $1 AND kontaktKomeSalje == $0)
        """.trimIndent(),
            kontaktMe, kontakt
        )
            .sort("datum", Sort.DESCENDING)
            .find()
    }

    suspend fun getAllGalleryPhotos(): List<GalleryR> {
        val currentID: Int = realm.query<ZlocinR>().max("idZlocin", Int::class).find() ?: 0
        val currentCrime = realm.query<ZlocinR>("idZlocin == $0", currentID).first().find()

        return realm.query<GalleryR>("zlocinId == $0", currentCrime)
            .sort("datum", Sort.DESCENDING)
            .find()
    }

    suspend fun getMotiveAlibiStatus(osobaId: Int): List<String> {
        val suspect = realm.query<OsumnjicenR>("osobaId.idOsoba == $0", osobaId).first().find()
        val motiveDescription = suspect?.motiv?.opis ?: ""
        val alibi = realm.query<AlibiR>("osumnjicenId.idOsumnjicen == $0", suspect?.idOsumnjicen).first().find()
        val alibiDescription = alibi?.opis ?: ""
        val status = suspect?.status
        val statusDescription = if (suspect?.status == 0) "Oslobodjen"
                                else "Osumnjicen"
        return listOf<String>(motiveDescription, alibiDescription, statusDescription)
    }

    data class LekarskiTestRezultat (
        var ime: String,
        var datum: RealmInstant?,
        var pol: String,
        var izvestaj: String
    )

    suspend fun getLastLekarskiTest(): LekarskiTestRezultat? {
        val lastId = realm.query<LekarskiTestR>().max("idLekarskiTest", Int::class).find() ?: return null
        val test = realm.query<LekarskiTestR>("idLekarskiTest == $0", lastId).first().find() ?: return null

        val pacijent = test.pacijentId
        val zrtva = pacijent?.zrtvaId

        return LekarskiTestRezultat(
            ime = zrtva?.osobaId?.ime ?: "Nepoznato",
            datum = zrtva?.osobaId?.datum,
            pol = zrtva?.osobaId?.pol ?: "Nepoznat",
            izvestaj = test.izvestaj
        )
    }

    fun insertDataForMurder()  {
        viewModelScope.launch {
            val tipZlocina: TipZlocinaR? = inserTipZlocina("Murder")

            val zlocin: ZlocinR? = insertZlocin(
                1,
                tipZlocina,
                "Murder of Isabelle Moreau",
                "16.11.2023",
                "Hotel in Monte Carlo",
                "Murder of the famous businesswoman Isabelle Moreau under mysterious circumstances.",
                stZlocinR.u_istrazi.name
            )

            _uiStateZlocinSave.value = UiStateZlocinSave(zlocin)

            val zrtva: ZrtvaR? = insertZrtva(
                1,
                "Osoba",
                "Isabelle Moreau",
                "Poznata poslovna žena sa dugovima zbog kockarske zavisnosti.",
                StatusZrtvaR.mrtva.name,
                zlocin,
                "+377 126 599",
                RealmInstant.now(),
                "Isabelle Moreau is the heir to a wealthy hotel chain, recognized for her ambitious approach to business and her presence in social circles. Although she was respected and admired in the world of luxury tourism, Isabelle had a dark side – a battle with a gambling addiction that led to significant financial troubles. Her life was marked by constant attempts to hide her debts and avoid public shame. Despite her business success, her financial problems became increasingly apparent, creating tension in her relationships with colleagues and partners. " +
                        "The gambling addiction left a deep mark on her career and personal life, making her vulnerable and burdened by daily stress."
                ,"zenski"
            )

            val motivMarcoBellini: MotivR? =
                insertMotiv("Zrtva mu je dugovala novac zbog kockarske zavisnosti")
            val motivVincentDuval: MotivR? =
                insertMotiv("Ljubomora zbog Isabelleine veze sa njegovom ženom")
            val motivAmeliaFontaine: MotivR? =
                insertMotiv("Ljubomora, zavist i želja za osvetom zbog nepriznate ljubavi prema Marcu i osećaja manje vrednosti pored Isabelle")

            var osumnjiceniMarcoBellini: OsumnjicenR? = insertOsumnjiceni(
                1,
                "Marco Bellini",
                0,
                TipOsumnjicenR.pojedinac.name,
                motivMarcoBellini,
                zlocin,
                0,
                "+377 555 123 456",
                RealmInstant.now(),
                "Marco Bellini is a professional gambler known for his participation in high-stakes games. He was involved in several business ventures, often partnering with wealthy individuals. His addiction to gambling has put him in financial distress, leading to a strained relationship with Isabelle, who he owed money to. " +
                        "Marco's gambling addiction is a key factor that has contributed to his involvement in the investigation.",
                "muski"
            )
            var osumnjiceniVincentDuval: OsumnjicenR? = insertOsumnjiceni(
                2,
                "Vincent Duval",
                0,
                TipOsumnjicenR.pojedinac.name,
                motivVincentDuval,
                zlocin,
                0,
                "+377 555 987 654",
                RealmInstant.now(),
                "Vincent Duval is a luxury hotel manager and was once romantically involved with Isabelle. The relationship ended when Isabelle started a relationship with another man, which led to jealousy and frustration. " +
                        "Vincent’s emotional attachment to Isabelle and the subsequent bitterness over their breakup may have influenced his actions, making him a person of interest in the case.",
                "muski"
            )
            var osumnjiceniAmeliaFontaine: OsumnjicenR? = insertOsumnjiceni(
                3,
                "Amelia Fontaine",
                1,
                TipOsumnjicenR.pojedinac.name,
                motivAmeliaFontaine,
                zlocin,
                1,
                "+377 556 789 123",
                RealmInstant.now(),
                "Amelia Fontaine is a successful businesswoman who owns a luxury fashion brand. She had a complicated relationship with Isabelle, marked by envy and unrequited love for Marco Bellini. Amelia’s feelings of jealousy and resentment towards Isabelle’s success and her romantic interest in Marco might have led her to a dangerous path. " +
                        "Amelia’s motives were fueled by rivalry and deep emotional conflict.",
                "muski"
            )

            var dokaz1: DokazR? = insertDokaz(
                1,
                TipDokazaR.fizicki.name,
                "A bloody knife with the initials 'M.B.' was found at the crime scene.",
                zlocin,
                zrtva,
                1
            )
            var dokaz5: DokazR? = insertDokaz(
                2,
                TipDokazaR.fizicki.name,
                "Traces of skin were found on the knife.",
                zlocin,
                zrtva,
                0
            )

            var dokaz6: DokazR? = insertDokaz(
                3,
                TipDokazaR.fizicki.name,
                "A Queen of Hearts playing card was discovered hidden in Isabelle's hotel room.",
                zlocin,
                zrtva,
                0
            )
            var dokaz2: DokazR? = insertDokaz(
                4,
                TipDokazaR.digitalni.name,
                "Izabel je primala preteće poruke na WhatsApp, koje su kasnije povezane sa brojem telefona Marka Belinija",
                zlocin,
                zrtva,
                0
            )
            var dokaz3: DokazR? = insertDokaz(
                5,
                TipDokazaR.fizicki.name,
                "Krvavi nož sa inicijalima 'M.B.' pronađen je na mestu zločina, ali analiza DNK je otkrila da su tragovi kože na nožu pripadali Ameliji",
                zlocin,
                zrtva,
                0
            )
            var dokaz4: DokazR? = insertDokaz(
                6,
                TipDokazaR.digitalni.name,
                "Preteće poruke na WhatsApp bile su povezane sa brojem telefona Marka Belinija, ali se ispostavilo da ih je poslala Amelija koristeći drugi uređaj",
                zlocin,
                zrtva,
                0
            )

            var dokazOsumnjiceni1: DokazOsumnjicenR? =
                insertDokazOsumnjicenog(1,dokaz1, osumnjiceniAmeliaFontaine)
            var dokazOsumnjiceni2: DokazOsumnjicenR? =
                insertDokazOsumnjicenog(2,dokaz2, osumnjiceniAmeliaFontaine)
            var dokazOsumnjiceni3: DokazOsumnjicenR? =
                insertDokazOsumnjicenog(3,dokaz3, osumnjiceniAmeliaFontaine)
            var dokazOsumnjiceni4: DokazOsumnjicenR? =
                insertDokazOsumnjicenog(4,dokaz4, osumnjiceniAmeliaFontaine)

            var svedokAmeliaFontaine: SvedokR? = insertSvedok(1,
                "Amelia Fontaine", "+377 556 789",
                "Tvrdila je da je videla Marca u blizini sobe žrtve.",
                zlocin, StatusSvedokR.nesaradnja.name, 1,RealmInstant.now(),"Amelia Fontaine is a successful businesswoman who owns a luxury fashion brand. She had a complicated relationship with Isabelle, marked by envy and unrequited love for Marco Bellini. Amelia’s feelings of jealousy and resentment towards Isabelle’s success and her romantic interest in Marco might have led her to a dangerous path. " +
                        "Amelia’s motives were fueled by rivalry and deep emotional conflict.","zenski"
            )

            var alibiMarcoBellini: AlibiR? = insertAlibi(
                osumnjiceniMarcoBellini,
                null,
                "Marko tvrdi da je bio u kazinu tokom zločina, igrajući poker, ali nema dokaza da je bio za stolom u to vreme",
                StatusAlibijaR.lažan.name
            )
            var alibiAmeliaFontaine: AlibiR? = insertAlibi(
                osumnjiceniAmeliaFontaine,
                null,
                "Amelia je tvrdila da je bila u kazinu u vreme ubistva, ali bezbednosni snimci su pokazali da je napustila sobu neposredno pre zločina.",
                StatusAlibijaR.lažan.name
            )

            var misija: MisijaR? = insertMisija(
                zlocin,
                "Skrivena karta",
                "Korisnik je dobio poruku sa nepoznatog broja sa sadržajem: 'Znaš da je Marko samo pion. Prava istina je dublje zakopana. Potraži kartu Kraljice srca.'",
                0
            )

            var kontaktAmeliaFontaine: KontaktR? =
                insertKontakt(1,"Amelia Fontaine", "+377 556 789", 0, zrtva)

//            var porukaKorisniku: PorukeR? = insertPoruka(TipPorukeR.SMS.name, "Znaš da je Marko samo pion. Prava istina je dublje zakopana. Potraži kartu Kraljice srca.",
//                null, zrtva, kontaktAmeliaFontaine, StatusPorukeR.sent.name, false)

            var porukaKorisniku: PorukeR? = null

            var misijaPoruka: MisijaPorukaR? =
                insertMisijaPoruka(zlocin, "Skrivena karta", porukaKorisniku, 0, "Amelia Fontaine")

            var obdukcija: ObdukcijaR? = insertObdukcija(
                1,
                "Na telu su pronađeni tragovi samoodbrane, a smrt je nastupila usled višestrukih ubodnih rana u predelu grudnog koša.",
                "16.11.2023",
                "Višestruke ubodne rane",
                zrtva,
                "Na noktima žrtve pronađeni su ostaci kože, ali analiza DNK još uvek traje."
            )

            var forenzickiDokaz: ForenzickiDokazR? = insertForenzickiDokaz(
                1,
                TipForenzickiDokazR.DNK.name,
                "Traces of skin were found under the victim's fingernails. The analysis results are pending.",
                0,
                zrtva,
                "Potencijalna povezanost sa osumnjičenim Marcom Bellinijem."
            )

            var telefon: TelefonR? = insertTelefon(1,"iPhone 14 Pro", "iOS", zrtva, "4862")

            var odnosOsumnjicenZrtvaAF: OdnosOsumnjicenZrtvaR? = insertOdnosOsumnjicenZrtva(1,
                osumnjiceniAmeliaFontaine,
                zrtva,
                TipOdnosaR.rivalski.name
            )
            var odnosOsumnjicenZrtvaMB: OdnosOsumnjicenZrtvaR? =
                insertOdnosOsumnjicenZrtva(2,osumnjiceniMarcoBellini, zrtva, TipOdnosaR.poslovni.name)
            var odnosOsumnjicenZrtvaVD: OdnosOsumnjicenZrtvaR? =
                insertOdnosOsumnjicenZrtva(3,osumnjiceniVincentDuval, zrtva, TipOdnosaR.ljubavni.name)

            // Marco Bellini - General Questions
            insertPitanjeIspitivanjeOsumnjicenog(24,
                osumnjiceniMarcoBellini?.idOsumnjicen ?: -1,
                "opsta",
                "What was your relationship with Isabelle Moreau before her death?",
                "We had a business relationship. I didn’t know her personally, just met occasionally for work.",
                "His tone is flat, possibly trying to keep a distance from her. Quick answer, maybe rehearsed."
            )
            insertPitanjeIspitivanjeOsumnjicenog(1,
                osumnjiceniMarcoBellini?.idOsumnjicen ?: -1,
                "opsta",
                "Why did you feel Isabelle owed you money?",
                "She had gambling debts. It affected our relationship because she couldn’t pay me back.",
                "His answer is quick and automatic, seems like something he’s said before, trying to keep things simple."
            )
            // Marco Bellini - Alibi Questions
            insertPitanjeIspitivanjeOsumnjicenog(2,
                osumnjiceniMarcoBellini?.idOsumnjicen ?: -1,
                "alibi",
                "Where were you at the time of Isabelle’s murder?",
                "I was at the casino, playing poker. Never left the table at that time.",
                "No hesitation, but his answer feels too perfect, might be trying to cover up something."
            )
            insertPitanjeIspitivanjeOsumnjicenog(3,
                osumnjiceniMarcoBellini?.idOsumnjicen ?: -1,
                "alibi",
                "Did you leave the casino during the night Isabelle was killed?",
                "No, I didn’t leave. I played poker the whole time.",
                "His response is quick, but there's no room for details, which might suggest a defensive tone."
            )
            // Marco Bellini - Evidence Questions
            insertPitanjeIspitivanjeOsumnjicenog(4,
                osumnjiceniMarcoBellini?.idOsumnjicen ?: -1,
                "dokaz",
                "Did you know that threatening messages sent to Isabelle were linked to your number?",
                "That’s a mistake. I don’t know anything about those messages. Someone must have used my number.",
                "His answer is fast, possibly nervous about the connection to his number, but he tries to explain it away."
            )
            insertPitanjeIspitivanjeOsumnjicenog(5,
                osumnjiceniMarcoBellini?.idOsumnjicen ?: -1,
                "dokaz",
                "Is there any reason your initials would be linked to the knife found at the crime scene?",
                "I don’t know why it was there. I’ve never used that knife.",
                "He answers quickly, but his tone lacks confidence, could be worried about the knife."
            )
            // Marco Bellini - Contradiction Questions
            insertPitanjeIspitivanjeOsumnjicenog(6,
                osumnjiceniMarcoBellini?.idOsumnjicen ?: -1,
                "kontradikcija",
                "You said you were at the casino when Isabelle was killed, but witnesses didn’t see you. How do you explain that?",
                "It must have been a mistake. I was at the casino the whole time.",
                "His response is too quick, maybe trying to brush off the discrepancy. Sounds defensive."
            )
            insertPitanjeIspitivanjeOsumnjicenog(7,
                osumnjiceniMarcoBellini?.idOsumnjicen ?: -1,
                "kontradikcija",
                "There are claims you were seen leaving Isabelle’s room. Can you deny that?",
                "That’s an absolute lie. I was never in her room.",
                "Quick, defensive response. His confidence feels a bit forced."
            )

            // Vincent Duval - General Questions
            insertPitanjeIspitivanjeOsumnjicenog(8,
                osumnjiceniVincentDuval?.idOsumnjicen ?: -1,
                "opsta",
                "What was your relationship with Isabelle Moreau before her death?",
                "We were in a romantic relationship, though it wasn’t easy. Isabelle had her own world, and I was jealous.",
                "He’s emotional in his response, which could be revealing. His jealousy seems genuine but might have been a motive."
            )
            insertPitanjeIspitivanjeOsumnjicenog(9,
                osumnjiceniVincentDuval?.idOsumnjicen ?: -1,
                "opsta",
                "Did you ever have a serious conflict with Isabelle before her death?",
                "Yes, a few times. Her relationship with someone else made me uncontrollably jealous.",
                "He admits jealousy openly, which shows emotional involvement. It could be important for motive."
            )

            // Vincent Duval - Alibi Questions
            insertPitanjeIspitivanjeOsumnjicenog(10,
                osumnjiceniVincentDuval?.idOsumnjicen ?: -1,
                "alibi",
                "Where were you at the time of Isabelle’s murder?",
                "I was in the hotel, in my room. No one saw me.",
                "His answer is calm but feels a bit unsure. There’s a slight hesitation, as if trying to cover all bases."
            )
            insertPitanjeIspitivanjeOsumnjicenog(11,
                osumnjiceniVincentDuval?.idOsumnjicen ?: -1,
                "alibi",
                "Did you have any contact with Isabelle shortly before her murder?",
                "No. We hadn’t spoken in days.",
                "He tries to show distance, but the tone suggests there’s more behind their recent interactions."
            )

            // Vincent Duval - Evidence Questions
            insertPitanjeIspitivanjeOsumnjicenog(12,
                osumnjiceniVincentDuval?.idOsumnjicen ?: -1,
                "dokaz",
                "Were there any threatening messages or evidence connecting your number to Isabelle?",
                "I never sent threatening messages. This is all a lie.",
                "His answer is quick, but something about the directness feels like he’s deflecting."
            )
            insertPitanjeIspitivanjeOsumnjicenog(13,
                osumnjiceniVincentDuval?.idOsumnjicen ?: -1,
                "dokaz",
                "A knife with your initials was found at the crime scene. Can you explain that?",
                "I’ve never been near that knife. It must be a set-up.",
                "His answer comes quickly, but he doesn’t seem fully confident. He might be trying to explain away something troubling."
            )

            // Vincent Duval - Contradiction Questions
            insertPitanjeIspitivanjeOsumnjicenog(14,
                osumnjiceniVincentDuval?.idOsumnjicen ?: -1,
                "kontradikcija",
                "You said you were in your room when Isabelle was killed, but witnesses saw you near her. How do you explain that?",
                "It’s a mistake. I never left my room.",
                "He’s defensive, trying to deny everything. The quickness of his answer might be a sign of stress."
            )
            insertPitanjeIspitivanjeOsumnjicenog(15,
                osumnjiceniVincentDuval?.idOsumnjicen ?: -1,
                "kontradikcija",
                "There are claims that you were jealous because of Isabelle’s other relationships. Can you deny that?",
                "Jealousy wasn’t the reason for her death. That’s just gossip.",
                "He denies it too easily. His response feels a little too rehearsed."
            )

            // Amelia Fontaine - General Questions
            insertPitanjeIspitivanjeOsumnjicenog(16,
                osumnjiceniAmeliaFontaine?.idOsumnjicen ?: -1,
                "opsta",
                "How would you describe your relationship with Isabelle Moreau?",
                "Isabelle was a competitor, but also a friend. We were in different industries, so we didn’t have much conflict.",
                "She tries to keep it neutral. Doesn’t want to reveal too much about her real feelings."
            )
            insertPitanjeIspitivanjeOsumnjicenog(17,
                osumnjiceniAmeliaFontaine?.idOsumnjicen ?: -1,
                "opsta",
                "How did you feel about her business success?",
                "She was successful, no doubt. But honestly, sometimes it was hard to watch.",
                "There’s a slight edge to her answer, indicating some underlying resentment or jealousy."
            )

            // Amelia Fontaine - Alibi Questions
            insertPitanjeIspitivanjeOsumnjicenog(18,
                osumnjiceniAmeliaFontaine?.idOsumnjicen ?: -1,
                "alibi",
                "Where were you at the time of Isabelle’s murder?",
                "I was at home, working.",
                "Her answer is quick and simple. There’s no real detail to back it up, which seems suspicious."
            )
            insertPitanjeIspitivanjeOsumnjicenog(19,
                osumnjiceniAmeliaFontaine?.idOsumnjicen ?: -1,
                "alibi",
                "Did you have any contact with Isabelle right before her death?",
                "No, we hadn’t spoken for months.",
                "She makes it sound like they were distant, but the lack of emotion could mean there’s more beneath the surface."
            )

            // Amelia Fontaine - Evidence Questions
            insertPitanjeIspitivanjeOsumnjicenog(20,
                osumnjiceniAmeliaFontaine?.idOsumnjicen ?: -1,
                "dokaz",
                "Were there any messages or evidence linking you to threatening Isabelle?",
                "No, I never sent any threatening messages.",
                "Her response is quick, but there’s something about her tone that feels off. Almost too rehearsed."
            )
            insertPitanjeIspitivanjeOsumnjicenog(21,
                osumnjiceniAmeliaFontaine?.idOsumnjicen ?: -1,
                "dokaz",
                "A knife with your initials was found at the crime scene. Can you explain that?",
                "I don’t know why it was there. I’ve never used that knife.",
                "Her answer is calm, but the lack of a solid explanation raises suspicion."
            )

            // Amelia Fontaine - Contradiction Questions
            insertPitanjeIspitivanjeOsumnjicenog(22,
                osumnjiceniAmeliaFontaine?.idOsumnjicen ?: -1,
                "kontradikcija",
                "You said you were at home when Isabelle was killed, but witnesses saw you near the crime scene. How do you explain that?",
                "That’s not true. I was home, like I said.",
                "Her answer is too firm. Could be a defensive reaction, or maybe she’s hiding something."
            )
            insertPitanjeIspitivanjeOsumnjicenog(23,
                osumnjiceniAmeliaFontaine?.idOsumnjicen ?: -1,
                "kontradikcija",
                "There were rumors about your jealousy of Isabelle. Can you deny that?",
                "Jealousy wasn’t the reason for what happened. That’s just talk.",
                "Her response feels too dismissive. Maybe trying to push away the idea without fully confronting it."
            )

            var pitanje1 = insertPitanje(1,zlocin, "Who do you think planted the knife with the initials M.B.?")
            var pitanje2 = insertPitanje(2,zlocin, "With what object do you think the victim was killed?")
            var pitanje3 = insertPitanje(3,zlocin, "Who do you think is lying among the witnesses?")
            var pitanje4 = insertPitanje(4,zlocin, "Who do you think killed Isabelle Moreau?")

            insertOdogovor(1,pitanje1, "Marco Bellini", false, 25)
            insertOdogovor(2,pitanje1, "Vincent Duval", false, 25)
            insertOdogovor(3,pitanje1, "Amelia Fontaine", true, 50)

            insertOdogovor(4,pitanje2, "knife", true, 40)
            insertOdogovor(5,pitanje2, "strangled", false, 20)

            insertOdogovor(6,pitanje3, "Marco Bellini", false, 25)
            insertOdogovor(7,pitanje3, "Vincent Duval", false, 25)
            insertOdogovor(8,pitanje3, "Amelia Fontaine", true, 40)

            insertOdogovor(9,pitanje4, "Marco Bellini", false, 50)
            insertOdogovor(10,pitanje4, "Vincent Duval", false, 50)
            insertOdogovor(11,pitanje4, "Amelia Fontaine", true, 50)

            insertPitanjeIspitivanjeSvedoka(1,svedokAmeliaFontaine, "Amelia, can you tell us where you were at the time of the crime?", "I was in the casino, but I left the area shortly before Isabelle's body was found. I didn't think anything suspicious was happening at that moment.")
            insertPitanjeIspitivanjeSvedoka(2,svedokAmeliaFontaine, "You mentioned seeing Marco leaving Isabelle's room. When exactly did this happen?", "It was a few hours before Isabelle was found dead. I noticed Marco leaving her room, looking a bit nervous, but I didn’t hear anything unusual.")
            insertPitanjeIspitivanjeSvedoka(3,svedokAmeliaFontaine, "Did you hear any arguments or strange sounds coming from Isabelle's room?", "Yes, I did hear some shouting, but I couldn’t make out the words. It was loud enough to make me curious, but I didn’t want to interfere.")
            insertPitanjeIspitivanjeSvedoka(4,svedokAmeliaFontaine, "How did you feel about Isabelle?", "I was very jealous of her, to be honest. She had everything I ever wanted – Marco’s attention, and a life full of luxury. But I never acted on that jealousy, or so I thought.")
            insertPitanjeIspitivanjeSvedoka(5,svedokAmeliaFontaine, "Did you ever have any conflicts with Isabelle?", "There were times when I felt overlooked or belittled by Isabelle. She often flaunted her success, especially in front of Marco. But I never thought it would escalate to something like this.")
            insertPitanjeIspitivanjeSvedoka(6,svedokAmeliaFontaine, "You mentioned seeing Marco earlier in the casino. Can you describe his behavior?", "He was very tense, as if something was bothering him. He wasn’t his usual calm self. I didn’t think much of it at the time, but it now seems significant.")
            insertPitanjeIspitivanjeSvedoka(7,svedokAmeliaFontaine, "Do you know if Marco and Isabelle had any financial issues?", "From what I knew, Isabelle had some debts, especially from gambling. Marco also had a gambling problem, so I wouldn’t be surprised if there was financial tension between them.")
            insertPitanjeIspitivanjeSvedoka(8,svedokAmeliaFontaine, "Have you ever been in Isabelle's room?", "No, I’ve never been inside her room. But I’ve seen her come and go a few times, usually after big wins at the casino.")
            insertPitanjeIspitivanjeSvedoka(9,svedokAmeliaFontaine, "Why do you think Marco might be involved in Isabelle's death?", "Marco had a clear motive – money and gambling debts. But after hearing the details of the investigation, I’m starting to doubt his innocence. I didn’t know who else could have done it until the truth started coming out.")
            insertPitanjeIspitivanjeSvedoka(10,svedokAmeliaFontaine, "Amelia, do you know why you were specifically called to testify today?", "I believe it’s because I was one of the last people to see Isabelle and Marco before her death. My testimony about the argument and my observations could help clarify what happened.")


            var zl10 = insertZadatak(1,"New evidence found", "Queen of Hearts card hidden in Isabelle's hotel room", false, null, zlocin)
            val zl9 = insertZadatak(2,"Interrogate Amelia",
                "Question Amelia",
                false, zl10, zlocin
            )

            val zl8 = insertZadatak(
                3,"Find out who sent the threatening messages",
                "Study the report on the threatening messages",
                false, zl9, zlocin
            )

            val zl7 = insertZadatak(
                4,"Compare DNA with the traces under the victim's nails",
                "Compare the DNA traces with samples from the victim's body",
                false, zl8, zlocin
            )

            val zl6 = insertZadatak(
                5,"Knife analysis result received",
                "Study the report and find out who the DNA traces belong to",
                false, zl7, zlocin
            )


            val zl5 = insertZadatak(
                6,"Interrogate Marco",
                "Question Marco",
                false, zl6, zlocin
            )

            val zl4 = insertZadatak(
                7,"Interrogate Vincent",
                "Question Vincent",
                false, zl5, zlocin
            )

            val zl3 = insertZadatak(
                8,"Check the phone",
                "Study the contacts on the victim's phone",
                false, zl4, zlocin
            )

            val zl2 = insertZadatak(
                9,"Interview witnesses",
                "Question Amelia",
                false, zl3, zlocin
            )

            val zl0 = insertZadatak(
                10,"Knife with blood traces found",
                "Send the knife for analysis",
                false,
                zl2,
                zlocin
            )

            var dokazZadatakNoz=insertDokazZadatak(1,"Send Evidence for Analysis", dokaz1,false,zl0)

            var dokazZadatakNoz2=insertDokazZadatak(2,"Send Evidence for Analysis", dokaz5,false,zl6)

            var dokazKraljicaSrca = insertDokazZadatak(3,"View",dokaz6,false,zl10)

            var forenzickiDokazZadatak = insertForenzickiDokazZadatak(1,"Send",forenzickiDokaz,false,zl7)

            var ispitivanjeSvedokaZadatakAmelia=insertIspitivanjeSvedokaZadatak(1,svedokAmeliaFontaine,zl2,false)

            var telefonZadatak=insertTelefonZadatak(1,telefon,zl3, false)

            var ispitivanjeOsumnjicenogZadatakVincent = insertIspitivanjeOsumnjicenogZadatak(1,osumnjiceniVincentDuval,zl4,false)

            var ispitivanjeOsumnjicenogZadatakMarco = insertIspitivanjeOsumnjicenogZadatak(2,osumnjiceniMarcoBellini,zl5,false)

            var ispitivanjeOsumnjicenogZadatakAmelia = insertIspitivanjeOsumnjicenogZadatak(3,osumnjiceniAmeliaFontaine,zl9,false)
            var porukaZadatak = insertPorukeZadatak(null, zl8,false)

            // telefon zrtve

            insertBeleska(1,zlocin, "Found a lipstick stain on a glass in the victim's room.", RealmInstant.now())
            insertBeleska(2,zlocin, "A torn photograph was discovered behind a painting.", RealmInstant.from(Instant.parse("2025-04-17T11:18:41Z").epochSecond, Instant.parse("2025-04-17T11:18:41Z").nano))
            insertBeleska(3,zlocin, "The victim received a threatening letter two days before the murder.", RealmInstant.now())
            insertBeleska(4,zlocin, "Casino security footage shows a shadowy figure entering Isabelle’s room.", RealmInstant.now())
            insertBeleska(5,zlocin, "Blood drops found near the garden entrance.", RealmInstant.from(Instant.parse("2025-04-10T11:18:41Z").epochSecond, Instant.parse("2025-04-10T11:18:41Z").nano))

            val kontaktMarco = insertWhatsAppKontakt(1,zlocin, "Marco Bellini", "+33612345678", R.drawable.whatsapp_profile_picture)
            val kontaktVincent = insertWhatsAppKontakt(2,zlocin, "Vincent Duval", "+33612345678", R.drawable.whatsapp_profile_picture)
            val kontaktAmelia = insertWhatsAppKontakt(3,zlocin, "Amelia Fontaine", "+33612345678", R.drawable.whatsapp_profile_picture)
            val kontaktIsabelle = insertWhatsAppKontakt(4,zlocin, "Isabelle Moreau", "+33612345678", R.drawable.whatsapp_profile_picture)
            val kontaktLuc = insertWhatsAppKontakt(5,zlocin, "Luc Moreau", "+33612345678", R.drawable.whatsapp_profile_picture)
            val kontaktMe = insertWhatsAppKontakt(6,zlocin, "Me", "+33612345678", R.drawable.whatsapp_profile_picture)

            if (kontaktAmelia != null && kontaktMe != null) insertWhatsAppPoruka(1,kontaktAmelia, kontaktMe, "I can't believe what happened to Isabelle.", RealmInstant.from(Instant.parse("2025-04-10T11:18:41Z").epochSecond, Instant.parse("2025-04-10T11:18:41Z").nano), true)
            if (kontaktAmelia != null && kontaktMe != null) insertWhatsAppPoruka(2,kontaktMe, kontaktAmelia, "Me neither", RealmInstant.from(Instant.parse("2025-04-11T11:18:41Z").epochSecond, Instant.parse("2025-04-11T11:18:41Z").nano), true)
            if (kontaktVincent != null && kontaktMe != null) insertWhatsAppPoruka(3,kontaktVincent, kontaktMe, "We need to talk. Urgently.", RealmInstant.now(), false)
            if (kontaktVincent != null && kontaktMe != null) insertWhatsAppPoruka(4,kontaktMe, kontaktVincent, "Okay I agree. Let's met at the coffee place tomorrow at 5pm.", RealmInstant.now(), false)
            if (kontaktMarco != null && kontaktMe != null) insertWhatsAppPoruka(5,kontaktMarco, kontaktMe, "Meet me at the fountain tonight. Come alone.", RealmInstant.now(), true)
            if (kontaktLuc != null && kontaktMe != null) insertWhatsAppPoruka(6,kontaktLuc, kontaktMe, "I saw something... but I'm not sure what it means.", RealmInstant.now(), false)
            if (kontaktIsabelle != null && kontaktMe != null) insertWhatsAppPoruka(7,kontaktIsabelle, kontaktMe, "They’re watching me. I don’t feel safe anymore.", RealmInstant.now(), true)

            val kontaktJean = insertOneContact(1,zlocin, "Jean Rousseau", "+33612345678", null)
            val kontaktClaire = insertOneContact(2,zlocin, "Claire Dubois", "+33687654321", R.drawable.whatsapp_profile_picture)
            val kontaktHenri = insertOneContact(3,zlocin, "Henri Leclerc", "+33655556666", R.drawable.whatsapp_profile_picture)
            val kontaktNatalie = insertOneContact(4,zlocin, "Natalie Girard", "+33677778888", R.drawable.whatsapp_profile_picture)
            val kontaktJulien = insertOneContact(5,zlocin, "Julien Martin", "+33699990000", R.drawable.whatsapp_profile_picture)
            val obicanKontaktMe = insertOneContact(6,zlocin, "Me", "+33699990000", R.drawable.whatsapp_profile_picture)

            insertOneCall(1,kontaktJean, RealmInstant.now(), propustenC = false, dolazniC = true)
            insertOneCall(2,kontaktClaire, RealmInstant.now(), propustenC = true, dolazniC = false)
            insertOneCall(3,kontaktHenri, RealmInstant.now(), propustenC = false, dolazniC = false)
            insertOneCall(4,kontaktNatalie, RealmInstant.now(), propustenC = true, dolazniC = true)
            insertOneCall(5,kontaktJulien, RealmInstant.now(), propustenC = false, dolazniC = true)
            insertOneCall(6,kontaktJean, RealmInstant.from(Instant.parse("2025-04-17T11:18:41Z").epochSecond, Instant.parse("2025-04-17T11:18:41Z").nano), propustenC = false, dolazniC = true)
            insertOneCall(7,kontaktClaire, RealmInstant.from(Instant.parse("2025-04-17T11:18:41Z").epochSecond, Instant.parse("2025-04-17T11:18:41Z").nano), propustenC = true, dolazniC = false)
            insertOneCall(8,kontaktHenri, RealmInstant.from(Instant.parse("2025-04-10T11:18:41Z").epochSecond, Instant.parse("2025-04-10T11:18:41Z").nano), propustenC = false, dolazniC = false)
            insertOneCall(9,kontaktNatalie, RealmInstant.from(Instant.parse("2025-04-17T11:18:41Z").epochSecond, Instant.parse("2025-04-17T11:18:41Z").nano), propustenC = true, dolazniC = true)
            insertOneCall(10,kontaktJulien, RealmInstant.from(Instant.parse("2025-04-10T11:18:41Z").epochSecond, Instant.parse("2025-04-10T11:18:41Z").nano), propustenC = false, dolazniC = true)

            insertGalleryPhoto(1,zlocin, R.drawable.whatsapp_profile_picture, RealmInstant.now(), "Casino Lobby")
            insertGalleryPhoto(2,zlocin, R.drawable.whatsapp_profile_picture, RealmInstant.now(), "Victim's Room")
            insertGalleryPhoto(3,zlocin, R.drawable.whatsapp_profile_picture, RealmInstant.now(), "Garden Entrance")
            insertGalleryPhoto(4,zlocin, R.drawable.whatsapp_profile_picture, RealmInstant.now(), "Security Office")
            insertGalleryPhoto(5,zlocin, R.drawable.whatsapp_profile_picture, RealmInstant.now(), "Underground Parking")

            if (kontaktJean != null && obicanKontaktMe != null) insertObicnaPoruka(kontaktJean, obicanKontaktMe, "Do you really think it was Marco?", RealmInstant.now(), false)
            if (kontaktJean != null && obicanKontaktMe != null) insertObicnaPoruka(obicanKontaktMe, kontaktJean, "Yes, call me tomorrow at 8pm.", RealmInstant.now(), false)
            if (kontaktClaire != null && obicanKontaktMe != null) insertObicnaPoruka(kontaktClaire, obicanKontaktMe,"Someone is lying. I can feel it.", RealmInstant.now(), true)
            if (kontaktHenri != null && obicanKontaktMe != null) insertObicnaPoruka(kontaktHenri, obicanKontaktMe,"Isabelle mentioned something strange last night.", RealmInstant.now(), false)
            if (kontaktNatalie != null && obicanKontaktMe != null) insertObicnaPoruka(kontaktNatalie, obicanKontaktMe,"Don't tell anyone we met.", RealmInstant.now(), true)
            if (kontaktJulien != null && obicanKontaktMe != null) insertObicnaPoruka(kontaktJulien, obicanKontaktMe,"She was scared. That I’m sure of.", RealmInstant.now(), false)
        }
    }
    private var _uiStateMysteriousSymptoms = MutableStateFlow(UiStateMysteriousSymptoms())
    val uiStateMysteriousSymptoms: StateFlow<UiStateMysteriousSymptoms> = _uiStateMysteriousSymptoms

    fun insertDataForMysteriousSymptoms() {
        viewModelScope.launch {
            val tipZlocina: TipZlocinaR? = inserTipZlocina("Mysterious Symptoms")

            val zlocin: ZlocinR? = insertZlocin(
                1,
                tipZlocina,
                "Pacijent 0",
                "1746124800000",
                "Bolnica St Thomas' Hospital, London",
                "Pacijentkinja bez identiteta sa anomalijama moždane aktivnosti, bez pulsa",
                stZlocinR.u_istrazi.name
            )

            var osoba =insertOsoba(
                idOsobaO = 1,
                imeZ = "Nepoznata",
                kontaktZ = "N/A",
                datumZ = RealmInstant.now(),
                zanimanjeZ = "Nepoznato",
                polZ = "zenski",
                zlocinZ = zlocin
            )

            // MENJALA
            var zrtva: ZrtvaR? =null
            zrtva = insertZrtva(
                idZrtvaZ = 1,
                tipZ = "",
                imeZ = osoba?.ime ?: "Nepoznata",
                detaljiZ = "",
                statusZ = "ziva, bolesna",
                zlocinZ = zlocin,
                kontaktZ = osoba?.kontakt ?: "N/A",
                datumZ = osoba?.datum ?: RealmInstant.now(),
                zanimanjeZ = osoba?.zanimanje ?: "Nepoznato",
                polZ = osoba?.pol ?: "Nepoznat"
            )

            var pacijent:PacijentR? =null
            if (zlocin != null && osoba !=null && zrtva != null) {
                pacijent =insertPacijent(
                    idPacijentP = 1,
                    simptomiP = "Bez pulsa, ali očuvana moždana aktivnost",
                    statusPacijentaP = "ziva",
                    datumPrijaveP = RealmInstant.now(),
                    prijavioP = "Dr. Ana King",
                    zlocinP = zlocin,
                    zrtvaP = zrtva
                )
            }
            var osobaKojaJeNaslaPacijenta =insertOsoba(
                idOsobaO = 2,
                imeZ = "Dr. Ana King",
                kontaktZ = "+399 2149 453",
                datumZ = RealmInstant.now(),
                zanimanjeZ = "doktor",
                polZ = "zenski",
                zlocinZ = zlocin
            )
            var izjava: IzjavaZaPacijentaR?= null
            if (osobaKojaJeNaslaPacijenta != null && pacijent!= null) {
                izjava =insertIzjavaZaPacijenta(
                    idIzjavaZaPacijentaI = 1,
                    izjavaI = "Pronašla sam je kako leži na stepeništu iza stare zgrade kod bolnice. Nije reagovala, ali mi se učinilo da njene oči prate pokret. Nisam mogla da ostavim ženu u takvom stanju. Ne znam ko je, niti kako je dospela tamo. Nije imala nikakve dokumente kod sebe. Nisam videla nikoga u blizini, sve je delovalo jezivo tiho. Samo... imala je neki uređaj u ruci koji mi je ispao dok sam je unosila. Nestao je kad sam se vratila da ga potražim.",
                    pacijentIdI = pacijent,
                    osobaP = osobaKojaJeNaslaPacijenta
                )
            }

            if (pacijent != null) insertLekarskiTest(idLekarskiTestL = 1, pacijentIdL = pacijent, izvestajL =
            "Hematološki nalazi:\n" +
                    "- Hemoglobin: 146 g/L (135 – 180) – Normalna oksigenacija\n" +
                    "- Eritrociti: 4.7 x10¹²/L (4.3 – 5.9) – Normalna\n" +
                    "- Leukociti: 8.5 x10⁹/L (4.0 – 10.0) – Bez leukocitoze\n" +
                    "- Trombociti: 220 x10⁹/L (150 – 400) – Normalna hemostaza\n" +
                    "- Hematokrit: 0.43 (0.40 – 0.50) – Normalan\n" +
                    "- Sedimentacija (SE): 9 mm/h (0 – 15) – Nema zapaljenskog odgovora\n"
            )

            _uiStateMysteriousSymptoms.value = UiStateMysteriousSymptoms(zlocin,tipZlocina,pacijent,osoba, osobaKojaJeNaslaPacijenta,izjava)
        }
    }

    fun clearDatabase() {
        realm.writeBlocking {
            deleteAll()
        }
    }
}

data class UiStateUserData (
    val userExists: Boolean? = null
)

data class UiStateZlocinSave (
    val zlocin: ZlocinR? =null
)

data class UiStateMysteriousSymptoms (
    val zlocin: ZlocinR? =null,
    val tipZlocina: TipZlocinaR? =null,
    val pacijentR: PacijentR? = null,
    val osobaPacijent: OsobaR? =null,
    val osobaKojaJeNaslaPacijenta: OsobaR? =null,
    val izjavaZaPacijenta: IzjavaZaPacijentaR? =null
)

data class UiStateCrimeData (
    val title: String? = null,
    val date: String? = null,
    val place: String? = null,
    val description: String? = null
)

data class WhatsAppPreviewItem(
    val kontakt: WhatsAppKontaktR,
    val lastMessage: WhatsAppPorukaR?
)

data class OneContactPreviewItem(
    val kontakt: OneContactR,
    val lastMessage: ObicnaPorukaR?
)
