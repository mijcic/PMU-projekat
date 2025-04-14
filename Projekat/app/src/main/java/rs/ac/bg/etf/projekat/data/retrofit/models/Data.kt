package rs.ac.bg.etf.projekat.data.retrofit.models

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