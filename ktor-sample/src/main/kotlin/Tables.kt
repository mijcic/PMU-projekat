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
    var idZlocin: Int,
    val tipZlocinaId: Int,
    val naziv: String,
    val datum: Long,
    val mesto: String,
    val opis: String,
    val status: String
)

@Serializable
data class OsobaData(
    var idOsoba: Int,
    var ime:String,
    var kontakt: String,
    var datum:Long,
    var zanimanje: String,
    var pol: String,
    var zlocinId:Int
)

@Serializable
data class ZrtvaData(
    var idZrtva: Int,
    val tipZrtve: String,
    val detalji: String,
    val statusZrtva: String,
    val zlocinId:Int,
    val osobaId:Int
)

@Serializable
data class MotivData(
    var idMotiv: Int,
    val opis: String
)

@Serializable
data class OsumnjicenData(
    var idOsumnjicen: Int,
    val status: Int,
    val tipOsumnjicen: String,
    var motiv: Int,
    var zlocinId: Int,
    val kriv: Int,
    val osobaId:Int
)

@Serializable
data class DokazData(
    var idDokaz:Int,
    val tipDokaza: String,
    val opis: String,
    val zlocinId: Int,
    val zrtvaId:Int,
    val status: Int,
)

@Serializable
data class SvedokData(
    var idSvedok: Int,
    val izjava: String,
    val statusSvedok: String,
    val statusIspitan: Int,
    val zlocinId: Int,
    val osobaId: Int
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
    var idObdukcija:Int,
    val izvestaj:String,
    val datum: Long,
    val uzrokSmrti:String,
    var zrtvaId: Int,
    val informacije: String,
)


@Serializable
data class ForenzickiDokazData (
    var idForenzickiDokaz: Int,
    val tipForenzickiDokaz: String,
    val opis: String,
    val statusS:Int,
    val veza: String,
)

@Serializable
data class TelefonData(
    var idTelefon: Int,
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
/*
@Serializable
open class ZadatakR (
    var idZadatak: Int,
    val tekst: String,
    val korak: String,
    val uradjen: Boolean,
    val next: ZadatakR?,
    val zlocinId: Int
)*/

// sign up

@Serializable
data class KorisnikRequest(
    val ime: String,
    val prezime: String,
    val korisnickoIme: String,
    val sifra: String,
    val email: String
)

@Serializable
data class KontaktData (
    var idKontakt: Int,
    var ime: String,
    var broj: String,
    var status: Int,
    var zrtvaId: ZrtvaData
)

@Serializable
data class PorukeData(
    var idPoruke: Int,
    var tipPoruke: String,
    var sadrzaj: String,
    var datumVreme: Long,
    var zrtvaId: ZrtvaData,
    var posiljalacId: KontaktData,
    var statusPoruke: String,
    var sifrovana: Boolean
)

@Serializable
data class PoziviData (
    var idPoziv: Int,
    var tip: Int,
    var broj: String,
    var datumVreme: Long,
    var zrtvaId: ZrtvaData,
    var status: Int,
    var kontaktId: KontaktData
)

@Serializable
data class GalerijaData (
    var idGalerija: Int,
    var tip: Int,
    var putanja: String,
    var zrtvaId: ZrtvaData,
    var datumVreme: Long,
    var lokacija: String
)

@Serializable
data class AplikacijaData (
    var idAplikacije: Int,
    var naziv: String,
    var tip: Int,
    var zrtvaId: ZrtvaData,
    var aktivna: Boolean,
    var informacije: String
)

@Serializable
data class TragData(
    var idTrag: Int,
    var forenzickiDokazId: ForenzickiDokazData,
    var osumnjicenId: OsumnjicenData
)

@Serializable
open class DokazOsumnjicenData (
    var idDokazOsumnjicen: Int,
    var dokazId: DokazData,
    var osumnjicenId: OsumnjicenData
)