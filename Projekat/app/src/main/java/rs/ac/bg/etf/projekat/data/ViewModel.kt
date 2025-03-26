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
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
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

    suspend fun inserTipZlocina(): TipZlocinaR? {
        var tipZlocina: TipZlocinaR? =null
        realm.write {
             tipZlocina =
                query<TipZlocinaR>("nazivTipaZlocina == $0", "murder").find().firstOrNull()

            if (tipZlocina == null) {
                val maxId = query<TipZlocinaR>().find().maxOfOrNull { it.idTipZlocina } ?: 0
                tipZlocina = TipZlocinaR().apply {
                    idTipZlocina = maxId + 1
                    nazivTipaZlocina = "murder"
                }
                copyToRealm(tipZlocina!!)
            }
        }
        return tipZlocina
    }

    suspend fun insertZlocin(tipZlocina: TipZlocinaR?) {
        realm.write {
            val nazivZlocina = "Murder in a luxury casino"
            val dateString = "16.11.2023"
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val localDate = LocalDate.parse(dateString, formatter)
            val instantDate = localDate.atStartOfDay(ZoneOffset.UTC).toInstant()

            val realmInstantDate = RealmInstant.from(instantDate.epochSecond, instantDate.nano)

            // Ako tipZlocina nije unet u bazu, unesite ga
            val existingTipZlocina = query<TipZlocinaR>("nazivTipaZlocina == $0", tipZlocina?.nazivTipaZlocina).find().firstOrNull()
                ?: tipZlocina?.let {
                    copyToRealm(it)
                }

            val zlocin = query<ZlocinR>("tipZlocinaId == $0 AND naziv == $1 AND datum == $2", existingTipZlocina, nazivZlocina, realmInstantDate).find().firstOrNull()
                ?: ZlocinR().apply {
                    idZlocin = (query<ZlocinR>().find().maxOfOrNull { it.idZlocin } ?: 0) + 1
                    tipZlocinaId = existingTipZlocina // Povezivanje sa tipom zločina
                    naziv = nazivZlocina
                    datum = realmInstantDate
                    mesto = "Luxury casino 'Fortuna' in Monte Carlo"
                    opis = "The young heiress of a wealthy hotel chain was found dead..."
                    status = stZlocinR.u_istrazi.name
                }

            copyToRealm(zlocin)
        }
    }


    fun insertDataForMurder()  {
        viewModelScope.launch {
            val tipZlocinaR:TipZlocinaR? = inserTipZlocina()
            insertZlocin(tipZlocinaR)
            realm.write {

                /*
                // tip zlocina
                var tipZlocina: TipZlocinaR? = null

                var postojiTipZlocina = query<TipZlocinaR>("nazivTipaZlocina == $0", "Murder").find().firstOrNull()

                if (postojiTipZlocina == null) {
                    val maxId = query<TipZlocinaR>().find().maxOfOrNull { it.idTipZlocina } ?: 0

                    tipZlocina = TipZlocinaR().apply {
                        idTipZlocina = maxId + 1
                        nazivTipaZlocina = "Murder"
                    }
                }

                // zlocin
                var zlocin: ZlocinR? = null

                val tipZlocinaZlocin = query<TipZlocinaR>("nazivTipaZlocina == $0", "Murder").find().firstOrNull()

                val nazivZlocina = "Murder in a luxury casino"

                val dateString = "16.11.2023"
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                val localDate = LocalDate.parse(dateString, formatter)

                val instantDate = localDate.atStartOfDay(ZoneOffset.UTC).toInstant()

                val realmInstantDate = RealmInstant.from(
                    instantDate.epochSecond,
                    instantDate.nano
                )

                val mestoZlocina = "Luxury casino 'Fortuna' in Monte Carlo"
                val opisZlocina = "The young heiress of a wealthy hotel chain was found dead in her luxurious room, " +
                                  "just a few hours after winning a record sum of money in a poker tournament. " +
                                  "Signs of a struggle were found at the crime scene, and the body had several stab wounds."

                var postojiZlocin = query<ZlocinR>("tipZlocinaId == $0 AND naziv == $1 AND datum == $2 AND mesto == $3 AND opis == $4 AND status == $5",
                    tipZlocinaZlocin, nazivZlocina, realmInstantDate, mestoZlocina, opisZlocina, stZlocinR.u_istrazi.name).find()

                System.out.println("USLO OVDE: " + tipZlocina?.nazivTipaZlocina)

                if (postojiZlocin == null) {
                    val maxId = query<ZlocinR>().find().maxOfOrNull { it.idZlocin } ?: 0

                    zlocin = ZlocinR().apply {
                        idZlocin = maxId + 1
                        tipZlocinaId = tipZlocinaZlocin
                        naziv = nazivZlocina
                        datum = realmInstantDate
                        mesto = mestoZlocina
                        opis = opisZlocina
                        status = stZlocinR.u_istrazi.name
                    }
                }

                // zrtva
                var zrtva: ZrtvaR? = null

                val imeZrtve = "Isabelle Moreau"
                val detaljiZrtve = "Isabelle was the heiress of a famous French family involved in the hotel industry. " +
                                   "She was known as an ambitious, yet controversial figure in social circles."

                var postojiZrtva = realm.query<ZrtvaR>("tipZrtve == $0 AND ime == $1 AND detalji == $2 AND statusZrtva == $3 AND zlocinId == $4",
                    "Person", imeZrtve, detaljiZrtve, StatusZrtvaR.mrtva.name, zlocin).find()

                var zlocinZrtva = realm.query<ZlocinR>("idZlocin == $0", zlocin?.idZlocin).find().firstOrNull()

                if (postojiZrtva == null) {
                    val maxId = realm.query<ZrtvaR>().find().maxOfOrNull { it.idZrtva } ?: 0

                    zrtva = ZrtvaR().apply {
                        idZrtva = maxId + 1
                        tipZrtve = "Person"
                        ime = imeZrtve
                        detalji = detaljiZrtve
                        statusZrtva = StatusZrtvaR.mrtva.name
                        zlocinId = zlocinZrtva
                    }
                }

                // motivi osumnjicenih
                var motivMarcoBellini: MotivR? = null
                var motivVincentDuval: MotivR? = null
                var motivAmeliaFontaine: MotivR? = null

                var postojiMotiv = realm.query<MotivR>("opis == $0", "The victim owed him money due to a gambling addiction.").find()

                if (postojiMotiv == null) {
                    val maxId = realm.query<MotivR>().find().maxOfOrNull { it.idMotiv } ?: 0

                    motivMarcoBellini = MotivR().apply {
                        idMotiv = maxId + 1
                        opis = "The victim owed him money due to a gambling addiction."
                    }
                }

                postojiMotiv = realm.query<MotivR>("opis == $0", "Jealousy due to Isabelle's relationship with him.").find()

                if (postojiMotiv == null) {
                    val maxId = realm.query<MotivR>().find().maxOfOrNull { it.idMotiv } ?: 0

                    motivVincentDuval = MotivR().apply {
                        idMotiv = maxId + 1
                        opis = "Jealousy due to Isabelle's relationship with him."
                    }
                }

                postojiMotiv = realm.query<MotivR>("opis == $0", "Jealousy, envy, and a desire for revenge due to unrequited love for Marco and feelings of inadequacy next to Isabelle.").find()

                if (postojiMotiv == null) {
                    val maxId = realm.query<MotivR>().find().maxOfOrNull { it.idMotiv } ?: 0

                    motivAmeliaFontaine = MotivR().apply {
                        idMotiv = maxId + 1
                        opis = "Jealousy, envy, and a desire for revenge due to unrequited love for Marco and feelings of inadequacy next to Isabelle."
                    }
                }

                // osumnjiceni
                var osumnjiceniMarcoBellini: OsumnjicenR? = null
                var osumnjiceniVincentDuval: OsumnjicenR? = null
                var osumnjiceniAmeliaFontaine: OsumnjicenR? = null

                val motivMBOsumnjiceni = realm.query<MotivR>("idMotiv == $0", motivMarcoBellini?.idMotiv).find().firstOrNull()
                val motivVDOsumnjiceni = realm.query<MotivR>("idMotiv == $0", motivVincentDuval?.idMotiv).find().firstOrNull()
                val motivAFOsumnjiceni = realm.query<MotivR>("idMotiv == $0", motivAmeliaFontaine?.idMotiv).find().firstOrNull()

                var postojiOsumnjiceni = realm.query<OsumnjicenR>("ime == $0 AND status == $1 AND tipOsumnjicen == $2 AND motiv == $3 AND zlocinId == $4 AND kriv == $5",
                    "Marco Bellini", 0, TipOsumnjicenR.pojedinac.name, motivMBOsumnjiceni, zlocin, 0).find()

                if (postojiOsumnjiceni == null) {
                    val maxId = realm.query<OsumnjicenR>().find().maxOfOrNull { it.idOsumnjicen } ?: 0

                    osumnjiceniMarcoBellini = OsumnjicenR().apply {
                        idOsumnjicen = maxId + 1
                        ime = "Marco Bellini"
                        status = 0
                        tipOsumnjicen = TipOsumnjicenR.pojedinac.name
                        motiv = motivMBOsumnjiceni
                        zlocinId = zlocin
                        kriv = 0
                    }
                }

                postojiOsumnjiceni = realm.query<OsumnjicenR>("ime == $0 AND status == $1 AND tipOsumnjicen == $2 AND motiv == $3 AND zlocinId == $4 AND kriv == $5",
                    "Vincent Duval", 0, TipOsumnjicenR.pojedinac.name, motivVDOsumnjiceni, zlocin, 0).find()

                if (postojiOsumnjiceni == null) {
                    val maxId = realm.query<OsumnjicenR>().find().maxOfOrNull { it.idOsumnjicen } ?: 0

                    osumnjiceniVincentDuval = OsumnjicenR().apply {
                        idOsumnjicen = maxId + 1
                        ime = "Vincent Duval"
                        status = 0
                        tipOsumnjicen = TipOsumnjicenR.pojedinac.name
                        motiv = motivVDOsumnjiceni
                        zlocinId = zlocin
                        kriv = 0
                    }
                }

                postojiOsumnjiceni = realm.query<OsumnjicenR>("ime == $0 AND status == $1 AND tipOsumnjicen == $2 AND motiv == $3 AND zlocinId == $4 AND kriv == $5",
                    "Amelia Fontaine", 1, TipOsumnjicenR.pojedinac.name, motivAFOsumnjiceni, zlocin, 1).find()

                if (postojiOsumnjiceni == null) {
                    val maxId = realm.query<OsumnjicenR>().find().maxOfOrNull { it.idOsumnjicen } ?: 0

                    osumnjiceniVincentDuval = OsumnjicenR().apply {
                        idOsumnjicen = maxId + 1
                        ime = "Amelia Fontaine"
                        status = 1
                        tipOsumnjicen = TipOsumnjicenR.pojedinac.name
                        motiv = motivAFOsumnjiceni
                        zlocinId = zlocin
                        kriv = 1
                    }
                }

                // dokazi
                var dokaz1: DokazR? = null
                var dokaz2: DokazR? = null
                var dokaz3: DokazR? = null
                var dokaz4: DokazR? = null

                var zlocinDokaz = realm.query<ZlocinR>("idZlocin == $0", zlocin?.idZlocin).find().firstOrNull()
                var zrtvaDokaz = realm.query<ZrtvaR>("idZrtva == $0", zrtva?.idZrtva).find().firstOrNull()

                var postojiDokaz = realm.query<DokazR>("tipDokaza == $0 AND opis == $1 AND zlocinId == $2 AND zrtvaId == $3 AND status == $4",
                    TipDokazaR.fizicki.name, "A bloody knife with the initials 'M.B.' was found at the crime scene.", zlocinDokaz, zrtvaDokaz, 0).first().find()

                if (postojiDokaz == null) {
                    val maxId = realm.query<DokazR>().find().maxOfOrNull { it.idDokaz } ?: 0

                    dokaz1 = DokazR().apply {
                        idDokaz = maxId + 1
                        tipDokaza = TipDokazaR.fizicki.name
                        opis = "A bloody knife with the initials 'M.B.' was found at the crime scene."
                        zlocinId = zlocinDokaz
                        zrtvaId = zrtvaDokaz
                        status = 0
                    }
                }

                postojiDokaz = realm.query<DokazR>("tipDokaza == $0 AND opis == $1 AND zlocinId == $2 AND zrtvaId == $3 AND status == $4",
                    TipDokazaR.digitalni.name, "Isabelle was receiving threatening messages on WhatsApp, which were later linked to Marc Bellini's phone number.", zlocinDokaz, zrtvaDokaz, 0).first().find()

                if (postojiDokaz == null) {
                    val maxId = realm.query<DokazR>().find().maxOfOrNull { it.idDokaz } ?: 0

                    dokaz2 = DokazR().apply {
                        idDokaz = maxId + 1
                        tipDokaza = TipDokazaR.digitalni.name
                        opis = "Isabelle was receiving threatening messages on WhatsApp, which were later linked to Marc Bellini's phone number."
                        zlocinId = zlocinDokaz
                        zrtvaId = zrtvaDokaz
                        status = 0
                    }
                }

                postojiDokaz = realm.query<DokazR>("tipDokaza == $0 AND opis == $1 AND zlocinId == $2 AND zrtvaId == $3 AND status == $4",
                    TipDokazaR.fizicki.name, "A bloody knife with the initials 'M.B.' was found at the crime scene, but DNA analysis revealed that the skin traces on the knife belonged to Amelie.", zlocinDokaz, zrtvaDokaz, 1).first().find()

                if (postojiDokaz == null) {
                    val maxId = realm.query<DokazR>().find().maxOfOrNull { it.idDokaz } ?: 0

                    dokaz3 = DokazR().apply {
                        idDokaz = maxId + 1
                        tipDokaza = TipDokazaR.fizicki.name
                        opis = "A bloody knife with the initials 'M.B.' was found at the crime scene, but DNA analysis revealed that the skin traces on the knife belonged to Amelie."
                        zlocinId = zlocinDokaz
                        zrtvaId = zrtvaDokaz
                        status = 1
                    }
                }

                postojiDokaz = realm.query<DokazR>("tipDokaza == $0 AND opis == $1 AND zlocinId == $2 AND zrtvaId == $3 AND status == $4",
                    TipDokazaR.digitalni.name, "Threatening messages on WhatsApp were linked to Marc Bellini's phone number, but it turned out that they were sent by Amelia using a different device.", zlocinDokaz, zrtvaDokaz, 1).first().find()

                if (postojiDokaz == null) {
                    val maxId = realm.query<DokazR>().find().maxOfOrNull { it.idDokaz } ?: 0

                    dokaz4 = DokazR().apply {
                        idDokaz = maxId + 1
                        tipDokaza = TipDokazaR.digitalni.name
                        opis = "Threatening messages on WhatsApp were linked to Marc Bellini's phone number, but it turned out that they were sent by Amelia using a different device."
                        zlocinId = zlocinDokaz
                        zrtvaId = zrtvaDokaz
                        status = 1
                    }
                }

                // dokazi osumnjicenih
                var dokazOsumnjiceni1: DokazOsumnjicenR? = null
                var dokazOsumnjiceni2: DokazOsumnjicenR? = null
                var dokazOsumnjiceni3: DokazOsumnjicenR? = null
                var dokazOsumnjiceni4: DokazOsumnjicenR? = null

                val dokazO1 = realm.query<DokazR>("idDokaz == $0", dokaz1?.idDokaz).find().firstOrNull()
                val dokazO2 = realm.query<DokazR>("idDokaz == $0", dokaz2?.idDokaz).find().firstOrNull()
                val dokazO3 = realm.query<DokazR>("idDokaz == $0", dokaz3?.idDokaz).find().firstOrNull()
                val dokazO4 = realm.query<DokazR>("idDokaz == $0", dokaz4?.idDokaz).find().firstOrNull()
                val osumnjiceniAF = realm.query<OsumnjicenR>("idOsumnjicen == $0", osumnjiceniAmeliaFontaine?.idOsumnjicen).find().firstOrNull()


                var postojiDokazOsumnjicen = realm.query<DokazOsumnjicenR>("dokazId == $0 AND osumnjicenId == $1", dokazO1, osumnjiceniAF).find()

                if (postojiDokazOsumnjicen == null) {
                    val maxId = realm.query<DokazOsumnjicenR>().find().maxOfOrNull { it.idDokazOsumnjicen } ?: 0

                    dokazOsumnjiceni1 = DokazOsumnjicenR().apply {
                        idDokazOsumnjicen = maxId + 1
                        dokazId = dokazO1
                        osumnjicenId = osumnjiceniAF
                    }
                }

                postojiDokazOsumnjicen = realm.query<DokazOsumnjicenR>("dokazId == $0 AND osumnjicenId == $1", dokazO2, osumnjiceniAF).find()

                if (postojiDokazOsumnjicen == null) {
                    val maxId = realm.query<DokazOsumnjicenR>().find().maxOfOrNull { it.idDokazOsumnjicen } ?: 0

                    dokazOsumnjiceni2 = DokazOsumnjicenR().apply {
                        idDokazOsumnjicen = maxId + 1
                        dokazId = dokazO2
                        osumnjicenId = osumnjiceniAF
                    }
                }

                postojiDokazOsumnjicen = realm.query<DokazOsumnjicenR>("dokazId == $0 AND osumnjicenId == $1", dokazO3, osumnjiceniAF).find()

                if (postojiDokazOsumnjicen == null) {
                    val maxId = realm.query<DokazOsumnjicenR>().find().maxOfOrNull { it.idDokazOsumnjicen } ?: 0

                    dokazOsumnjiceni3 = DokazOsumnjicenR().apply {
                        idDokazOsumnjicen = maxId + 1
                        dokazId = dokazO3
                        osumnjicenId = osumnjiceniAF
                    }
                }

                postojiDokazOsumnjicen = realm.query<DokazOsumnjicenR>("dokazId == $0 AND osumnjicenId == $1", dokazO4, osumnjiceniAF).find()

                if (postojiDokazOsumnjicen == null) {
                    val maxId = realm.query<DokazOsumnjicenR>().find().maxOfOrNull { it.idDokazOsumnjicen } ?: 0

                    dokazOsumnjiceni4 = DokazOsumnjicenR().apply {
                        idDokazOsumnjicen = maxId + 1
                        dokazId = dokazO4
                        osumnjicenId = osumnjiceniAF
                    }
                }

                // svedok
                var svedokAmeliaFontaine: SvedokR? = null

                val zlocinSvedok = realm.query<ZlocinR>("idZlocin == $0", zlocin?.idZlocin).find().firstOrNull()
                val izjavaSvedok = "Amelia saw Marc leaving Isabelle's room a few hours before the body was found. " +
                                   "She also claimed to have heard an argument coming from the room."

                var postojiSvedok = realm.query<SvedokR>("ime == $0 AND kontakt == $1 AND izjava == $2 AND zlocinId == $3 AND statusSvedok == $4 AND statusIspitan == $5",
                    "Amelia Fontaine", "+377 556 789", izjavaSvedok, zlocinSvedok, StatusSvedokR.aktivno.name, 1).find()

                if (postojiSvedok == null) {
                    val maxId = realm.query<SvedokR>().find().maxOfOrNull { it.idSvedok } ?: 0

                    svedokAmeliaFontaine = SvedokR().apply {
                        idSvedok = maxId + 1
                        ime = "Amelia Fontaine"
                        kontakt = "+377 556 789"
                        izjava = izjavaSvedok
                        zlocinId = zlocinSvedok
                        statusSvedok = StatusSvedokR.aktivno.name
                        statusIspitan = 1
                    }
                }

                // alibiji
                var alibiMarcoBellini: AlibiR? = null
                var alibiAmeliaFontaine: AlibiR? = null

                val osumnjiceniZaAlibiMB = realm.query<OsumnjicenR>("idOsumnjicen == $0", osumnjiceniMarcoBellini?.idOsumnjicen).find().firstOrNull()
                val osumnjiceniZaAlibiAF = realm.query<OsumnjicenR>("idOsumnjicen == $0", osumnjiceniAmeliaFontaine?.idOsumnjicen).find().firstOrNull()
                val opisAlibijaMB = "Marco claims that he was at the casino during the crime, playing poker, " +
                                    "but there is no evidence that he was at the table at that time."
                val opisAlibijaAF = "Amelia claimed that she was at the casino at the time of the murder, " +
                                    "but security footage showed that she left the room just before the crime."

                var postojiAlibi = realm.query<AlibiR>("osumnjicenId == $0 AND svedokId == $1 AND opis == $2 AND statusAlibija == $3",
                    osumnjiceniZaAlibiMB, null, opisAlibijaMB, StatusAlibijaR.lažan.name).find()

                if (postojiAlibi == null) {
                    val maxId = realm.query<AlibiR>().find().maxOfOrNull { it.idAlibi } ?: 0

                    alibiMarcoBellini = AlibiR().apply {
                        idAlibi = maxId + 1
                        osumnjicenId = osumnjiceniZaAlibiMB
                        svedokId = null
                        opis = opisAlibijaMB
                        statusAlibija = StatusAlibijaR.lažan.name
                    }
                }

                postojiAlibi = realm.query<AlibiR>("osumnjicenId == $0 AND svedokId == $1 AND opis == $2 AND statusAlibija == $3",
                    osumnjiceniZaAlibiAF, null, opisAlibijaAF, StatusAlibijaR.lažan.name).find()

                if (postojiAlibi == null) {
                    val maxId = realm.query<AlibiR>().find().maxOfOrNull { it.idAlibi } ?: 0

                    alibiAmeliaFontaine = AlibiR().apply {
                        idAlibi = maxId + 1
                        osumnjicenId = osumnjiceniZaAlibiAF
                        svedokId = null
                        opis = opisAlibijaAF
                        statusAlibija = StatusAlibijaR.lažan.name
                    }
                }

                // misija
                var misija: MisijaR? = null

                val zlocinMisija = realm.query<ZlocinR>("idZlocin == $0", zlocin?.idZlocin).find().firstOrNull()

                val opisMisije = "The user received a message from an unknown number with the content: " +
                                 "'You know Marco is just a pawn. The real truth is buried deeper. Look for the Queen of Hearts card."

                val postojiMisija = realm.query<MisijaR>("zlocinId == $0 AND naziv == $1 AND opis == $2 AND status == $3",
                    zlocinMisija, "Hidden card", opisMisije, 0).find()

                if (postojiMisija == null) {
                    val maxId = realm.query<MisijaR>().find().maxOfOrNull { it.idMisija } ?: 0

                    misija = MisijaR().apply {
                        idMisija = maxId + 1
                        zlocinId = zlocin
                        naziv = "Hidden card"
                        opis = opisMisije
                        status = 0
                    }
                }

                // kontakt
                var kontaktAmeliaFontaine: KontaktR? = null

                val zrtvaKontakta = realm.query<ZrtvaR>("idZrtva == $0", zrtva?.idZrtva).find().firstOrNull()

                val postojiKontant = realm.query<KontaktR>("ime == $0 AND broj == $1 AND status == $2 AND zrtvaId == $3",
                    "Amelia Fontaine", "+377 556 789", 0, zrtvaKontakta).find()

                if (postojiKontant == null) {
                    val maxId = realm.query<KontaktR>().find().maxOfOrNull { it.idKontakt } ?: 0

                    kontaktAmeliaFontaine = KontaktR().apply {
                        idKontakt = maxId + 1
                        ime = "Amelia Fontaine"
                        broj = "+377 556 789"
                        status = 0
                        zrtvaId = zrtvaKontakta
                    }
                }

                // poruka
                var porukaKorisniku: PorukeR? = null

                val sadrzajPoruke = "You know Marco is just a pawn. The real truth is buried deeper. Look for the Queen of Hearts card."
                val zrtvaPoruke = realm.query<ZrtvaR>("idZrtva == $0", zrtva?.idZrtva).find().firstOrNull()
                val posiljalacPoruke = realm.query<KontaktR>("idKontakt == $0", kontaktAmeliaFontaine?.idKontakt).find().firstOrNull()

                val postojiPoruka = realm.query<PorukeR>("sadrzaj == $0 AND datumVreme == $1 AND zrtvaId == $2 AND posiljalacId == $3 AND statusPoruke == $4 AND sifrovana == $5",
                    sadrzajPoruke, null, zrtvaPoruke, posiljalacPoruke, StatusPorukeR.sent.name, false).find()

                if (postojiPoruka == null) {
                    val maxId = realm.query<PorukeR>().find().maxOfOrNull { it.idPoruke } ?: 0

                    porukaKorisniku = PorukeR().apply {
                        idPoruke = maxId + 1
                        tipPoruke = TipPorukeR.SMS
                        sadrzaj = sadrzajPoruke
                        datumVreme = null
                        zrtvaId = zrtvaPoruke
                        posiljalacId = posiljalacPoruke
                        statusPoruke = StatusPorukeR.sent.name
                        sifrovana = false
                    }
                }

                // misija poruka
                var misijaPoruka: MisijaPorukaR? = null

                val zlocinMisijaPoruka = realm.query<ZlocinR>("idZlocin == $0", zlocin?.idZlocin).find().firstOrNull()
                val porukaMisijaPoruka = realm.query<PorukeR>("idPoruke == $0", porukaKorisniku?.idPoruke).find().firstOrNull()

                val postojiMisijaPoruka = realm.query<MisijaPorukaR>("zlocinId == $0 AND naziv == $1 AND poruka == $2 AND status == $3 AND posiljalac == $4",
                    zlocinMisijaPoruka, "Hidden card", porukaMisijaPoruka, 0, "Amelia Fontaine").find()

                if (postojiMisijaPoruka == null) {
                    val maxId = realm.query<MisijaPorukaR>().find().maxOfOrNull { it.idMisija } ?: 0

                    misijaPoruka = MisijaPorukaR().apply {
                        idMisija = maxId + 1
                        zlocinId = zlocinMisijaPoruka
                        naziv = "Hidden card"
                        poruka = porukaMisijaPoruka
                        status = 0
                        posiljalac = "Amelia Fontaine"
                    }
                }

                // obdukcija
                var obdukcija: ObdukcijaR? = null

                val izvestajObdukcije = "Defensive wounds were found on the body, " +
                                        "and death was caused by multiple stab wounds to the chest area."

                val dateStringO = "16.11.2023"
                val formatterO = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                val localDateO = LocalDate.parse(dateStringO, formatterO)

                val instantDateO = localDateO.atStartOfDay(ZoneOffset.UTC).toInstant()

                val realmInstantDateO = RealmInstant.from(
                    instantDateO.epochSecond,
                    instantDateO.nano
                )

                val zrtvaObdukcija = realm.query<ZrtvaR>("idZrtva == $0", zrtva?.idZrtva).find().firstOrNull()
                val infoObdukcija = "Skin remnants were found under the victim's nails, but the DNA analysis is still ongoing."

                val postojiObdukcija = realm.query<ObdukcijaR>("izvestaj == $0 AND datum == $1 AND uzrokSmrti == $2 AND zrtvaId == $3 AND informacije == $4",
                    izvestajObdukcije, realmInstantDateO, "Multiple stab wounds", zrtvaObdukcija, infoObdukcija).find()

                if (postojiObdukcija == null) {
                    val maxId = realm.query<ObdukcijaR>().find().maxOfOrNull { it.idObdukcija } ?: 0

                    obdukcija = ObdukcijaR().apply {
                        idObdukcija = maxId + 1
                        izvestaj = izvestajObdukcije
                        datum = realmInstantDate
                        uzrokSmrti = "Multiple stab wounds"
                        zrtvaId = zrtvaObdukcija
                        informacije = infoObdukcija
                    }
                }

                // forenzicki dokaz
                var forenzickiDokaz: ForenzickiDokazR? = null

                val opisForenzika = "Skin remnants were found under the victim's nails. The analysis result is pending."
                val zrtvaForenzika = realm.query<ZrtvaR>("idZrtva == $0", zrtva?.idZrtva).find().firstOrNull()

                val postojiForenzickiDokaz = realm.query<ForenzickiDokazR>("tipForenzickiDokaz == $0 AND opis == $1 AND status == $2 AND zrtvaId == $3 AND veza == $4",
                    TipForenzickiDokazR.DNK.name, opisForenzika, 0, zrtvaForenzika, "Potential connection to the suspect Marco Bellini.").find()

                if (postojiForenzickiDokaz == null) {
                    val maxId = realm.query<ForenzickiDokazR>().find().maxOfOrNull { it.idForenzickiDokaz } ?: 0

                    forenzickiDokaz = ForenzickiDokazR().apply {
                        idForenzickiDokaz = maxId + 1
                        tipForenzickiDokaz = TipForenzickiDokazR.DNK.name
                        opis = opisForenzika
                        status = 0
                        zrtvaId = zrtvaForenzika
                        veza = "Potential connection to the suspect Marco Bellini."
                    }
                }

                // telefon
                var telefon: TelefonR? = null

                val zrtvaTelefon = realm.query<ZrtvaR>("idZrtva == $0", zrtva?.idZrtva).find().firstOrNull()

                val postojiTelefon = realm.query<TelefonR>("model == $0 AND os == $1 AND zrtvaId == $2 AND sifra == $3",
                    "iPhone 14 Pro", "iOS", zrtvaTelefon, "4862").find()

                if (postojiTelefon == null) {
                    val maxId = realm.query<TelefonR>().find().maxOfOrNull { it.idTelefon } ?: 0

                    telefon = TelefonR().apply {
                        idTelefon = maxId + 1
                        model = "iPhone 14 Pro"
                        os = "iOS"
                        zrtvaId = zrtvaTelefon
                        sifra = "4862"
                    }
                }

                // odnosi izmedju osumnjicenog i zrtve
                var odnosOsumnjicenZrtvaAF: OdnosOsumnjicenZrtvaR? = null
                var odnosOsumnjicenZrtvaMB: OdnosOsumnjicenZrtvaR? = null
                var odnosOsumnjicenZrtvaVD: OdnosOsumnjicenZrtvaR? = null

                val osumnjiceniAFOdnos = realm.query<OsumnjicenR>("idOsumnjicen == $0", osumnjiceniAmeliaFontaine?.idOsumnjicen).find().firstOrNull()
                val osumnjiceniMBOdnos = realm.query<OsumnjicenR>("idOsumnjicen == $0", osumnjiceniMarcoBellini?.idOsumnjicen).find().firstOrNull()
                val osumnjiceniVDOdnos = realm.query<OsumnjicenR>("idOsumnjicen == $0", osumnjiceniVincentDuval?.idOsumnjicen).find().firstOrNull()
                val zrtvaOdnos = realm.query<ZrtvaR>("idZrtva == $0", zrtva?.idZrtva).find().firstOrNull()

                var postojiOdnos = realm.query<OdnosOsumnjicenZrtvaR>("osumnjicenId == $0 AND zrtvaId == $1 AND tipOdnosa == $2",
                osumnjiceniAFOdnos, zrtvaOdnos, TipOdnosaR.rivalski.name).find()

                if (postojiOdnos == null) {
                    val maxId = realm.query<OdnosOsumnjicenZrtvaR>().find().maxOfOrNull { it.idOdnos } ?: 0

                    odnosOsumnjicenZrtvaAF = OdnosOsumnjicenZrtvaR().apply {
                        idOdnos = maxId
                        osumnjicenId = osumnjiceniAFOdnos
                        zrtvaId = zrtvaOdnos
                        tipOdnosa = TipOdnosaR.rivalski.name
                    }
                }

                postojiOdnos = realm.query<OdnosOsumnjicenZrtvaR>("osumnjicenId == $0 AND zrtvaId == $1 AND tipOdnosa == $2",
                    osumnjiceniMBOdnos, zrtvaOdnos, TipOdnosaR.poslovni.name).find()

                if (postojiOdnos == null) {
                    val maxId = realm.query<OdnosOsumnjicenZrtvaR>().find().maxOfOrNull { it.idOdnos } ?: 0

                    odnosOsumnjicenZrtvaMB = OdnosOsumnjicenZrtvaR().apply {
                        idOdnos = maxId
                        osumnjicenId = osumnjiceniMBOdnos
                        zrtvaId = zrtvaOdnos
                        tipOdnosa = TipOdnosaR.poslovni.name
                    }
                }

                postojiOdnos = realm.query<OdnosOsumnjicenZrtvaR>("osumnjicenId == $0 AND zrtvaId == $1 AND tipOdnosa == $2",
                    osumnjiceniVDOdnos, zrtvaOdnos, TipOdnosaR.ljubavni.name).find()

                if (postojiOdnos == null) {
                    val maxId = realm.query<OdnosOsumnjicenZrtvaR>().find().maxOfOrNull { it.idOdnos } ?: 0

                    odnosOsumnjicenZrtvaVD = OdnosOsumnjicenZrtvaR().apply {
                        idOdnos = maxId
                        osumnjicenId = osumnjiceniVDOdnos
                        zrtvaId = zrtvaOdnos
                        tipOdnosa = TipOdnosaR.ljubavni.name
                    }
                }

                if (tipZlocina != null) copyToRealm(tipZlocina, updatePolicy = UpdatePolicy.ALL)

                if (zlocin != null) copyToRealm(zlocin, updatePolicy = UpdatePolicy.ALL)

                if (zrtva != null) copyToRealm(zrtva, updatePolicy = UpdatePolicy.ALL)

                if (motivMarcoBellini != null) copyToRealm(motivMarcoBellini, updatePolicy = UpdatePolicy.ALL)
                if (motivVincentDuval != null) copyToRealm(motivVincentDuval, updatePolicy = UpdatePolicy.ALL)
                if (motivAmeliaFontaine != null) copyToRealm(motivAmeliaFontaine, updatePolicy = UpdatePolicy.ALL)

                if (osumnjiceniMarcoBellini != null) copyToRealm(osumnjiceniMarcoBellini, updatePolicy = UpdatePolicy.ALL)
                if (osumnjiceniVincentDuval != null) copyToRealm(osumnjiceniVincentDuval, updatePolicy = UpdatePolicy.ALL)
                if (osumnjiceniAmeliaFontaine != null) copyToRealm(osumnjiceniAmeliaFontaine, updatePolicy = UpdatePolicy.ALL)

                if (dokaz1 != null) copyToRealm(dokaz1, updatePolicy = UpdatePolicy.ALL)
                if (dokaz2 != null) copyToRealm(dokaz2, updatePolicy = UpdatePolicy.ALL)
                if (dokaz3 != null) copyToRealm(dokaz3, updatePolicy = UpdatePolicy.ALL)
                if (dokaz4 != null) copyToRealm(dokaz4, updatePolicy = UpdatePolicy.ALL)

                if (dokazOsumnjiceni1 != null) copyToRealm(dokazOsumnjiceni1, updatePolicy = UpdatePolicy.ALL)
                if (dokazOsumnjiceni2 != null) copyToRealm(dokazOsumnjiceni2, updatePolicy = UpdatePolicy.ALL)
                if (dokazOsumnjiceni3 != null) copyToRealm(dokazOsumnjiceni3, updatePolicy = UpdatePolicy.ALL)
                if (dokazOsumnjiceni4 != null) copyToRealm(dokazOsumnjiceni4, updatePolicy = UpdatePolicy.ALL)

                if (svedokAmeliaFontaine != null) copyToRealm(svedokAmeliaFontaine, updatePolicy = UpdatePolicy.ALL)

                if (alibiMarcoBellini != null) copyToRealm(alibiMarcoBellini, updatePolicy = UpdatePolicy.ALL)
                if (alibiAmeliaFontaine != null) copyToRealm(alibiAmeliaFontaine, updatePolicy = UpdatePolicy.ALL)

                if (misija != null) copyToRealm(misija, updatePolicy = UpdatePolicy.ALL)

                if (kontaktAmeliaFontaine != null) copyToRealm(kontaktAmeliaFontaine, updatePolicy = UpdatePolicy.ALL)
                if (porukaKorisniku != null) copyToRealm(porukaKorisniku, updatePolicy = UpdatePolicy.ALL)
                if (misijaPoruka != null) copyToRealm(misijaPoruka, updatePolicy = UpdatePolicy.ALL)

                if (obdukcija != null) copyToRealm(obdukcija, updatePolicy = UpdatePolicy.ALL)

                if (forenzickiDokaz != null) copyToRealm(forenzickiDokaz, updatePolicy = UpdatePolicy.ALL)

                if (telefon != null) copyToRealm(telefon, updatePolicy = UpdatePolicy.ALL)

                if (odnosOsumnjicenZrtvaAF != null) copyToRealm(odnosOsumnjicenZrtvaAF, updatePolicy = UpdatePolicy.ALL)
                if (odnosOsumnjicenZrtvaMB != null) copyToRealm(odnosOsumnjicenZrtvaMB, updatePolicy = UpdatePolicy.ALL)
                if (odnosOsumnjicenZrtvaVD != null) copyToRealm(odnosOsumnjicenZrtvaVD, updatePolicy = UpdatePolicy.ALL)

                 */
            }
        }
    }
}

data class UiStateZlocin(
    val zlocin: List<Zlocin> = emptyList()
)

data class UiStatePostZlocin(
    val message: String?= null
)