package com.example

import kotlinx.serialization.Serializable

@Serializable
data class TipZlocinaDC(
    val id: Int,
    val naziv: String,
)

@Serializable
data class Zlocin(
    val id: Int,
    val naziv: String,
    val opis: String,
    val idTipZlocina: Int,
    val datum: Long,
    val mesto:String,
    val status: String
)

@Serializable
data class ClanOrganizacije(
    val idClan:Int,
    val idOsumnjicen:Int,
    val idOrganizacija:Int
)

@Serializable
data class Dokaz(
    val idDokaz:Int,
    val opis: String,
    val lokacija: String,
    val idZlocin:Int
)

@Serializable
data class ForenzickiDokaz(
    val idForenzickiDokaz:Int,
    val opis: String,
    val tip: String,
    val idZlocin:Int
)

@Serializable
data class Korisnik(
    val idKorisnik:Int,
    val korisnickoIme: String,
    val ime: String,
    val prezime: String,
    val sifra: String,
    val email: String,
    val nacinPrijave: String,
    val poeni:Int,
    val poslednjaAktivnost:Long
)

@Serializable
data class ScoreKorisnik(
    val mesto: Int,
    val korisnickoIme: String,
    val poeni:Int,
)

@Serializable
data class MessageResponse(
    val message: String
)



@Serializable
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
    val forenzickiDokazi: List<ForenzickiDokazData>,
    val telefon: TelefonData,
    val misijaPoruka: List<MisijaPorukaData>
)

@Serializable
data class ZlocinData(
    var id: Int,
    val idTipZlocina: Int,
    val naziv: String,
    val datum: Long,
    val mesto: String,
    val opis: String,
    val status: String
)

@Serializable
data class ZrtvaData(
    var id: Int,
    val ime: String,
    val tipZrtve: String,
    val detalji: String,
    val statusZrtva: String
)

@Serializable
data class MotivData(
    var idMotiv: Int,
    val opis: String
)

@Serializable
data class OsumnjicenData(
    var id: Int,
    val ime: String,
    val tipOsumnjicen: Int,
    val motiv: Int,
    val status: Int,
    val kriv: Int,
    val odnosZrtva:String
)

@Serializable
data class DokazData(
    var id:Int,
    val tipDokaza: String,
    val opis: String,
    val status: Int,
    var osumnjicen: OsumnjicenData
)

@Serializable
data class SvedokData(
    var id: Int,
    val ime: String,
    val izjava: String,
    val kontakt: String,
    val statusSvedok: String,
    val status: Int
)

@Serializable
data class AlibiData(
    var id:Int,
    var osumnjicen: OsumnjicenData,
    var svedok: SvedokData?,
    val opis: String,
    val statusAlibija: String
)

@Serializable
data class ObdukcijaData(
    var id:Int,
    val izvestaj:String,
    val datum: Long,
    val uzrokSmrti:String,
    val informacije: String,
)

@Serializable
data class ForenzickiDokazData (
    var id: Int,
    val tipForenzickiDokaz: String,
    val opis: String,
    val statusS:Int,
    val veza: String,
)

@Serializable
data class TelefonData(
    var id: Int,
    val model:String,
    val os: String,
    val sifra: String,
    val informacije: String
)

@Serializable
data class MisijaData(
    val naziv: String,
    val opis: String,
    val status: Int
)
@Serializable
data class MisijaPorukaData(
    var id: Int,
    val naziv: String,
    val statusS: Int,
    val posiljalac:String,
    val poruka: String,
)

// sign up

@Serializable
data class KorisnikRequest(
    val ime: String,
    val prezime: String,
    val korisnickoIme: String,
    val sifra: String,
    val email: String
)