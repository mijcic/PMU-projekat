package rs.ac.bg.etf.projekat.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
import rs.ac.bg.etf.projekat.data.realm.PitanjeIspitivanjeOsumnjicenogR
import rs.ac.bg.etf.projekat.data.realm.PorukeR
import rs.ac.bg.etf.projekat.data.realm.PrijavljeniKorisnikR
import rs.ac.bg.etf.projekat.data.realm.StatusAlibijaR
import rs.ac.bg.etf.projekat.data.realm.StatusSvedokR
import rs.ac.bg.etf.projekat.data.realm.StatusZrtvaR
import rs.ac.bg.etf.projekat.data.realm.SvedokR
import rs.ac.bg.etf.projekat.data.realm.TelefonR
import rs.ac.bg.etf.projekat.data.realm.TipDokazaR
import rs.ac.bg.etf.projekat.data.realm.TipForenzickiDokazR
import rs.ac.bg.etf.projekat.data.realm.TipOdnosaR
import rs.ac.bg.etf.projekat.data.realm.TipOsumnjicenR
import rs.ac.bg.etf.projekat.data.realm.TipZlocinaR
import rs.ac.bg.etf.projekat.data.realm.ZlocinR
import rs.ac.bg.etf.projekat.data.realm.ZrtvaR
import rs.ac.bg.etf.projekat.data.realm.stZlocinR
import rs.ac.bg.etf.projekat.data.retrofit.models.MessageResponse
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RealmViewModel @Inject constructor(
    private val MyRepository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateUserData())
    val uiState : StateFlow<UiStateUserData> = _uiState

    private val _uiStateCrimeData = MutableStateFlow(UiStateCrimeData())
    val uiStateCrimeData : StateFlow<UiStateCrimeData> = _uiStateCrimeData

    suspend fun inserTipZlocina(nazivTZ: String): TipZlocinaR? {
        var tipZlocina: TipZlocinaR? = null
        realm.write {
            tipZlocina = query<TipZlocinaR>("nazivTipaZlocina == $0", nazivTZ).find().firstOrNull()

            if (tipZlocina == null) {
                val maxId = query<TipZlocinaR>().find().maxOfOrNull { it.idTipZlocina } ?: 0
                tipZlocina = TipZlocinaR().apply {
                    idTipZlocina = maxId + 1
                    nazivTipaZlocina = nazivTZ
                }
                copyToRealm(tipZlocina!!)
            }
        }
        return tipZlocina
    }

    suspend fun insertZlocin(tipZlocina: TipZlocinaR?, nazivZ: String, datumZ: String, mestoZ: String, opisZ: String, statusZ: String): ZlocinR? {
        var zlocin: ZlocinR? = null
        realm.write {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val localDate = LocalDate.parse(datumZ, formatter)
            val instantDate = localDate.atStartOfDay(ZoneOffset.UTC).toInstant()
            val realmInstantDate = RealmInstant.from(instantDate.epochSecond, instantDate.nano)

            // Ako tipZlocina nije unet u bazu, unesite ga
            val existingTipZlocina = query<TipZlocinaR>("nazivTipaZlocina == $0", tipZlocina?.nazivTipaZlocina).find().firstOrNull()
                ?: tipZlocina?.let {
                    copyToRealm(it)
                }

            zlocin = query<ZlocinR>("tipZlocinaId == $0 AND naziv == $1 AND datum == $2 AND mesto == $3 AND opis == $4 AND status == $5",
                existingTipZlocina, nazivZ, realmInstantDate, mestoZ, opisZ, statusZ).find().firstOrNull()
                ?: ZlocinR().apply {
                    idZlocin = (query<ZlocinR>().find().maxOfOrNull { it.idZlocin } ?: 0) + 1
                    tipZlocinaId = existingTipZlocina // Povezivanje sa tipom zločina
                    naziv = nazivZ
                    datum = realmInstantDate
                    mesto = mestoZ
                    opis = opisZ
                    status = statusZ
                }
            copyToRealm(zlocin!!)
        }
        return zlocin
    }

    suspend fun insertZrtva(tipZ: String, imeZ: String, detaljiZ: String, statusZ: String, zlocinZ: ZlocinR?): ZrtvaR? {
        var zrtva: ZrtvaR? = null
        realm.write {
            // Ako zlocin nije unet u bazu, unesite ga
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinZ?.idZlocin).find().firstOrNull()
                ?: zlocinZ?.let {
                    copyToRealm(it)
                }

            zrtva = query<ZrtvaR>("tipZrtve == $0 AND ime == $1 AND detalji == $2 AND statusZrtva == $3 AND zlocinId == $4",
                tipZ, imeZ, detaljiZ, statusZ, zlocinZ).find().firstOrNull()
                ?: ZrtvaR().apply {
                    idZrtva = (query<ZrtvaR>().find().maxOfOrNull { it.idZrtva } ?: 0) + 1
                    tipZrtve = tipZ
                    ime = imeZ
                    detalji = detaljiZ
                    statusZrtva = statusZ
                    zlocinId = zlocinZ
                }
            copyToRealm(zrtva!!)
        }
        return zrtva
    }

    suspend fun insertMotiv(opisM: String): MotivR? {
        var motiv: MotivR? = null
        realm.write {
            motiv = query<MotivR>("opis == $0", opisM).find().firstOrNull()
                ?: MotivR().apply {
                    idMotiv = (query<MotivR>().find().maxOfOrNull { it.idMotiv } ?: 0) + 1
                    opis = opisM
                }
            copyToRealm(motiv!!)
        }
        return motiv
    }

    suspend fun insertOsumnjiceni(imeO: String, statusO: Int, tipOsumnjicenO: String, motivO: MotivR?, zlocinO: ZlocinR?, krivO: Int): OsumnjicenR? {
        var osumnjiceni: OsumnjicenR? = null
        realm.write {
            // Ako motiv nije unet u bazu, unesite ga
            val existingMotiv = query<MotivR>("idMotiv == $0", motivO?.idMotiv).find().firstOrNull()
                ?: motivO?.let {
                    copyToRealm(it)
                }

            // Ako zlocin nije unet u bazu, unesite ga
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinO?.idZlocin).find().firstOrNull()
                ?: zlocinO?.let {
                    copyToRealm(it)
                }

            osumnjiceni = query<OsumnjicenR>("ime == $0 AND status == $1 AND tipOsumnjicen == $2 AND motiv == $3 AND zlocinId == $4 AND kriv == $5",
                imeO, statusO, tipOsumnjicenO, existingMotiv, existingZlocin, krivO).find().firstOrNull()
                ?: OsumnjicenR().apply {
                    idOsumnjicen = (query<OsumnjicenR>().find().maxOfOrNull { it.idOsumnjicen } ?: 0) + 1
                    ime = imeO
                    status = statusO
                    tipOsumnjicen = tipOsumnjicenO
                    motiv = existingMotiv
                    zlocinId = existingZlocin
                    kriv = krivO
                }
            copyToRealm(osumnjiceni!!)
        }
        return osumnjiceni
    }

    suspend fun insertDokaz(tipDokazaD: String, opisD: String, zlocinD: ZlocinR?, zrtvaD: ZrtvaR?, statusD: Int): DokazR? {
        var dokaz: DokazR? = null
        realm.write {
            // Ako zlocin nije unet u bazu, unesite ga
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinD?.idZlocin).find().firstOrNull()
                ?: zlocinD?.let {
                    copyToRealm(it)
                }

            // Ako zrtva nije uneta u bazu, unesite je
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaD?.idZrtva).find().firstOrNull()
                ?: zrtvaD?.let {
                    copyToRealm(it)
                }

            dokaz = query<DokazR>("tipDokaza == $0 AND opis == $1 AND zlocinId == $2 AND zrtvaId == $3 AND status == $4",
                tipDokazaD, opisD, existingZlocin, existingZrtva, statusD).find().firstOrNull()
                ?: DokazR().apply {
                    idDokaz = (query<DokazR>().find().maxOfOrNull { it.idDokaz } ?: 0) + 1
                    tipDokaza = tipDokazaD
                    opis = opisD
                    zlocinId = existingZlocin
                    zrtvaId = existingZrtva
                    status = statusD
                }
            copyToRealm(dokaz!!)
        }
        return dokaz
    }

    suspend fun insertDokazOsumnjicenog(dokazIdDO: DokazR?, osumnjicenIdDO: OsumnjicenR?): DokazOsumnjicenR? {
        var dokazOsumnjicenog: DokazOsumnjicenR? = null
        realm.write {
            // Ako dokaz nije unet u bazu, unesite ga
            val existingDokaz = query<DokazR>("idDokaz == $0", dokazIdDO?.idDokaz).find().firstOrNull()
                ?: dokazIdDO?.let {
                    copyToRealm(it)
                }

            // Ako osumnjiceni nije unet u bazu, unesite ga
            val existingOsumnjiceni = query<OsumnjicenR>("idOsumnjicen == $0", osumnjicenIdDO?.idOsumnjicen).find().firstOrNull()
                ?: osumnjicenIdDO?.let {
                    copyToRealm(it)
                }

            dokazOsumnjicenog = query<DokazOsumnjicenR>("dokazId == $0 AND osumnjicenId == $1", existingDokaz, existingOsumnjiceni).find().firstOrNull()
                ?: DokazOsumnjicenR().apply {
                    idDokazOsumnjicen = (query<DokazOsumnjicenR>().find().maxOfOrNull { it.idDokazOsumnjicen } ?: 0) + 1
                    dokazId = existingDokaz
                    osumnjicenId = existingOsumnjiceni
                }
            copyToRealm(dokazOsumnjicenog!!)
        }
        return dokazOsumnjicenog
    }

    suspend fun insertSvedok(imeS: String, kontaktS: String, izjavaS: String, zlocinS: ZlocinR?, statusSvedokS: String, statusIspitanS: Int): SvedokR? {
        var svedok: SvedokR? = null
        realm.write {
            // Ako zlocin nije unet u bazu, unesite ga
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinS?.idZlocin).find().firstOrNull()
                ?: zlocinS?.let {
                    copyToRealm(it)
                }

            svedok = query<SvedokR>("ime == $0 AND kontakt == $1 AND izjava == $2 AND zlocinId == $3 AND statusSvedok == $4 AND statusIspitan == $5",
                imeS, kontaktS, izjavaS, existingZlocin, statusSvedokS, statusIspitanS).find().firstOrNull()
                ?: SvedokR().apply {
                    idSvedok = (query<SvedokR>().find().maxOfOrNull { it.idSvedok } ?: 0) + 1
                    ime = imeS
                    kontakt = kontaktS
                    izjava = izjavaS
                    zlocinId = existingZlocin
                    statusSvedok = statusSvedokS
                    statusIspitan = statusIspitanS
                }
            copyToRealm(svedok!!)
        }
        return svedok
    }

    suspend fun insertAlibi(osumnjicenA: OsumnjicenR?, svedokA: SvedokR?, opisA: String, statusAlibijaA: String): AlibiR? {
        var alibi: AlibiR? = null
        realm.write {
            // Ako osumnjiceni nije unet u bazu, unesite ga
            val existingOsumnjiceni = query<OsumnjicenR>("idOsumnjicen == $0", osumnjicenA?.idOsumnjicen).find().firstOrNull()
                ?: osumnjicenA?.let {
                    copyToRealm(it)
                }

            // Ako svedok nije unet u bazu, unesite ga
            val existingSvedok = query<SvedokR>("idSvedok == $0", svedokA?.idSvedok).find().firstOrNull()
                ?: svedokA?.let {
                    copyToRealm(it)
                }

            alibi = query<AlibiR>("osumnjicenId == $0 AND svedokId == $1 AND opis == $2 AND statusAlibija == $3",
                existingOsumnjiceni, existingSvedok, opisA, statusAlibijaA).find().firstOrNull()
                ?: AlibiR().apply {
                    idAlibi = (query<AlibiR>().find().maxOfOrNull { it.idAlibi } ?: 0) + 1
                    osumnjicenId = existingOsumnjiceni
                    svedokId = existingSvedok
                    opis = opisA
                    statusAlibija = statusAlibijaA
                }
            copyToRealm(alibi!!)
        }
        return alibi
    }

    suspend fun insertMisija(zlocinM: ZlocinR?, nazivM: String, opisM: String, statusM: Int): MisijaR? {
        var misija: MisijaR? = null
        realm.write {
            // Ako zlocin nije unet u bazu, unesite ga
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinM?.idZlocin).find().firstOrNull()
                ?: zlocinM?.let {
                    copyToRealm(it)
                }

            misija = query<MisijaR>("zlocinId == $0 AND naziv == $1 AND opis == $2 AND status == $3",
                existingZlocin, nazivM, opisM, statusM).find().firstOrNull()
                ?: MisijaR().apply {
                    idMisija = (query<MisijaR>().find().maxOfOrNull { it.idMisija } ?: 0) + 1
                    zlocinId = existingZlocin
                    naziv = nazivM
                    opis = opisM
                    status = statusM
                }
            copyToRealm(misija!!)
        }
        return misija
    }

    suspend fun insertKontakt(imeK: String, brojK: String, statusK: Int, zrtvaK: ZrtvaR?): KontaktR? {
        var kontakt: KontaktR? = null
        realm.write {
            // Ako zrtva nije uneta u bazu, unesite je
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaK?.idZrtva).find().firstOrNull()
                ?: zrtvaK?.let {
                    copyToRealm(it)
                }

            kontakt = query<KontaktR>("ime == $0 AND broj == $1 AND status == $2 AND zrtvaId == $3",
                imeK, brojK, statusK, existingZrtva).find().firstOrNull()
                ?: KontaktR().apply {
                    idKontakt = (query<KontaktR>().find().maxOfOrNull { it.idKontakt } ?: 0) + 1
                    ime = imeK
                    broj = brojK
                    status = statusK
                    zrtvaId = existingZrtva
                }
            copyToRealm(kontakt!!)
        }
        return kontakt
    }

    suspend fun insertPoruka(tipP: String, sadrzajP: String, datumVremeP: RealmInstant?, zrtvaP: ZrtvaR?, posiljalacP: KontaktR?, statusP: String, sifrovanaP: Boolean): PorukeR? {
        var poruka: PorukeR? = null
        realm.write {
            // Ako zrtva nije uneta u bazu, unesite je
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaP?.idZrtva).find().firstOrNull()
                ?: zrtvaP?.let {
                    copyToRealm(it)
                }

            // Ako posiljalac nije unet u bazu, unesite ga
            val existingPosiljalac = query<KontaktR>("idKontakt == $0", posiljalacP?.idKontakt).find().firstOrNull()
                ?: posiljalacP?.let {
                    copyToRealm(it)
                }

            poruka = query<PorukeR>("tipPoruke == $0 AND sadrzaj == $1 AND datumVreme == $2 AND zrtvaId == $3 AND posiljalacId == $4 AND statusPoruke == $5 AND sifrovana == $6",
                tipP, sadrzajP, datumVremeP, existingZrtva, existingPosiljalac, statusP, sifrovanaP).find().firstOrNull()
                ?: PorukeR().apply {
                    idPoruke = (query<PorukeR>().find().maxOfOrNull { it.idPoruke } ?: 0) + 1
                    tipPoruke = tipP
                    sadrzaj = sadrzajP
                    datumVreme = datumVremeP
                    zrtvaId = existingZrtva
                    posiljalacId = existingPosiljalac
                    statusPoruke = statusP
                    sifrovana = sifrovanaP
                }
            copyToRealm(poruka!!)
        }
        return poruka
    }

    suspend fun insertMisijaPoruka(zlocinMP: ZlocinR?, nazivMP: String, porukaMP: PorukeR?, statusMP: Int, posiljalacMP: String): MisijaPorukaR? {
        var misijaPoruka: MisijaPorukaR? = null
        realm.write {
            // Ako zlocin nije unet u bazu, unesite ga
            val existingZlocin = query<ZlocinR>("idZlocin == $0", zlocinMP?.idZlocin).find().firstOrNull()
                ?: zlocinMP?.let {
                    copyToRealm(it)
                }

            // Ako poruka nije uneta u bazu, unesite je
            val existingPoruka = query<PorukeR>("idPoruke == $0", porukaMP?.idPoruke).find().firstOrNull()
                ?: porukaMP?.let {
                    copyToRealm(it)
                }

            misijaPoruka = query<MisijaPorukaR>("zlocinId == $0 AND naziv == $1 AND poruka == $2 AND status == $3 AND posiljalac == $4",
                existingZlocin, nazivMP, existingPoruka, statusMP, posiljalacMP).find().firstOrNull()
                ?: MisijaPorukaR().apply {
                    idMisija = (query<MisijaPorukaR>().find().maxOfOrNull { it.idMisija } ?: 0) + 1
                    zlocinId = existingZlocin
                    naziv = nazivMP
                    poruka = existingPoruka
                    status = statusMP
                    posiljalac = posiljalacMP
                }
            copyToRealm(misijaPoruka!!)
        }
        return misijaPoruka
    }

    suspend fun insertObdukcija(izvestajO: String, datumO: String, uzrokSmrtiO: String, zrtvaO: ZrtvaR?, informacijeO: String): ObdukcijaR? {
        var obdukcija: ObdukcijaR? = null
        realm.write {
            // Ako zrtva nije uneta u bazu, unesite je
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaO?.idZrtva).find().firstOrNull()
                ?: zrtvaO?.let {
                    copyToRealm(it)
                }

            val formatterO = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val localDateO = LocalDate.parse(datumO, formatterO)

            val instantDateO = localDateO.atStartOfDay(ZoneOffset.UTC).toInstant()

            val realmInstantDateO = RealmInstant.from(
                instantDateO.epochSecond,
                instantDateO.nano
            )

            obdukcija = query<ObdukcijaR>("izvestaj == $0 AND datum == $1 AND uzrokSmrti == $2 AND zrtvaId == $3 AND informacije == $4",
                izvestajO, realmInstantDateO, uzrokSmrtiO, existingZrtva, informacijeO).find().firstOrNull()
                ?: ObdukcijaR().apply {
                    idObdukcija = (query<ObdukcijaR>().find().maxOfOrNull { it.idObdukcija } ?: 0) + 1
                    izvestaj = izvestajO
                    datum = realmInstantDateO
                    uzrokSmrti = uzrokSmrtiO
                    zrtvaId = existingZrtva
                    informacije = informacijeO
                }
            copyToRealm(obdukcija!!)
        }
        return obdukcija
    }

    suspend fun insertForenzickiDokaz(tipFD: String, opisFD: String, statusFD: Int, zrtvaFD: ZrtvaR?, vezaFD: String): ForenzickiDokazR? {
        var forenzickiDokaz: ForenzickiDokazR? = null
        realm.write {
            // Ako zrtva nije uneta u bazu, unesite je
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaFD?.idZrtva).find().firstOrNull()
                ?: zrtvaFD?.let {
                    copyToRealm(it)
                }

            forenzickiDokaz = query<ForenzickiDokazR>("tipForenzickiDokaz == $0 AND opis == $1 AND status == $2 AND zrtvaId == $3 AND veza == $4",
                tipFD, opisFD, statusFD, existingZrtva, vezaFD).find().firstOrNull()
                ?: ForenzickiDokazR().apply {
                    idForenzickiDokaz = (query<ForenzickiDokazR>().find().maxOfOrNull { it.idForenzickiDokaz } ?: 0) + 1
                    tipForenzickiDokaz = tipFD
                    opis = opisFD
                    status = statusFD
                    zrtvaId = existingZrtva
                    veza = vezaFD
                }
            copyToRealm(forenzickiDokaz!!)
        }
        return forenzickiDokaz
    }

    suspend fun insertTelefon(modelT: String, osT: String, zrtvaT: ZrtvaR?, sifraT: String): TelefonR? {
        var telefon: TelefonR? = null
        realm.write {
            // Ako zrtva nije uneta u bazu, unesite je
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaT?.idZrtva).find().firstOrNull()
                ?: zrtvaT?.let {
                    copyToRealm(it)
                }

            telefon = query<TelefonR>("model == $0 AND os == $1 AND zrtvaId == $2 AND sifra == $3",
                modelT, osT, zrtvaT, sifraT).find().firstOrNull()
                ?: TelefonR().apply {
                    idTelefon = (query<TelefonR>().find().maxOfOrNull { it.idTelefon } ?: 0) + 1
                    model = modelT
                    os = osT
                    zrtvaId = existingZrtva
                    sifra = sifraT
                }
            copyToRealm(telefon!!)
        }
        return telefon
    }

    suspend fun insertOdnosOsumnjicenZrtva(osumnjicenOOZ: OsumnjicenR?, zrtvaOOZ: ZrtvaR?, tipOdnosaOOZ: String): OdnosOsumnjicenZrtvaR? {
        var odnos: OdnosOsumnjicenZrtvaR? = null
        realm.write {
            // Ako osumnjiceni nije unet u bazu, unesite ga
            val existingOsumnjiceni = query<OsumnjicenR>("idOsumnjicen == $0", osumnjicenOOZ?.idOsumnjicen).find().firstOrNull()
                ?: osumnjicenOOZ?.let {
                    copyToRealm(it)
                }

            // Ako zrtva nije uneta u bazu, unesite je
            val existingZrtva = query<ZrtvaR>("idZrtva == $0", zrtvaOOZ?.idZrtva).find().firstOrNull()
                ?: zrtvaOOZ?.let {
                    copyToRealm(it)
                }

            odnos = query<OdnosOsumnjicenZrtvaR>("osumnjicenId == $0 AND zrtvaId == $1 AND tipOdnosa == $2", existingOsumnjiceni, existingZrtva, tipOdnosaOOZ).find().firstOrNull()
                ?: OdnosOsumnjicenZrtvaR().apply {
                    idOdnos = (query<OdnosOsumnjicenZrtvaR>().find().maxOfOrNull { it.idOdnos } ?: 0) + 1
                    osumnjicenId = existingOsumnjiceni
                    zrtvaId = existingZrtva
                    tipOdnosa = tipOdnosaOOZ
                }
            copyToRealm(odnos!!)
        }
        return odnos
    }

    fun checkIfUserExists() {
        val userExists = realm.query<PrijavljeniKorisnikR>().count().find() > 0
        _uiState.value = UiStateUserData(userExists)
    }

    suspend fun insertPrijavljeniKorisnik(korisnickoImePK: String, sifraPK: String) {
        var prijavljeniKorisnik: PrijavljeniKorisnikR? = null
        realm.write {
            prijavljeniKorisnik = query<PrijavljeniKorisnikR>("korisnickoIme == $0 AND sifra == $1", korisnickoImePK, sifraPK).find().firstOrNull()
                ?: PrijavljeniKorisnikR().apply {
                    idKorisnik = (query<PrijavljeniKorisnikR>().find().maxOfOrNull { it.idKorisnik } ?: 0) + 1
                    korisnickoIme = korisnickoImePK
                    sifra = sifraPK
                }
            copyToRealm(prijavljeniKorisnik!!)
        }
    }

    suspend fun insertPitanjeIspitivanjeOsumnjicenog(osumnjicenZ: OsumnjicenR?, kategorijaZ: String, tekstZ: String, odgovorZ: String, komentarZ: String): PitanjeIspitivanjeOsumnjicenogR? {
        var pitanje: PitanjeIspitivanjeOsumnjicenogR? = null
        realm.write {
            // If the suspect is not in the database, insert it
            val existingOsumnjicen = query<OsumnjicenR>("idOsumnjicen == $0", osumnjicenZ?.idOsumnjicen).find().firstOrNull()
                ?: osumnjicenZ?.let {
                    copyToRealm(it)
                }

            // Check if the question already exists based on the provided data
            pitanje = query<PitanjeIspitivanjeOsumnjicenogR>("kategorija == $0 AND tekst == $1 AND odgovor == $2 AND komentar == $3 AND osumnjicenId == $4",
                kategorijaZ, tekstZ, odgovorZ, komentarZ, osumnjicenZ).find().firstOrNull()
                ?: PitanjeIspitivanjeOsumnjicenogR().apply {
                    // Ensure the primary key is unique by querying the maximum id in PitanjeIspitivanjeOsumnjicenogR
                    idPitanjeIspitivanjeOsumnjicenog = (query<PitanjeIspitivanjeOsumnjicenogR>().find().maxOfOrNull { it.idPitanjeIspitivanjeOsumnjicenog } ?: 0) + 1
                    kategorija = kategorijaZ
                    tekst = tekstZ
                    odgovor = odgovorZ
                    komentar = komentarZ
                    osumnjicenId = osumnjicenZ
                }

            copyToRealm(pitanje!!)
        }
        return pitanje
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
    }

    fun insertDataForMurder()  {
        viewModelScope.launch {
            val tipZlocina: TipZlocinaR? = inserTipZlocina("Murder")

            val zlocin: ZlocinR? = insertZlocin(
                tipZlocina,
                "Ubistvo Isabelle Moreau",
                "16.11.2023",
                "Hotel u Monte Karlu",
                "Ubistvo poznate poslovne žene Isabelle Moreau pod misterioznim okolnostima.",
                stZlocinR.u_istrazi.name
            )

            val zrtva: ZrtvaR? = insertZrtva(
                "Osoba",
                "Isabelle Moreau",
                "Poznata poslovna žena sa dugovima zbog kockarske zavisnosti.",
                StatusZrtvaR.mrtva.name,
                zlocin
            )

            val motivMarcoBellini: MotivR? =
                insertMotiv("Zrtva mu je dugovala novac zbog kockarske zavisnosti")
            val motivVincentDuval: MotivR? =
                insertMotiv("Ljubomora zbog Isabelleine veze sa njegovom ženom")
            val motivAmeliaFontaine: MotivR? =
                insertMotiv("Ljubomora, zavist i želja za osvetom zbog nepriznate ljubavi prema Marcu i osećaja manje vrednosti pored Isabelle")

            var osumnjiceniMarcoBellini: OsumnjicenR? = insertOsumnjiceni(
                "Marco Bellini",
                0,
                TipOsumnjicenR.pojedinac.name,
                motivMarcoBellini,
                zlocin,
                0
            )
            var osumnjiceniVincentDuval: OsumnjicenR? = insertOsumnjiceni(
                "Vincent Duval",
                0,
                TipOsumnjicenR.pojedinac.name,
                motivVincentDuval,
                zlocin,
                0
            )
            var osumnjiceniAmeliaFontaine: OsumnjicenR? = insertOsumnjiceni(
                "Amelia Fontaine",
                1,
                TipOsumnjicenR.pojedinac.name,
                motivAmeliaFontaine,
                zlocin,
                1
            )

            var dokaz1: DokazR? = insertDokaz(
                TipDokazaR.fizicki.name,
                "Krvavi nož sa inicijalima 'M.B.' pronađen je na mestu zločina",
                zlocin,
                zrtva,
                0
            )
            var dokaz2: DokazR? = insertDokaz(
                TipDokazaR.digitalni.name,
                "Izabel je primala preteće poruke na WhatsApp, koje su kasnije povezane sa brojem telefona Marka Belinija",
                zlocin,
                zrtva,
                0
            )
            var dokaz3: DokazR? = insertDokaz(
                TipDokazaR.fizicki.name,
                "Krvavi nož sa inicijalima 'M.B.' pronađen je na mestu zločina, ali analiza DNK je otkrila da su tragovi kože na nožu pripadali Ameliji",
                zlocin,
                zrtva,
                1
            )
            var dokaz4: DokazR? = insertDokaz(
                TipDokazaR.digitalni.name,
                "Preteće poruke na WhatsApp bile su povezane sa brojem telefona Marka Belinija, ali se ispostavilo da ih je poslala Amelija koristeći drugi uređaj",
                zlocin,
                zrtva,
                1
            )

            var dokazOsumnjiceni1: DokazOsumnjicenR? =
                insertDokazOsumnjicenog(dokaz1, osumnjiceniAmeliaFontaine)
            var dokazOsumnjiceni2: DokazOsumnjicenR? =
                insertDokazOsumnjicenog(dokaz2, osumnjiceniAmeliaFontaine)
            var dokazOsumnjiceni3: DokazOsumnjicenR? =
                insertDokazOsumnjicenog(dokaz3, osumnjiceniAmeliaFontaine)
            var dokazOsumnjiceni4: DokazOsumnjicenR? =
                insertDokazOsumnjicenog(dokaz4, osumnjiceniAmeliaFontaine)

            var svedokAmeliaFontaine: SvedokR? = insertSvedok(
                "Amelia Fontaine", "+377 556 789",
                "Tvrdila je da je videla Marca u blizini sobe žrtve.",
                zlocin, StatusSvedokR.nesaradnja.name, 1
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
                insertKontakt("Amelia Fontaine", "+377 556 789", 0, zrtva)

//            var porukaKorisniku: PorukeR? = insertPoruka(TipPorukeR.SMS.name, "Znaš da je Marko samo pion. Prava istina je dublje zakopana. Potraži kartu Kraljice srca.",
//                null, zrtva, kontaktAmeliaFontaine, StatusPorukeR.sent.name, false)

            var porukaKorisniku: PorukeR? = null

            var misijaPoruka: MisijaPorukaR? =
                insertMisijaPoruka(zlocin, "Skrivena karta", porukaKorisniku, 0, "Amelia Fontaine")

            var obdukcija: ObdukcijaR? = insertObdukcija(
                "Na telu su pronađeni tragovi samoodbrane, a smrt je nastupila usled višestrukih ubodnih rana u predelu grudnog koša.",
                "16.11.2023",
                "Višestruke ubodne rane",
                zrtva,
                "Na noktima žrtve pronađeni su ostaci kože, ali analiza DNK još uvek traje."
            )

            var forenzickiDokaz: ForenzickiDokazR? = insertForenzickiDokaz(
                TipForenzickiDokazR.DNK.name,
                "Na noktima žrtve pronađeni su ostaci kože. Čeka se rezultat analize.",
                0,
                zrtva,
                "Potencijalna povezanost sa osumnjičenim Marcom Bellinijem."
            )

            var telefon: TelefonR? = insertTelefon("iPhone 14 Pro", "iOS", zrtva, "4862")

            var odnosOsumnjicenZrtvaAF: OdnosOsumnjicenZrtvaR? = insertOdnosOsumnjicenZrtva(
                osumnjiceniAmeliaFontaine,
                zrtva,
                TipOdnosaR.rivalski.name
            )
            var odnosOsumnjicenZrtvaMB: OdnosOsumnjicenZrtvaR? =
                insertOdnosOsumnjicenZrtva(osumnjiceniMarcoBellini, zrtva, TipOdnosaR.poslovni.name)
            var odnosOsumnjicenZrtvaVD: OdnosOsumnjicenZrtvaR? =
                insertOdnosOsumnjicenZrtva(osumnjiceniVincentDuval, zrtva, TipOdnosaR.ljubavni.name)

            // Marco Bellini - General Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniMarcoBellini,
                "opsta",
                "What was your relationship with Isabelle Moreau before her death?",
                "We had a business relationship. I didn’t know her personally, just met occasionally for work.",
                "His tone is flat, possibly trying to keep a distance from her. Quick answer, maybe rehearsed."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniMarcoBellini,
                "opsta",
                "Why did you feel Isabelle owed you money?",
                "She had gambling debts. It affected our relationship because she couldn’t pay me back.",
                "His answer is quick and automatic, seems like something he’s said before, trying to keep things simple."
            )
            // Marco Bellini - Alibi Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniMarcoBellini,
                "alibi",
                "Where were you at the time of Isabelle’s murder?",
                "I was at the casino, playing poker. Never left the table at that time.",
                "No hesitation, but his answer feels too perfect, might be trying to cover up something."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniMarcoBellini,
                "alibi",
                "Did you leave the casino during the night Isabelle was killed?",
                "No, I didn’t leave. I played poker the whole time.",
                "His response is quick, but there's no room for details, which might suggest a defensive tone."
            )
            // Marco Bellini - Evidence Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniMarcoBellini,
                "dokaz",
                "Did you know that threatening messages sent to Isabelle were linked to your number?",
                "That’s a mistake. I don’t know anything about those messages. Someone must have used my number.",
                "His answer is fast, possibly nervous about the connection to his number, but he tries to explain it away."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniMarcoBellini,
                "dokaz",
                "Is there any reason your initials would be linked to the knife found at the crime scene?",
                "I don’t know why it was there. I’ve never used that knife.",
                "He answers quickly, but his tone lacks confidence, could be worried about the knife."
            )
            // Marco Bellini - Contradiction Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniMarcoBellini,
                "kontradikcija",
                "You said you were at the casino when Isabelle was killed, but witnesses didn’t see you. How do you explain that?",
                "It must have been a mistake. I was at the casino the whole time.",
                "His response is too quick, maybe trying to brush off the discrepancy. Sounds defensive."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniMarcoBellini,
                "kontradikcija",
                "There are claims you were seen leaving Isabelle’s room. Can you deny that?",
                "That’s an absolute lie. I was never in her room.",
                "Quick, defensive response. His confidence feels a bit forced."
            )

            // Vincent Duval - General Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniVincentDuval,
                "opsta",
                "What was your relationship with Isabelle Moreau before her death?",
                "We were in a romantic relationship, though it wasn’t easy. Isabelle had her own world, and I was jealous.",
                "He’s emotional in his response, which could be revealing. His jealousy seems genuine but might have been a motive."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniVincentDuval,
                "opsta",
                "Did you ever have a serious conflict with Isabelle before her death?",
                "Yes, a few times. Her relationship with someone else made me uncontrollably jealous.",
                "He admits jealousy openly, which shows emotional involvement. It could be important for motive."
            )

            // Vincent Duval - Alibi Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniVincentDuval,
                "alibi",
                "Where were you at the time of Isabelle’s murder?",
                "I was in the hotel, in my room. No one saw me.",
                "His answer is calm but feels a bit unsure. There’s a slight hesitation, as if trying to cover all bases."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniVincentDuval,
                "alibi",
                "Did you have any contact with Isabelle shortly before her murder?",
                "No. We hadn’t spoken in days.",
                "He tries to show distance, but the tone suggests there’s more behind their recent interactions."
            )

            // Vincent Duval - Evidence Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniVincentDuval,
                "dokaz",
                "Were there any threatening messages or evidence connecting your number to Isabelle?",
                "I never sent threatening messages. This is all a lie.",
                "His answer is quick, but something about the directness feels like he’s deflecting."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniVincentDuval,
                "dokaz",
                "A knife with your initials was found at the crime scene. Can you explain that?",
                "I’ve never been near that knife. It must be a set-up.",
                "His answer comes quickly, but he doesn’t seem fully confident. He might be trying to explain away something troubling."
            )

            // Vincent Duval - Contradiction Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniVincentDuval,
                "kontradikcija",
                "You said you were in your room when Isabelle was killed, but witnesses saw you near her. How do you explain that?",
                "It’s a mistake. I never left my room.",
                "He’s defensive, trying to deny everything. The quickness of his answer might be a sign of stress."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniVincentDuval,
                "kontradikcija",
                "There are claims that you were jealous because of Isabelle’s other relationships. Can you deny that?",
                "Jealousy wasn’t the reason for her death. That’s just gossip.",
                "He denies it too easily. His response feels a little too rehearsed."
            )

            // Amelia Fontaine - General Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniAmeliaFontaine,
                "opsta",
                "How would you describe your relationship with Isabelle Moreau?",
                "Isabelle was a competitor, but also a friend. We were in different industries, so we didn’t have much conflict.",
                "She tries to keep it neutral. Doesn’t want to reveal too much about her real feelings."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniAmeliaFontaine,
                "opsta",
                "How did you feel about her business success?",
                "She was successful, no doubt. But honestly, sometimes it was hard to watch.",
                "There’s a slight edge to her answer, indicating some underlying resentment or jealousy."
            )

            // Amelia Fontaine - Alibi Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniAmeliaFontaine,
                "alibi",
                "Where were you at the time of Isabelle’s murder?",
                "I was at home, working.",
                "Her answer is quick and simple. There’s no real detail to back it up, which seems suspicious."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniAmeliaFontaine,
                "alibi",
                "Did you have any contact with Isabelle right before her death?",
                "No, we hadn’t spoken for months.",
                "She makes it sound like they were distant, but the lack of emotion could mean there’s more beneath the surface."
            )

            // Amelia Fontaine - Evidence Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniAmeliaFontaine,
                "dokaz",
                "Were there any messages or evidence linking you to threatening Isabelle?",
                "No, I never sent any threatening messages.",
                "Her response is quick, but there’s something about her tone that feels off. Almost too rehearsed."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniAmeliaFontaine,
                "dokaz",
                "A knife with your initials was found at the crime scene. Can you explain that?",
                "I don’t know why it was there. I’ve never used that knife.",
                "Her answer is calm, but the lack of a solid explanation raises suspicion."
            )

            // Amelia Fontaine - Contradiction Questions
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniAmeliaFontaine,
                "kontradikcija",
                "You said you were at home when Isabelle was killed, but witnesses saw you near the crime scene. How do you explain that?",
                "That’s not true. I was home, like I said.",
                "Her answer is too firm. Could be a defensive reaction, or maybe she’s hiding something."
            )
            insertPitanjeIspitivanjeOsumnjicenog(
                osumnjiceniAmeliaFontaine,
                "kontradikcija",
                "There were rumors about your jealousy of Isabelle. Can you deny that?",
                "Jealousy wasn’t the reason for what happened. That’s just talk.",
                "Her response feels too dismissive. Maybe trying to push away the idea without fully confronting it."
            )
        }
    }
}

