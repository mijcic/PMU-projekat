package com.example.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class TipZlocinaDC(
    val id: Int,
    val naziv: String,
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
    var osobaId: OsobaData
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
    var motiv: MotivData,
    var zlocinId: Int,
    val kriv: Int,
    val osobaId: OsobaData
)

@Serializable
data class DokazData(
    var idDokaz:Int,
    var tipDokaza: String,
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
    val osobaId: OsobaData
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
    var datum: Long,
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

@Serializable
open class ZadatakData (
    var idZadatak: Int,
    val tekst: String,
    val korak: String,
    val uradjen: Boolean,
    val nextZadatak: Int?,
    val zlocinId: Int
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

// telefon zrtve

@Serializable
data class OneContactData(
    var idOneContact: Int,
    val zlocinId: Int,
    val ime: String,
    val broj: String,
    val slika: Int
)

@Serializable
data class BeleskaData (
    var idBeleska: Int,
    val zlocinId: Int,
    val tekst: String,
    val datum: Long
)

@Serializable
data class WhatsAppKontaktData (
    var idWhatsAppKontakt: Int,
    val zlocinId: Int,
    val ime: String,
    val broj: String,
    val slika: Int?
)

@Serializable
data class WhatsAppPorukaData ( // SA MASOM
    var idWhatsAppPoruka: Int,
    val kontaktKoSalje: Int,
    val kontaktKomeSalje: Int,
    val tekst: String,
    val datum: Long,
    val procitana: Boolean
)

@Serializable
data class OneCallData (
    var idOneCall: Int,
    val kontakt: Int,
    val datum: Long,
    val propusten: Boolean,
    val dolazni: Boolean
)

@Serializable
data class GalleryData (
    var idPhoto: Int,
    val zlocinId: Int,
    val slika: Int?,
    val datum: Long,
    val mesto: String
)

@Serializable
data class ObicnaPorukaData ( // SA MASOM
    var idObicnaPoruka: Int,
    val kontaktKoSalje: Int,
    val kontaktKomeSalje: Int,
    val tekst: String,
    val datum: Long,
    val procitana: Boolean
)

@Serializable
data class OdnosOsumnjicenZrtvaData ( // SA MASOM
    var idOdnos: Int,
    val osumnjicenId:  Int,
    val zrtvaId:  Int,
    val tipOdnosa: String
)

@Serializable
data class PrijavljeniKorisnikData (
    var idKorisnik: Int,
    val korisnickoIme: String,
    val sifra: String,
)

@Serializable
data class PitanjeData (
    var idPitanje: Int,
    val zlocinId: Int,
    val tekst: String
)

@Serializable
data class OdgovorData (
    var idOdogovor: Int,
    val pitanjeId: Int,
    val tekstOdgovora: String,
    val tacan: Boolean,
    val bodovi: Int
)

@Serializable
data class PitanjeIspitivanjeOsumnjicenogData (   // SA MASOM
    var idPitanjeIspitivanjeOsumnjicenog: Int,
    var kategorija: String,
    val tekst: String,
    val odgovor: String,
    val komentar: String,
    val osumnjicenId: Int
)

@Serializable
data class PitanjeIspitivanjeSvedokaData (
    var idPitanjeIspitivanjeSvedoka: Int,
    val tekst: String,
    val odgovor: String,
    val svedokId: Int,
    val nextPitanje: Int
)

@Serializable
data class DokazZadatakData (
    var idDokazZadatak: Int,
    val tekst: String,
    val dokazId: Int,
    val uradjen: Boolean,
    val zadatakId: Int
)

@Serializable
data class IspitivanjeOsumnjicenogZadatakData (
    var idIspitivanjeOsumnjicenogZadatak: Int,
    val osumnjicenId: Int,
    val zadatakId: Int,
    val uradjen: Boolean
)

@Serializable
data class IspitivanjeSvedokaZadatakData (
    var idIspitivanjeSvedokaZadatak: Int,
    val svedokId: Int,
    val zadatakId: Int,
    val uradjen: Boolean
)

@Serializable
data class TelefonZadatakData (
    var idTelefonZadatak: Int,
    val telefonId: Int,
    val zadatakId: Int,
    val uradjen: Boolean
)

@Serializable
data class ForenzickiDokazZadatakData (
    var idForenzickiDokazZadatak: Int,
    val tekst: String,
    val forenzickiDokazId: Int,
    val uradjen: Boolean,
    val zadatakId: Int
)

@Serializable
data class PorukeZadatakData (
    var idPorukeZadatak: Int,
    val porukeId: Int,
    val zadatakId: Int,
    val uradjen: Boolean
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

@Serializable
open class PacijentData (
    var idPacijent: Int,
    var simptomi: String,
    var statusPacijenta: String, //ziva,mrtva
    var datumPrijave: Long,
    var prijavio: OsobaData,
    var zlocinId: ZlocinData,
    var zrtvaId: ZrtvaData
)


@Serializable
open class MedicinskiIzvestajData (
    var idMedicinskiIzvestaj: Int,
    var rezime: String,
    var CTnalaz: String,
    var MRInalaz: String,
    var krvnaSlika: String,
    var toksikoloskeAnalize: String,
    var zakljucak: String,
    var pacijentId: PacijentData
)

@Serializable
open class LekarskiTestData (
    var idLekarskiTest: Int,
    var pacijentId: PacijentData,
    var izvestaj: String,
)

@Serializable
open class LokacijeIstrageData (
    var idLokacijeIstrage: Int,
    var mesto: String,
    var naziv: String,
    var opis: String,
    var zlocinId: Int,
    var geoTackaALatitude:Double,
    var geoTackaALongitude:Double
)

@Serializable
open class IzjavaZaPacijentaData (
    var idIzjavaZaPacijenta: Int,
    var izjava: String = "",
    var pacijentId: PacijentData,
    var osobaId: OsobaData
)