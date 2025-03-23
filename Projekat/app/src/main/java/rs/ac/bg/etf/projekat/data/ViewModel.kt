package rs.ac.bg.etf.projekat.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.type.TimeZone
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.UpdatePolicy
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
    //private val MyRepository: Repository
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

     fun saveData() {
        viewModelScope.launch {
            realm.write {
                val zlocin=ZlocinR().apply {
                    idZlocin = 1 // Primer ID-a
                    naziv = "Ubistvo"
                    mesto = "Beograd"
                    opis = "Ubistvo u centru grada"
                    datum = RealmInstant.now()
                }

                copyToRealm(zlocin, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    fun insertDataForMurder() {
        viewModelScope.launch {
            realm.write {
                // tip zlocina
                val tipZlocina = TipZlocinaR().apply {
                    nazivTipaZlocina = "Murder"
                }

                // zlocin
                val zlocin = ZlocinR().apply {
                    tipZlocinaId = tipZlocina
                    naziv = "Murder in a luxury casino"

                    val dateString = "16.11.2023"
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    val localDate = LocalDate.parse(dateString, formatter)
                    val instantDate = localDate.atStartOfDay().toInstant(ZoneOffset.UTC)
                    val realmInstantDate = RealmInstant.from(
                        instantDate.toEpochMilli(),
                        nanosecondAdjustment = 0
                    )

                    datum = realmInstantDate
                    mesto = "Luxury casino 'Fortuna' in Monte Carlo"
                    opis = "The young heiress of a wealthy hotel chain was found dead in her luxurious room, " +
                            "just a few hours after winning a record sum of money in a poker tournament. " +
                            "Signs of a struggle were found at the crime scene, and the body had several stab wounds."
                    var status: String = stZlocinR.u_istrazi.name
                }

                // zrtva
                val zrtva = ZrtvaR().apply {
                    tipZrtve = "Person"
                    ime = "Isabelle Moreau"
                    detalji = "Isabelle was the heiress of a famous French family involved in the hotel industry. " +
                            "She was known as an ambitious, yet controversial figure in social circles."
                    statusZrtva = StatusZrtvaR.mrtva.name
                    zlocinId = zlocin
                }

                // motivi osumnjicenih
                val motivMarcoBellini = MotivR().apply {
                    opis = "The victim owed him money due to a gambling addiction."
                }

                val motivVincentDuval = MotivR().apply {
                    opis = "Jealousy due to Isabelle's relationship with him."
                }

                val motivAmeliaFontaine = MotivR().apply {
                    opis = "Jealousy, envy, and a desire for revenge due to unrequited love for Marco and feelings of inadequacy next to Isabelle."
                }

                // osumnjiceni
                val osumnjiceniMarcoBellini = OsumnjicenR().apply {
                    ime = "Marco Bellini"
                    status = 0
                    tipOsumnjicen = TipOsumnjicenR.pojedinac.name
                    motiv = motivMarcoBellini
                    zlocinId = zlocin
                    kriv = 0
                }

                val osumnjiceniVincentDuval = OsumnjicenR().apply {
                    ime = "Vincent Duval"
                    status = 0
                    tipOsumnjicen = TipOsumnjicenR.pojedinac.name
                    motiv = motivVincentDuval
                    zlocinId = zlocin
                    kriv = 0
                }

                val osumnjiceniAmeliaFontaine = OsumnjicenR().apply {
                    ime = "Amelia Fontaine"
                    status = 1
                    tipOsumnjicen = TipOsumnjicenR.pojedinac.name
                    motiv = motivAmeliaFontaine
                    zlocinId = zlocin
                    kriv = 1
                }

                // dokazi
                val dokaz1 = DokazR().apply {
                    tipDokaza = TipDokazaR.fizicki.name
                    opis = "A bloody knife with the initials 'M.B.' was found at the crime scene."
                    zlocinId = zlocin
                    zrtvaId = zrtva
                    status = 0
                }

                val dokaz2 = DokazR().apply {
                    tipDokaza = TipDokazaR.digitalni.name
                    opis = "Isabelle was receiving threatening messages on WhatsApp, which were later linked to Marc Bellini's phone number."
                    zlocinId = zlocin
                    zrtvaId = zrtva
                    status = 0
                }

                val dokaz3 = DokazR().apply {
                    tipDokaza = TipDokazaR.fizicki.name
                    opis = "A bloody knife with the initials 'M.B.' was found at the crime scene, but DNA analysis revealed that the skin traces on the knife belonged to Amelie."
                    zlocinId = zlocin
                    zrtvaId = zrtva
                    status = 1
                }

                val dokaz4 = DokazR().apply {
                    tipDokaza = TipDokazaR.digitalni.name
                    opis = "Threatening messages on WhatsApp were linked to Marc Bellini's phone number, but it turned out that they were sent by Amelia using a different device."
                    zlocinId = zlocin
                    zrtvaId = zrtva
                    status = 1
                }

                // dokazi osumnjicenih
                val dokazOsumnjiceni1 = DokazOsumnjicenR().apply {
                    dokazId = dokaz1
                    osumnjicenId = osumnjiceniAmeliaFontaine
                }

                val dokazOsumnjiceni2 = DokazOsumnjicenR().apply {
                    dokazId = dokaz2
                    osumnjicenId = osumnjiceniAmeliaFontaine
                }

                val dokazOsumnjiceni3 = DokazOsumnjicenR().apply {
                    dokazId = dokaz3
                    osumnjicenId = osumnjiceniAmeliaFontaine
                }

                val dokazOsumnjiceni4 = DokazOsumnjicenR().apply {
                    dokazId = dokaz4
                    osumnjicenId = osumnjiceniAmeliaFontaine
                }

                // svedok
                val svedokAmeliaFontaine = SvedokR().apply {
                    ime = "Amelia Fontaine"
                    kontakt = "+377 556 789"
                    izjava = "Amelia saw Marc leaving Isabelle's room a few hours before the body was found. " +
                            "She also claimed to have heard an argument coming from the room."
                    zlocinId = zlocin
                    statusSvedok = StatusSvedokR.aktivno.name
                    statusIspitan = 1
                }

                // alibiji
                val alibiMarcoBellini = AlibiR().apply {
                    osumnjicenId = osumnjiceniMarcoBellini
                    svedokId = null
                    opis = "Marco claims that he was at the casino during the crime, playing poker, " +
                            "but there is no evidence that he was at the table at that time."
                    statusAlibija = StatusAlibijaR.lažan.name
                }

                val alibiAmeliaFontaine = AlibiR().apply {
                    osumnjicenId = osumnjiceniAmeliaFontaine
                    svedokId = null
                    opis = "Amelia claimed that she was at the casino at the time of the murder, " +
                            "but security footage showed that she left the room just before the crime."
                    statusAlibija = StatusAlibijaR.lažan.name
                }

                // misija
                val misija = MisijaR().apply {
                    zlocinId = zlocin
                    naziv = "Hidden card"
                    opis = "The user received a message from an unknown number with the content: " +
                            "'You know Marco is just a pawn. The real truth is buried deeper. Look for the Queen of Hearts card."
                    status = 0
                }

                // kontakt
                val kontaktAmeliaFontaine = KontaktR().apply {
                    ime = "Amelia Fontaine"
                    broj = "+377 556 789"
                    status = 0
                    zrtvaId = zrtva
                }

                // poruka
                val porukaKorisniku = PorukeR().apply {
                    tipPoruke = TipPorukeR.SMS
                    sadrzaj = "You know Marco is just a pawn. The real truth is buried deeper. Look for the Queen of Hearts card."
                    datumVreme = null
                    zrtvaId = zrtva
                    posiljalacId = kontaktAmeliaFontaine
                    statusPoruke = StatusPorukeR.sent.name
                    sifrovana = false
                }

                // misija poruka
                val misijaPoruka = MisijaPorukaR().apply {
                    zlocinId = zlocin
                    naziv = "Hidden card"
                    poruka = porukaKorisniku
                    status = 0
                    posiljalac = "Amelia Fontaine"
                }

                // obdukcija
                val obdukcija = ObdukcijaR().apply {
                    izvestaj = "Defensive wounds were found on the body, " +
                            "and death was caused by multiple stab wounds to the chest area."

                    val dateString = "16.11.2023"
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    val localDate = LocalDate.parse(dateString, formatter)
                    val instantDate = localDate.atStartOfDay().toInstant(ZoneOffset.UTC)
                    val realmInstantDate = RealmInstant.from(
                        instantDate.toEpochMilli(),
                        nanosecondAdjustment = 0
                    )

                    datum = realmInstantDate
                    uzrokSmrti = "Multiple stab wounds"
                    zrtvaId = zrtva
                    informacije = "Skin remnants were found under the victim's nails, but the DNA analysis is still ongoing."
                }

                // forenzicki dokaz
                val forenzickiDokaz = ForenzickiDokazR().apply {
                    tipForenzickiDokaz = TipForenzickiDokazR.DNK.name
                    opis = "Skin remnants were found under the victim's nails. The analysis result is pending."
                    status = 0
                    zrtvaId = zrtva
                    veza = "Potential connection to the suspect Marco Bellini."
                }

                // telefon
                val telefon = TelefonR().apply {
                    model = "iPhone 14 Pro"
                    os = "iOS"
                    zrtvaId = zrtva
                    sifra = "4862"
                }

                // odnosi izmedju osumnjicenog i zrtve
                val odnosOsumnjicenZrtvaAF = OdnosOsumnjicenZrtvaR().apply {
                    osumnjicenId = osumnjiceniAmeliaFontaine
                    zrtvaId = zrtva
                    tipOdnosa = TipOdnosaR.rivalski.name
                }

                val odnosOsumnjicenZrtvaMB = OdnosOsumnjicenZrtvaR().apply {
                    osumnjicenId = osumnjiceniMarcoBellini
                    zrtvaId = zrtva
                    tipOdnosa = TipOdnosaR.poslovni.name
                }

                val odnosOsumnjicenZrtvaVD = OdnosOsumnjicenZrtvaR().apply {
                    osumnjicenId = osumnjiceniVincentDuval
                    zrtvaId = zrtva
                    tipOdnosa = TipOdnosaR.ljubavni.name
                }

                copyToRealm(tipZlocina, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(zlocin, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(zrtva, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(motivMarcoBellini, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(motivVincentDuval, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(motivAmeliaFontaine, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(osumnjiceniMarcoBellini, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(osumnjiceniVincentDuval, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(osumnjiceniAmeliaFontaine, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(dokaz1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(dokaz2, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(dokaz3, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(dokaz4, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(dokazOsumnjiceni1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(dokazOsumnjiceni2, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(dokazOsumnjiceni3, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(dokazOsumnjiceni4, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(svedokAmeliaFontaine, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(alibiMarcoBellini, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(alibiAmeliaFontaine, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(misija, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(kontaktAmeliaFontaine, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(porukaKorisniku, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(misijaPoruka, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(obdukcija, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(forenzickiDokaz, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(telefon, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(odnosOsumnjicenZrtvaAF, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(odnosOsumnjicenZrtvaMB, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(odnosOsumnjicenZrtvaVD, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }
}

data class UiStateZlocin(
    val zlocin: List<Zlocin> = emptyList()
)