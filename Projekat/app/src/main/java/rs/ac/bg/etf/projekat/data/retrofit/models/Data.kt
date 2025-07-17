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
    var idZlocin: Int,
    val tipZlocinaId: Int,
    val naziv: String,
    val datum: Long,
    val mesto: String,
    val opis: String,
    val status: String
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
    val email: String,
    val nacinPrijave: String,
    val idToken: String,
    val idTokenLast256: String
)

// ScorePage korisnici
data class ScorePageKorisnikResponse(
    val mesto:Int,
    val korisnickoIme:String,
    val poeni:Int
)


// gemini



data class OsobaData(
    var idOsoba: Int,
    var ime:String,
    var kontakt: String,
    var datum:Long,
    var zanimanje: String,
    var pol: String,
    var zlocinId:Int
)

data class ZrtvaData(
    var idZrtva: Int,
    val tipZrtve: String,
    val detalji: String,
    val statusZrtva: String,
    val zlocinId:Int,
    val osobaId:OsobaData
)

data class MotivData(
    var idMotiv: Int,
    val opis: String
)

data class OsumnjicenData(
    var idOsumnjicen: Int,
    val status: Int,
    val tipOsumnjicen: String,
    var motiv: MotivData,
    var zlocinId: Int,
    val kriv: Int,
    val osobaId:OsobaData
)

data class DokazData(
    var idDokaz:Int,
    val tipDokaza: String,
    val opis: String,
    val zlocinId: Int,
    val zrtvaId:Int,
    val status: Int,
)

data class SvedokData(
    var idSvedok: Int,
    val izjava: String,
    val statusSvedok: String,
    val statusIspitan: Int,
    val zlocinId: Int,
    val osobaId: OsobaData
)

data class AlibiData(
    var id:Int,
    var osumnjicen: OsumnjicenData,
    var svedok: SvedokData?,
    val opis: String,
    val statusAlibija: String
)

data class ObdukcijaData(
    var idObdukcija:Int,
    val izvestaj:String,
    val datum: Long,
    val uzrokSmrti:String,
    var zrtvaId: Int,
    val informacije: String,
)


data class ForenzickiDokazData (
    var idForenzickiDokaz: Int,
    val tipForenzickiDokaz: String,
    val opis: String,
    val statusS:Int,
    val veza: String,
    val zrtvaId:Int,
)

data class TelefonData(
    var idTelefon: Int,
    val model:String,
    val os: String,
    val sifra: String,
    val informacije: String,
    val zrtvaId: Int
)

data class MisijaData(
    val naziv: String,
    val opis: String,
    val status: Int
)
data class MisijaPorukaData(
    var id: Int,
    val naziv: String,
    val statusS: Int,
    val posiljalac:String,
    val poruka: String,
)

open class ZadatakData (
    var idZadatak: Int,
    val tekst: String,
    val korak: String,
    val uradjen: Boolean,
    val nextZadatak: Int?,
    val zlocinId: Int
)



// telefon zrtve

data class OneContactData(
    var idOneContact: Int,
    val zlocinId: Int,
    val ime: String,
    val broj: String,
    val slika: Int
)

data class BeleskaData (
    var idBeleska: Int,
    val zlocinId: Int,
    val tekst: String,
    val datum: Long
)

data class WhatsAppKontaktData (
    var idWhatsAppKontakt: Int,
    val zlocinId: Int,
    val ime: String,
    val broj: String,
    val slika: Int?
)

data class WhatsAppPorukaData (
    var idWhatsAppPoruka: Int,
    val kontaktKoSalje: Int,
    val kontaktKomeSalje: Int,
    val tekst: String,
    val datum: Long,
    val procitana: Boolean
)

data class OneCallData (
    var idOneCall: Int,
    val kontakt: Int,
    val datum: Long,
    val propusten: Boolean,
    val dolazni: Boolean,
    val zrtvaId: Int
)

data class GalleryData (
    var idPhoto: Int,
    val zlocinId: Int,
    val slika: Int?,
    val datum: Long,
    val mesto: String
)

data class ObicnaPorukaData (
    var idObicnaPoruka: Int,
    val kontaktKoSalje: Int,
    val kontaktKomeSalje: Int,
    val tekst: String,
    val datum: Long,
    val procitana: Boolean
)

data class OdnosOsumnjicenZrtvaData (
    var idOdnos: Int,
    val osumnjicenId:  Int,
    val zrtvaId:  Int,
    val tipOdnosa: String
)

