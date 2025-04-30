package rs.ac.bg.etf.projekat.data.retrofit.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.LocalDate
import java.util.Date

data class Zlocin(
    val id: Int,
    val idTipZlocina: Int,
    val naziv: String,
    val datum: Long,
    val mesto: String,
    val opis: String,
    val status:String
)

data class MessageResponse(
    val message: String
)


data class ZlocinRequest(
    val zlocin: ZlocinData,
    val zrtva: ZrtvaData,
    val osumnjicen: List<OsumnjicenData>,
    val dokazi: List<DokazData>,
    val svedok: List<SvedokData>,
    val alibi: List<AlibiData>,
    val misija: MisijaData,
    val motivi: List<MotivData>,
    val obdukcija: ObdukcijaData,
    val forenzickiDokazi: List<ForenzickiDokaz>,
    val telefon: Telefon,
    val misijaPoruka: List<MisijaPoruka>
)

data class ZlocinData(
    val id: Int,
    val idTipZlocina: Int,
    val naziv: String,
    val datum: Long,
    val mesto: String,
    val opis: String,
    val status:String
)
data class MotivData(
    val idMotiv: Int,
    val opis: String
)
data class ZrtvaData(
    val id: Int,val ime: String, val tipZrtve:String, val detalji: String, val statusZrtva: String)
data class OsumnjicenData(
    val id:Int,
    val ime: String, val tipOsumnjicen: Int,
    val motiv: Int, val status: Int, val kriv:Int, val odnosZrtva:String
)
data class DokazData(
    val id:Int,val tipDokaza: String, val opis: String, val status:Int, val osumnjicen: OsumnjicenData
)
data class SvedokData(
    val id: Int,
    val ime: String, val izjava: String, val kontakt:String,
    val statusSvedok:String, val status: Int
)
data class AlibiData(
    val id:Int, val osumnjicen: OsumnjicenData, val svedok: SvedokData,
    val opis: String, val statusAlibija: String
)
data class ObdukcijaData(
    val id:Int,
    val izvestaj:String,
    val datum: Long,
    val uzrokSmrti:String,
    val informacije: String,
)

data class ForenzickiDokaz (
    val id: Int,
    val tipForenzickiDokaz: String,
    val opis: String,
    val statusS:Int,
    val veza: String,
)

data class Telefon(
    val id: Int,
    val model:String,
    val os: String,
    val sifra: String,
    val informacije: String
)

data class MisijaData(val naziv: String, val opis: String,val status: Int)

data class MisijaPoruka(
    val id: Int,
    val naziv: String,
    val statusS: Int,
    val posiljalac:String,
    val poruka: String,
)

// Sign up i log in

data class KorisnikRequest(
    val ime: String,
    val prezime: String,
    val korisnickoIme: String,
    val sifra: String,
    val email: String
)

// ScorePage korisnici
data class ScorePageKorisnikResponse(
    val mesto:Int,
    val korisnickoIme:String,
    val poeni:Int
)


// gemini

data class ZlocinR(val idZlocin: Int, val tipZlocinaId: Int, val naziv: String, val datum: String?, val mesto: String, val opis: String, val status:String)

data class OsobaR(val idOsoba: Int, val ime: String, val kontakt: String?, val datum: String?, val zanimanje: String,val pol:String,val zlocinId: Int)

data class OsumnjicenR(val idOsumnjicen: Int, val status: Int, val motiv: MotivR?,val tipOsumnjicen:String, val zlocinId:Int,val kriv:Int, val osobaId:OsobaR?)

data class MotivR(val idMotiv:Int, val opis: String)

data class DokazR(val idDokaz: Int, val tipDokaza: String,val opis: String, val zlocinId: Int, val zrtvaId: Int, val status: Int)

data class DokazZadatakR(val idDokazZadatak: Int, val tekst: String,val dokazId:DokazR?, val uradjen:Boolean)

data class SvedokR(val idSvedok: Int, val izjava: String, val statusSvedok: String,val statusIspitan:Int,val zlocinId: Int, val osobaId: OsobaR?)

data class ZrtvaR(val idZrtva: Int, val tipZrtve: String, val detalji:String, val statusZrtva:String,val zlocinId: Int, val osobaId: OsobaR?)

data class ObdukcijaR(val idObdukcija:Int, val izvestaj:String, val datum: String, val uzrokSmrti:String, var zrtvaId: Int, val informacije: String )

data class ForenzickiDokazR(val idForenzickiDokaz: Int, val tipForenzickiDokaz: String, val opis: String, val statusS:Int, val veza: String)

data class TelefonR(val idTelefon: Int, val model:String, val os: String, val sifra: String, val informacije: String)

data class DokazOsumnjicenR(var idDokazOsumnjicen: Int, var dokazId: Int, var osumnjicenId: Int)

data class ZadatakR(var idZadatak: Int, var tekst: String, var korak: String,
    var uradjen: Boolean, var next: ZadatakR?, var zlocinId: Int)

data class AlibiR (var idAlibi: Int, var osumnjicenId: Int, var svedokId: Int?, var opis: String, var statusAlibija: String)

data class KontaktKtor(var idKontakt:Int, val ime: String, val broj: String, val status: Int, var zrtvaId: Int)


data class GeminiResponse2(
    val zlocinR: ZlocinR,
    val osumnjicenR: List<OsumnjicenR>,
    val dokazR: List<DokazR>,
    val svedokR: List<SvedokR>,
    val zrtvaR: ZrtvaR,
    val obdukcijaR: ObdukcijaR,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
    val dokazOsumnjicenR: List<DokazOsumnjicenR>,
    val zadatakR: ZadatakR,
    val alibiR: List<AlibiR>,
    val kontaktKtor: List<KontaktKtor>
)