data class UiStateUserData (
    val userExists: Boolean? = null
)

data class UiStateCrimeData (
    val title: String? = null,
    val date: String? = null,
    val place: String? = null,
    val description: String? = null
)

// get data

suspend fun selectAllOsumnjiceni(): List<OsumnjicenR> {
    val osumnjiceni: List<OsumnjicenR>

    osumnjiceni = realm.query<OsumnjicenR>().find()

    return osumnjiceni
}

suspend fun selectAllSvedoci(): List<SvedokR> {
    val svedoci: List<SvedokR>

    svedoci = realm.query<SvedokR>().find()

    return svedoci
}

suspend fun selectAllPitanjaIspitivanjeOsumnjicenog(): List<PitanjeIspitivanjeOsumnjicenogR> {
    val pitanja: List<PitanjeIspitivanjeOsumnjicenogR>

    pitanja = realm.query<PitanjeIspitivanjeOsumnjicenogR>().find()
    return pitanja
}

suspend fun selectPitanjaByOsumnjicenAndCategory(osumnjicenId: String, category: String): List<PitanjeIspitivanjeOsumnjicenogR> {
    val pitanja: List<PitanjeIspitivanjeOsumnjicenogR>

    pitanja = realm.query<PitanjeIspitivanjeOsumnjicenogR>(
        "osumnjicenId.ime == $0 AND kategorija == $1",
        osumnjicenId,
        category
    ).find()

    return pitanja
}