data class PrijavljeniKorisnikData (
    var idKorisnik: Int,
    val korisnickoIme: String,
    val sifra: String,
)

data class PitanjeData (
    var idPitanje: Int,
    val zlocinId: Int,
    val tekst: String
)

data class OdgovorData (
    var idOdogovor: Int,
    val pitanjeId: Int,
    val tekstOdgovora: String,
    val tacan: Boolean,
    val bodovi: Int
)

data class PitanjeIspitivanjeOsumnjicenogData (
    var idPitanjeIspitivanjeOsumnjicenog: Int,
    var kategorija: String,
    val tekst: String,
    val odgovor: String,
    val komentar: String,
    val osumnjicenId: Int
)

data class PitanjeIspitivanjeSvedokaData (
    var idPitanjeIspitivanjeSvedoka: Int,
    val tekst: String,
    val odgovor: String,
    val svedokId: Int,
    val nextPitanje: Int
)

data class DokazZadatakData (
    var idDokazZadatak: Int,
    val tekst: String,
    val dokazId: Int,
    val uradjen: Boolean,
    val zadatakId: Int
)

data class IspitivanjeOsumnjicenogZadatakData (
    var idIspitivanjeOsumnjicenogZadatak: Int,
    val osumnjicenId: Int,
    val zadatakId: Int,
    val uradjen: Boolean
)

data class IspitivanjeSvedokaZadatakData (
    var idIspitivanjeSvedokaZadatak: Int,
    val svedokId: Int,
    val zadatakId: Int,
    val uradjen: Boolean
)

data class TelefonZadatakData (
    var idTelefonZadatak: Int,
    val telefonId: Int,
    val zadatakId: Int,
    val uradjen: Boolean
)

data class ForenzickiDokazZadatakData (
    var idForenzickiDokazZadatak: Int,
    val tekst: String,
    val forenzickiDokazId: Int,
    val uradjen: Boolean,
    val zadatakId: Int
)

data class PorukeZadatakData (
    var idPorukeZadatak: Int,
    val porukeId: Int,
    val zadatakId: Int,
    val uradjen: Boolean
)
data class KontaktData (
    var idKontakt: Int,
    var ime: String,
    var broj: String,
    var status: Int,
    var zrtvaId: ZrtvaData
)

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

data class PoziviData (
    var idPoziv: Int,
    var tip: Int,
    var broj: String,
    var datumVreme: Long,
    var zrtvaId: ZrtvaData,
    var status: Int,
    var kontaktId: KontaktData
)

data class GalerijaData (
    var idGalerija: Int,
    var tip: Int,
    var putanja: String,
    var zrtvaId: ZrtvaData,
    var datumVreme: Long,
    var lokacija: String
)

data class AplikacijaData (
    var idAplikacije: Int,
    var naziv: String,
    var tip: Int,
    var zrtvaId: ZrtvaData,
    var aktivna: Boolean,
    var informacije: String
)

data class TragData(
    var idTrag: Int,
    var forenzickiDokazId: ForenzickiDokazData,
    var osumnjicenId: OsumnjicenData
)

open class DokazOsumnjicenData (
    var idDokazOsumnjicen: Int,
    var dokazId: DokazData,
    var osumnjicenId: OsumnjicenData
)

// RETROFIT 2

data class PacijentData (
    var idPacijent: Int,
    var simptomi: String,
    var statusPacijenta: String, //ziva,mrtva
    var datumPrijave: Long,
    var prijavio: OsobaData,
    var zlocinId: ZlocinData,
    var zrtvaId: ZrtvaData
)

data class MedicinskiIzvestajData (
    var idMedicinskiIzvestaj: Int,
    var rezime: String,
    var CTnalaz: String,
    var MRInalaz: String,
    var krvnaSlika: String,
    var toksikoloskeAnalize: String,
    var zakljucak: String,
    var pacijentId: PacijentData
)

data class LekarskiTestData (
    var idLekarskiTest: Int,
    var pacijentId: PacijentData,
    var izvestaj: String,
)

data class LokacijeIstrageData (
    var idLokacijeIstrage: Int,
    var mesto: String,
    var naziv: String,
    var opis: String,
    var zlocinId: Int,
    var geoTackaALatitude:Double,
    var geoTackaALongitude:Double
)

data class IzjavaZaPacijentaData (
    var idIzjavaZaPacijenta: Int,
    var izjava: String = "",
    var pacijentId: PacijentData,
    var osobaId: OsobaData
)

