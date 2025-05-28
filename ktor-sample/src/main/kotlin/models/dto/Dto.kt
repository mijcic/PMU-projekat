package com.example.models.dto

import kotlinx.serialization.Serializable

//DTO (Data Transfer Object). Koristi se za prenos podataka preko mreže, obično između: klijenta i servera, kontrolera i servisa, servisa i eksternih API-jeva.
//DTO-vi sadrže samo podatke (nema logike) i često predstavljaju uprošćenu ili specifičnu verziju podataka.


@Serializable
data class ZlocinR(val idZlocin: Int, val tipZlocinaId: Int, val naziv: String, val datum: String?, val mesto: String, val opis: String, val status:String)

@Serializable
data class OsobaR(val idOsoba: Int, val ime: String, val kontakt: String?, val datum: String?, val zanimanje: String,val pol:String,val zlocinId: Int)

@Serializable
data class OsumnjicenR(val idOsumnjicen: Int, val status: Int, val motiv: MotivR?,val tipOsumnjicen:String, val zlocinId:Int,val kriv:Int, val osobaId:OsobaR?)

@Serializable
data class MotivR(val idMotiv:Int, val opis: String)

@Serializable
data class DokazR(val idDokaz: Int, val tipDokaza: String,val opis: String, val zlocinId: Int, val zrtvaId: Int, val status: Int)

//@Serializable
//data class DokazZadatakR(val idDokazZadatak: Int, val tekst: String,val dokazId:DokazR?, val uradjen:Boolean)

@Serializable
data class SvedokR(val idSvedok: Int, val izjava: String, val statusSvedok: String,val statusIspitan:Int,val zlocinId: Int, val osobaId: OsobaR?)

@Serializable
data class ZrtvaR(val idZrtva: Int, val tipZrtve: String, val detalji:String, val statusZrtva:String,val zlocinId: Int, val osobaId: OsobaR?)

@Serializable
data class ObdukcijaR(val idObdukcija:Int, val izvestaj:String, val datum: String, val uzrokSmrti:String, var zrtvaId: Int, val informacije: String )

@Serializable
data class ForenzickiDokazR(val idForenzickiDokaz: Int, val tipForenzickiDokaz: String, val opis: String, val statusS:Int, val veza: String)

@Serializable
data class TelefonR(val idTelefon: Int, val model:String, val os: String, val sifra: String, val informacije: String)

@Serializable
data class DokazOsumnjicenR(var idDokazOsumnjicen: Int, var dokazId: Int, var osumnjicenId: Int)

@Serializable
data class AlibiR (var idAlibi: Int, var osumnjicenId: Int, var svedokId: Int?, var opis: String, var statusAlibija: String)

@Serializable
data class KontaktKtor(var idKontakt:Int, val ime: String, val broj: String, val status: Int, var zrtvaId: Int)

@Serializable
data class PorukeKtor(var idPoruke: Int, val tipPoruke: String, val sadrzaj: String, val datumVreme: String, var zrtvaId: Int, var posiljalacId: Int, val statusPoruke: String, val sirovana: Boolean)

@Serializable
data class PoziviKtor (var idPoziv: Int, val tip: Int, val broj: String, val datumVreme: String, val zrtvaId: Int, val status: Int, var kontaktId: Int)

@Serializable
data class GalerijaKtor (var idGalerija: Int, val tip: Int, val putanja: String, var zrtvaId: Int, val datumVreme: String, val lokacija: String)

@Serializable
data class AplikacijaKtor (var idAplikacije: Int, val naziv: String, val tip: Int, val zrtvaId: Int, val aktivna: Boolean, val informacije: String)

@Serializable
data class TragKtor(var idTrag: Int, var forenzickiDokazId: Int, var osumnjicenId: Int)

@Serializable
data class DokazOsumnjicenKtor(var idDokazOsumnjicen: Int, var dokazId: Int, var osumnjicenId: Int)

@Serializable
data class OneContactR(val idOneContact: Int, val zlocinId: Int, val ime: String, val broj: String, val slika: Int)

@Serializable
data class BeleskaR (val idBeleska: Int, val zlocinId: Int, val tekst: String, val datum: String)

@Serializable
data class WhatsAppKontaktR (val idWhatsAppKontakt: Int, val zlocinId: Int, val ime: String, val broj: String, val slika: Int?)

@Serializable
data class WhatsAppPorukaR (val idWhatsAppPoruka: Int, var kontaktKoSalje: Int, var kontaktKomeSalje: Int, val tekst: String, val datum: String, val procitana: Boolean)

@Serializable
data class OneCallR (val idOneCall: Int, var kontakt: Int, val datum: String, val propusten: Boolean, val dolazni: Boolean)

@Serializable
data class GalleryR (val idPhoto: Int, val zlocinId: Int, val slika: Int?, val datum: String, val mesto: String)

@Serializable
data class ObicnaPorukaR (val idObicnaPoruka: Int, var kontaktKoSalje: Int, var kontaktKomeSalje: Int, val tekst: String, val datum: String, val procitana: Boolean)

@Serializable
data class OdnosOsumnjicenZrtvaR (val idOdnos: Int, var osumnjicenId:  Int, val zrtvaId:  Int, var tipOdnosa: String)

@Serializable
data class PrijavljeniKorisnikR (val idKorisnik: Int, val korisnickoIme: String, val sifra: String)

@Serializable
data class PitanjeR (val idPitanje: Int, val zlocinId: Int, val tekst: String)

@Serializable
data class OdgovorR (val idOdogovor: Int, var pitanjeId: Int, val tekstOdgovora: String, val tacan: Boolean, val bodovi: Int)

@Serializable
data class PitanjeIspitivanjeOsumnjicenogR (val idPitanjeIspitivanjeOsumnjicenog: Int, var kategorija: String, val tekst: String, val odgovor: String, val komentar: String, var osumnjicenId: Int)

@Serializable
data class PitanjeIspitivanjeSvedokaR (val idPitanjeIspitivanjeSvedoka: Int, val tekst: String, val odgovor: String, var svedokId: Int, val nextPitanje: Int)

@Serializable
open class ZadatakR (val idZadatak: Int, val tekst: String = "", val korak: String = "", val uradjen: Boolean = false, val nextZadatak: Int? = null, val zlocinId: Int = 0)

@Serializable
data class IspitivanjeOsumnjicenogZadatakR (var idIspitivanjeOsumnjicenogZadatak: Int, var osumnjicenId: Int, var zadatakId: Int, val uradjen: Boolean)

@Serializable
data class DokazZadatakR (val idDokazZadatak: Int, val tekst: String, var dokazId: Int, val uradjen: Boolean, var zadatakId: Int)

@Serializable
data class IspitivanjeSvedokaZadatakR (val idIspitivanjeSvedokaZadatak: Int, var svedokId: Int, var zadatakId: Int, val uradjen: Boolean)

@Serializable
data class TelefonZadatakR (val idTelefonZadatak: Int, var telefonId: Int, var zadatakId: Int, val uradjen: Boolean)

@Serializable
data class ForenzickiDokazZadatakR (val idForenzickiDokazZadatak: Int, val tekst: String, var forenzickiDokazId: Int, val uradjen: Boolean, var zadatakId: Int)

@Serializable
data class PorukeZadatakR (var idPorukeZadatak: Int, val porukeId: Int, val zadatakId: Int, val uradjen: Boolean)