data class GeminiResponseRetrofit(
    var zlocinRetrofit: ZlocinData?,
    var zrtvaRetrofit: ZrtvaData?,
    var osumnjiceniRetrofit: List<OsumnjicenData>?,
    var dokaziRetrofit: List<DokazData>?,
    var telefoniRetrofit: List<TelefonData>?,
    var forenzickiDokazRetrofit: List<ForenzickiDokazData>?,
    var obdukcijaRetrofit: ObdukcijaData?,
    var svedociRetrofit: List<SvedokData>?,
    var oneContactRetrofit: List<OneContactData>?,
    var kontaktiRetrofit: List<KontaktData>?,
    var porukeRetrofit: List<PorukeData>?,  //ne
    var poziviRetrofit: List<PoziviData>?, //ne
    var galerijaRetrofit: List<GalerijaData>?, //ne
    var aplikacijeRetrofit: List<AplikacijaData>?,
    var tragoviRetrofit: List<TragData>?,
    var dokaziOsumnjiceniRetrofit: List<DokazOsumnjicenData>?,
    var beleskeRetrofit: List<BeleskaData>?,
    var whatsappKontaktRetrofit: List<WhatsAppKontaktData>?,
    var whatsappPorukaRetrofit: List<WhatsAppPorukaData>?,
    var oneCallRetrofit: List<OneCallData>?,
    var galleryRetrofit: List<GalleryData>?,
    var obicnePorukeRetrofit: List<ObicnaPorukaData>?, //ne
    var odnosiOsumnjiceniZrtvaRetrofit: List<OdnosOsumnjicenZrtvaData>?,
    var pitanjaRetrofit: List<PitanjeData>?,
    var odgovoriRetrofit: List<OdgovorData>?,
    var pitanjeIspitivanjeOsumnjicenogRetrofit: List<PitanjeIspitivanjeOsumnjicenogData>?,
    var pitanjeIspitivanjeSvedokaRetrofit: List<PitanjeIspitivanjeSvedokaData>?,
    var osobeRetrofit: List<OsobaData>?,//nisam
    var zadaciRetrofit: List<ZadatakData>?,
    var dokaziZadaciRetrofit: List<DokazZadatakData>?,
    var ispitivanjeOsumnjicenogZadaciRetrofit: List<IspitivanjeOsumnjicenogZadatakData>?,//nisam
    var ispitivanjeSvedokaZadaciRetrofit: List<IspitivanjeSvedokaZadatakData>?,
    var telefonZadaciRetrofit: List<TelefonZadatakData>?,
    var forenzickiDokazZadaciRetrofit: List<ForenzickiDokazZadatakData>?
)

// RETROFIT 2

data class GeminiResponseRetrofitMysteriousSymptoms(
    var zlocinRetrofit: ZlocinData?,
    var dokaziRetrofit: List<DokazData>?,
    var telefoniRetrofit: List<TelefonData>?,
    var forenzickiDokazRetrofit: List<ForenzickiDokazData>?,
    var oneContactRetrofit: List<OneContactData>?,
    var aplikacijeRetrofit: List<AplikacijaData>?,
    var beleskeRetrofit: List<BeleskaData>?,
    var whatsappKontaktRetrofit: List<WhatsAppKontaktData>?,
    var whatsappPorukaRetrofit: List<WhatsAppPorukaData>?,
    var oneCallRetrofit: List<OneCallData>?,
    var galleryRetrofit: List<GalleryData>?,
    var obicnePorukeRetrofit: List<ObicnaPorukaData>?,
    var pitanjaRetrofit: List<PitanjeData>?,
    var odgovoriRetrofit: List<OdgovorData>?,
    var osobeRetrofit: List<OsobaData>?,
    var zadaciRetrofit: List<ZadatakData>?,
    var dokaziZadaciRetrofit: List<DokazZadatakData>?,
    var telefonZadaciRetrofit: List<TelefonZadatakData>?,
    var forenzickiDokazZadaciRetrofit: List<ForenzickiDokazZadatakData>?,

    var pacijentRetrofit: PacijentData?,
    var medicinskiIzvestajRetrofit: MedicinskiIzvestajData?,
    var lekarskiTestRetrofit: LekarskiTestData?,
    var lokacijeIstrageRetrofit: List<LokacijeIstrageData>?,
    var izjavaZaPacijentaRetrofit: IzjavaZaPacijentaData?
)

data class Story(
    val story:String
)