package com.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

interface GeminiResponseCommon2{
    val dokazR: List<DokazR>
    val dokazZadatakR:List<DokazZadatakR>
    val osobaR:  List<OsobaR>
    val forenzickiDokazR: List<ForenzickiDokazR>
    val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>
    val telefonR: List<TelefonR>
    val telefonZadatakR: List<TelefonZadatakR>
    val aplikacijaKtor: List<AplikacijaKtor>

    val oneContactR: List<OneContactR>
    val beleskaR: List<BeleskaR>
    val whatsAppKontaktR: List<WhatsAppKontaktR>
    val whatsAppPorukaR: List<WhatsAppPorukaR>
    val oneCallR: List<OneCallR>
    val galleryR: List<GalleryR>
    val obicnaPorukaR: List<ObicnaPorukaR>
    val pitanjeR: List<PitanjeR>
    val odgovorR: List<OdgovorR>
    val zadatakR: List<ZadatakR>
}
interface GeminiResponseRetrofitCommon{
    var osobeRetrofit: List<OsobaData>?
    var dokaziRetrofit:List<DokazData>?
    var forenzickiDokazRetrofit: List<ForenzickiDokazData>?
    var forenzickiDokazZadaciRetrofit: List<ForenzickiDokazZadatakData>?
    var telefoniRetrofit: List<TelefonData>?
    var telefonZadaciRetrofit: List<TelefonZadatakData>?
    var aplikacijeRetrofit: List<AplikacijaData>?

    var oneContactRetrofit: List<OneContactData>?
    var beleskeRetrofit: List<BeleskaData>?
    var whatsappKontaktRetrofit: List<WhatsAppKontaktData>?
    var whatsappPorukaRetrofit: List<WhatsAppPorukaData>?
    var oneCallRetrofit: List<OneCallData>?
    var galleryRetrofit: List<GalleryData>?
    var obicnePorukeRetrofit: List<ObicnaPorukaData>?
    var pitanjaRetrofit: List<PitanjeData>?
    var odgovoriRetrofit: List<OdgovorData>?
    var zadaciRetrofit: List<ZadatakData>?
    var dokaziZadaciRetrofit: List<DokazZadatakData>?
}

@Serializable
data class GeminiRequest2(
    val prompt: String,
    val tables: Tables
)

@Serializable
data class Tables(
    val zlocinR: ZlocinR,
    val osobaR: List<OsobaR>,
    //val motivR: List<MotivR>,
    val osumnjicenR: List<OsumnjicenR>,
    val dokazR: List<DokazR>,
    //val dokazZadatakR: DokazZadatakR,
    val svedokR: List<SvedokR>,
    val zrtvaR: ZrtvaR,
    val obdukcijaR: ObdukcijaR,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
    //val dokazOsumnjicenR: List<DokazOsumnjicenR>,
    //val zadatakR: ZadatakR,
    //val alibiR: List<AlibiR>,
    val kontaktKtor: List<KontaktKtor>,
    val porukeKtor: List<PorukeKtor>,
    val poziviKtor: List<PoziviKtor>,
    val galerijaKtor: List<GalerijaKtor>,
    val aplikacijaKtor: List<AplikacijaKtor>,
    val tragKtor: List<TragKtor>,
    val dokazOsumnjicenKtor: List<DokazOsumnjicenKtor>,
    val oneContactR: List<OneContactR>,
    val beleskaR: List<BeleskaR>,
    val whatsAppKontaktR: List<WhatsAppKontaktR>,
    val whatsAppPorukaR: List<WhatsAppPorukaR>,
    val oneCallR: List<OneCallR>,
    val galleryR: List<GalleryR>,
    val obicnaPorukaR: List<ObicnaPorukaR>,
    val odnosOsumnjicenZrtvaR: List<OdnosOsumnjicenZrtvaR>,
    val prijavljeniKorisnikR: List<PrijavljeniKorisnikR>,
    val pitanjeR: List<PitanjeR>,
    val odgovorR: List<OdgovorR>,
    val pitanjeIspitivanjeOsumnjicenogR: List<PitanjeIspitivanjeOsumnjicenogR>,
    val pitanjeIspitivanjeSvedokaR: List<PitanjeIspitivanjeSvedokaR>,
    val zadatakR: List<ZadatakR>,
    val dokazZadatakR: List<DokazZadatakR>,
    val ispitivanjeOsumnjicenogZadatakR: List<IspitivanjeOsumnjicenogZadatakR>,
    val ispitivanjeSvedokaZadatakR: List<IspitivanjeSvedokaZadatakR>,
    val telefonZadatakR: List<TelefonZadatakR>,
    val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,
    //val porukeZadatakR: List<PorukeZadatakR>
)


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

// --- Konstante ---
// UPOZORENJE: NIKADA NE STAVLJAJ API KLJUČ DIREKTNO U KOD U PRODUKCIJI!
// Koristi environment variable ili configuration fajl.
const val GEMINI_API_KEY = "AIzaSyD0Fssx_oFXYrO4dSoRuSfxhGpn4ziWPQk"

// --- Data Klase za Gemini Zahtev ---
@Serializable
data class GeminiRequest(
    val contents: List<Content>
)

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class ContentStream(
    val parts: List<PartStream>
)

@Serializable
data class Part(
    val text: String
)

@Serializable
data class PartStream(
    val section:String,
    val text: String
)

// --- Data Klase za Gemini Odgovor ---
@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(
    val content: Content? = null,
    val finishReason: String? = null,
    val index: Int? = null
)

@Serializable
data class GeminiResponse2(
    val zlocinR: ZlocinR,
    override val osobaR:  List<OsobaR>,
    //val motivR: List<MotivR>,
    val osumnjicenR:  List<OsumnjicenR>,
    override val dokazR:  List<DokazR>,
    val svedokR:  List<SvedokR>,
    val zrtvaR:  ZrtvaR,
    val obdukcijaR: ObdukcijaR,
    override val forenzickiDokazR: List<ForenzickiDokazR>,
    override val telefonR: List<TelefonR>,
    //val dokazOsumnjicenR: List<DokazOsumnjicenR>,
    //val zadatakR: ZadatakR,
    //val alibiR: List<AlibiR>,
    val kontaktKtor: List<KontaktKtor>,
    val porukeKtor: List<PorukeKtor>,
    val poziviKtor: List<PoziviKtor>,
    val galerijaKtor: List<GalerijaKtor>,
    override val aplikacijaKtor: List<AplikacijaKtor>,
    val tragKtor: List<TragKtor>,
    val dokazOsumnjicenKtor: List<DokazOsumnjicenKtor>,
    override val oneContactR: List<OneContactR>,
    override val beleskaR: List<BeleskaR>,
    override val whatsAppKontaktR: List<WhatsAppKontaktR>,
    override val whatsAppPorukaR: List<WhatsAppPorukaR>,
    override val oneCallR: List<OneCallR>,
    override val galleryR: List<GalleryR>,
    override val obicnaPorukaR: List<ObicnaPorukaR>,
    val odnosOsumnjicenZrtvaR: List<OdnosOsumnjicenZrtvaR>,
    val prijavljeniKorisnikR: List<PrijavljeniKorisnikR>,
    override val pitanjeR: List<PitanjeR>,
    override val odgovorR: List<OdgovorR>,
    val pitanjeIspitivanjeOsumnjicenogR: List<PitanjeIspitivanjeOsumnjicenogR>,
    val pitanjeIspitivanjeSvedokaR: List<PitanjeIspitivanjeSvedokaR>,
    override val zadatakR: List<ZadatakR>,
    override val dokazZadatakR: List<DokazZadatakR>,
    val ispitivanjeOsumnjicenogZadatakR: List<IspitivanjeOsumnjicenogZadatakR>,
    val ispitivanjeSvedokaZadatakR: List<IspitivanjeSvedokaZadatakR>,
    override val telefonZadatakR: List<TelefonZadatakR>,
    override val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,
) : GeminiResponseCommon2

@Serializable
data class GeminiResponseRetrofit(
    var zlocinRetrofit: ZlocinData?,
    var zrtvaRetrofit: ZrtvaData?,
    var osumnjiceniRetrofit: List<OsumnjicenData>?,
    override var dokaziRetrofit: List<DokazData>?,
    override var telefoniRetrofit: List<TelefonData>?,
    override var forenzickiDokazRetrofit: List<ForenzickiDokazData>?,
    var obdukcijaRetrofit: ObdukcijaData?,
    var svedociRetrofit: List<SvedokData>?,
    override var oneContactRetrofit: List<OneContactData>?,
    var kontaktiRetrofit: List<KontaktData>?,
    var porukeRetrofit: List<PorukeData>?,
    var poziviRetrofit: List<PoziviData>?,
    var galerijaRetrofit: List<GalerijaData>?,
    override var aplikacijeRetrofit: List<AplikacijaData>?,
    var tragoviRetrofit: List<TragData>?,
    var dokaziOsumnjiceniRetrofit: List<DokazOsumnjicenData>?,
    override var beleskeRetrofit: List<BeleskaData>?,
    override var whatsappKontaktRetrofit: List<WhatsAppKontaktData>?,
    override var whatsappPorukaRetrofit: List<WhatsAppPorukaData>?,
    override var oneCallRetrofit: List<OneCallData>?,
    override var galleryRetrofit: List<GalleryData>?,
    override var obicnePorukeRetrofit: List<ObicnaPorukaData>?,
    var odnosiOsumnjiceniZrtvaRetrofit: List<OdnosOsumnjicenZrtvaData>?,
    override var pitanjaRetrofit: List<PitanjeData>?,
    override var odgovoriRetrofit: List<OdgovorData>?,
    var pitanjeIspitivanjeOsumnjicenogRetrofit: List<PitanjeIspitivanjeOsumnjicenogData>?,
    var pitanjeIspitivanjeSvedokaRetrofit: List<PitanjeIspitivanjeSvedokaData>?,
    override var osobeRetrofit: List<OsobaData>?,
    override var zadaciRetrofit: List<ZadatakData>?,
    override var dokaziZadaciRetrofit: List<DokazZadatakData>?,
    var ispitivanjeOsumnjicenogZadaciRetrofit: List<IspitivanjeOsumnjicenogZadatakData>?,
    var ispitivanjeSvedokaZadaciRetrofit: List<IspitivanjeSvedokaZadatakData>?,
    override var telefonZadaciRetrofit: List<TelefonZadatakData>?,
    override var forenzickiDokazZadaciRetrofit: List<ForenzickiDokazZadatakData>?,
):GeminiResponseRetrofitCommon

@Serializable
data class GeminiResponse2_1(
    val zlocinR: ZlocinR,
    val osobaR:  List<OsobaR>,
    val osumnjicenR:  List<OsumnjicenR>,
    //override val dokazR:  List<DokazR>,
    val svedokR:  List<SvedokR>,
    val zrtvaR:  ZrtvaR,
    //val obdukcijaR: ObdukcijaR,
    //override val forenzickiDokazR: List<ForenzickiDokazR>,
    //override val telefonR: List<TelefonR>,
    //val kontaktKtor: List<KontaktKtor>,
    //val porukeKtor: List<PorukeKtor>,
    //val poziviKtor: List<PoziviKtor>,
    //val galerijaKtor: List<GalerijaKtor>,
    //override val aplikacijaKtor: List<AplikacijaKtor>,
    //val tragKtor: List<TragKtor>,
    //val dokazOsumnjicenKtor: List<DokazOsumnjicenKtor>,
    //override val oneContactR: List<OneContactR>,
    //override val beleskaR: List<BeleskaR>,
    //override val whatsAppKontaktR: List<WhatsAppKontaktR>,
    //override val whatsAppPorukaR: List<WhatsAppPorukaR>,
    //override val oneCallR: List<OneCallR>,
    //override val galleryR: List<GalleryR>,
    //override val obicnaPorukaR: List<ObicnaPorukaR>,
    //val odnosOsumnjicenZrtvaR: List<OdnosOsumnjicenZrtvaR>,
    //val prijavljeniKorisnikR: List<PrijavljeniKorisnikR>,
    //override val pitanjeR: List<PitanjeR>,
    //override val odgovorR: List<OdgovorR>,
    //val pitanjeIspitivanjeOsumnjicenogR: List<PitanjeIspitivanjeOsumnjicenogR>,
    //val pitanjeIspitivanjeSvedokaR: List<PitanjeIspitivanjeSvedokaR>,
    //override val zadatakR: List<ZadatakR>,
    //override val dokazZadatakR: List<DokazZadatakR>,
    //val ispitivanjeOsumnjicenogZadatakR: List<IspitivanjeOsumnjicenogZadatakR>,
    //val ispitivanjeSvedokaZadatakR: List<IspitivanjeSvedokaZadatakR>,
    //override val telefonZadatakR: List<TelefonZadatakR>,
    //override val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>
)

@Serializable
data class GeminiResponseRetrofit_1(
    var zlocinRetrofit: ZlocinData?,
    var zrtvaRetrofit: ZrtvaData?,
    var osumnjiceniRetrofit: List<OsumnjicenData>?,
    //override var dokaziRetrofit: List<DokazData>?,
    //override var telefoniRetrofit: List<TelefonData>?,
    //override var forenzickiDokazRetrofit: List<ForenzickiDokazData>?,
    //var obdukcijaRetrofit: ObdukcijaData?,
    var svedociRetrofit: List<SvedokData>?,
    //override var oneContactRetrofit: List<OneContactData>?,
    //var kontaktiRetrofit: List<KontaktData>?,
    //var porukeRetrofit: List<PorukeData>?,
    //var poziviRetrofit: List<PoziviData>?,
    //var galerijaRetrofit: List<GalerijaData>?,
    //override var aplikacijeRetrofit: List<AplikacijaData>?,
    //var tragoviRetrofit: List<TragData>?,
    //var dokaziOsumnjiceniRetrofit: List<DokazOsumnjicenData>?,
    //override var beleskeRetrofit: List<BeleskaData>?,
    //override var whatsappKontaktRetrofit: List<WhatsAppKontaktData>?,
    //override var whatsappPorukaRetrofit: List<WhatsAppPorukaData>?,
    //override var oneCallRetrofit: List<OneCallData>?,
    //override var galleryRetrofit: List<GalleryData>?,
    //override var obicnePorukeRetrofit: List<ObicnaPorukaData>?,
    //var odnosiOsumnjiceniZrtvaRetrofit: List<OdnosOsumnjicenZrtvaData>?,
    //override var pitanjaRetrofit: List<PitanjeData>?,
    //override var odgovoriRetrofit: List<OdgovorData>?,
    //var pitanjeIspitivanjeOsumnjicenogRetrofit: List<PitanjeIspitivanjeOsumnjicenogData>?,
    //var pitanjeIspitivanjeSvedokaRetrofit: List<PitanjeIspitivanjeSvedokaData>?,
    var osobeRetrofit: List<OsobaData>?,
    //override var zadaciRetrofit: List<ZadatakData>?,
    //override var dokaziZadaciRetrofit: List<DokazZadatakData>?,
    //var ispitivanjeOsumnjicenogZadaciRetrofit: List<IspitivanjeOsumnjicenogZadatakData>?,
    //var ispitivanjeSvedokaZadaciRetrofit: List<IspitivanjeSvedokaZadatakData>?,
    //override var telefonZadaciRetrofit: List<TelefonZadatakData>?,
    //override var forenzickiDokazZadaciRetrofit: List<ForenzickiDokazZadatakData>?,
)//:GeminiResponseRetrofitCommon

// http klijent za komunikaciju sa gemini api-jem
val geminiClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    engine {
        requestTimeout = 60_000 // 60 sekundi timeout za API poziv
    }
}

suspend fun queryGemini(prompt: String,tables:String): Any {
    val request = GeminiRequest(
        contents = listOf(Content(parts = listOf(Part(text = prompt+tables)))),
    )

    try {
        val t0 = System.currentTimeMillis()
        val response: HttpResponse = geminiClient.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$GEMINI_API_KEY") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val t1 = System.currentTimeMillis()
        println("Gemini API trajanje: ${t1 - t0}ms")

        return if (response.status == HttpStatusCode.OK) {
            val geminiResponse: GeminiResponse = response.body()
            val t2 = System.currentTimeMillis()
            println("Parsiranje trajanje: ${t2 - t1}ms")

            //to insert data into mysql database
            val geminiResponseRetrofit =getDataGeminiResponse(geminiResponse)

            val t3 = System.currentTimeMillis()
            println("Insert trajanje: ${t3 - t2}ms")

            geminiResponseRetrofit
        } else {
            val errorBody = response.bodyAsText()
            println("Error from Gemini: ${response.status} - $errorBody")
            "Error during communication with the Gemini API: ${response.status}"
        }

    } catch (e: Exception) {
        println("Exception during the Gemini API call: ${e.message}")
        e.printStackTrace()
        return "Internal error during communication with the AI service."
    }
}

fun getDataGeminiResponse(geminiResponse:GeminiResponse): GeminiResponseRetrofit {
    val json2 =Json {
        ignoreUnknownKeys = true
    }
    val cleanJsonString = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.replace("`", "")
    val cleanJsonString2 = cleanJsonString?.removePrefix("json")
    val geminiResponse2: GeminiResponse2? =
        cleanJsonString2?.let {
            json2.decodeFromString(
                it
            )
        }
    //println(geminiResponse2)

    val geminiResponseRetrofit:GeminiResponseRetrofit=GeminiResponseRetrofit(
        zlocinRetrofit = null,
        zrtvaRetrofit = null,
        osumnjiceniRetrofit = null,
        dokaziRetrofit =null,
        telefoniRetrofit = null,
        forenzickiDokazRetrofit = null,
        obdukcijaRetrofit = null,
        svedociRetrofit =null,
        oneContactRetrofit = null,
        kontaktiRetrofit = null,
        porukeRetrofit = null,
        poziviRetrofit = null,
        galerijaRetrofit = null,
        aplikacijeRetrofit = null,
        tragoviRetrofit = null,
        dokaziOsumnjiceniRetrofit = null,
        beleskeRetrofit = null,
        whatsappKontaktRetrofit = null,
        whatsappPorukaRetrofit = null,
        oneCallRetrofit = null,
        galleryRetrofit = null,
        obicnePorukeRetrofit = null,
        odnosiOsumnjiceniZrtvaRetrofit = null,
        pitanjaRetrofit = null,
        odgovoriRetrofit = null,
        pitanjeIspitivanjeOsumnjicenogRetrofit = null,
        pitanjeIspitivanjeSvedokaRetrofit = null,
        osobeRetrofit = null,
        zadaciRetrofit = null,
        dokaziZadaciRetrofit = null,
        ispitivanjeOsumnjicenogZadaciRetrofit = null,
        ispitivanjeSvedokaZadaciRetrofit = null,
        telefonZadaciRetrofit = null,
        forenzickiDokazZadaciRetrofit = null
    )

    if (geminiResponse2 != null) {
        val datumString = geminiResponse2.zlocinR.datum
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val datum = datumString?.let { LocalDate.parse(it, formatter) }
        val timestamp = datum?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        geminiResponse2.zlocinR.datum?.let {
            if (timestamp != null) {

                val zl =ZlocinData(
                    idZlocin = geminiResponse2.zlocinR.idZlocin,
                    tipZlocinaId = 1,
                    naziv = geminiResponse2.zlocinR.naziv,
                    datum = timestamp,
                    mesto = geminiResponse2.zlocinR.mesto,
                    opis = geminiResponse2.zlocinR.opis,
                    status = geminiResponse2.zlocinR.status,
                )
                insertZlocinData(zl)
                geminiResponseRetrofit.zlocinRetrofit=zl

                //zrtva i osoba
                val sviDokaziZrtva = insertGeminiZrtva(geminiResponse2,geminiResponseRetrofit,timestamp,zl)

                //osumnjiceni i osobe (izmeniti)
                val osumnjiceniLista = insertGeminiOsumnjicen(geminiResponse2,geminiResponseRetrofit,timestamp,zl)

                //svedoci
                val svedociLista = insertGeminiSvedok(geminiResponse2,geminiResponseRetrofit,timestamp,zl)

                // kontakti
                val kontaktiLista = insertGeminiOneContact(geminiResponse2, geminiResponseRetrofit, zl)

                // beleske
                insertGeminiBeleska(geminiResponse2, geminiResponseRetrofit, zl, timestamp)

                // whatsAppKontakt
                val whatsAppKontaktiLista = insertGeminiWhatsAppKontakt(geminiResponse2, geminiResponseRetrofit,zl)

                val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

                scope.launch {
                    suspendInsertKontakti(whatsAppKontaktiLista,geminiResponse2,geminiResponseRetrofit,timestamp,kontaktiLista,zl)
                }

                // odnos osumnjicen zrtva

                sviDokaziZrtva.zrtva?.let { it1 ->
                    insertGeminiOdnosOsumnjicenZrtva(geminiResponse2, geminiResponseRetrofit, osumnjiceniLista,
                        it1
                    )
                }

                // prijavljeni korisnik

                insertGeminiPrijavljeniKorisnik(geminiResponse2)

                // pitanje

                val pitanjaLista = insertGeminiPitanje(geminiResponse2, geminiResponseRetrofit,zl)

                scope.launch {
                    suspendInsertPitanja(pitanjaLista,geminiResponse2,geminiResponseRetrofit,timestamp,osumnjiceniLista,svedociLista,zl)
                }

                // zadatak

                val zadaciLista = insertGeminiZadatak(geminiResponse2, zl)
                updateGeminiZadatakList(geminiResponse2,geminiResponseRetrofit, zl)
                geminiResponseRetrofit.zadaciRetrofit = zadaciLista

                scope.launch {
                    suspendInsertZadaci(zadaciLista, geminiResponse2, geminiResponseRetrofit, osumnjiceniLista, svedociLista, sviDokaziZrtva)
                }

                // porukeZadatak

                //println("GEMINI RESPONSE RETROFIT \n" + geminiResponseRetrofit)

            }
        }
    }
    return geminiResponseRetrofit
}

data class SviDokaziOdZrtve(
    val dokaziLista: MutableList<DokazData>,
    val telefoniLista: MutableList<TelefonData>,
    val forenzickiDokaziLista: MutableList<ForenzickiDokazData>,
    val zrtva: ZrtvaData?
)


fun insertGeminiZrtva(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,timestamp:Long,zl:ZlocinData): SviDokaziOdZrtve {
    val zrtva = geminiResponse2.zrtvaR
    val datumStr = zrtva.osobaId?.datum
    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
    var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

    var dokaziLista: MutableList<DokazData> = mutableListOf()
    var telefoniLista: MutableList<TelefonData> = mutableListOf()
    var forenzickiDokaziLista: MutableList<ForenzickiDokazData> = mutableListOf()

    var zr: ZrtvaData? = null

    if(timestamp2==null){
        timestamp2=timestamp
    }
    val os= timestamp2?.let {
        zrtva.osobaId?.let { it1 ->
            OsobaData(
                idOsoba = it1.idOsoba,
                ime = zrtva.osobaId.ime,
                kontakt = if(zrtva.osobaId.kontakt==null)"" else zrtva.osobaId.kontakt,
                datum = it,
                zanimanje = zrtva.osobaId.zanimanje,
                pol = zrtva.osobaId.pol,
                zlocinId = zrtva.osobaId.zlocinId
            )
        }
    }
    if (os != null) {
        insertOsobaData(os,zl)
    }
    if (os != null) {
        zr=ZrtvaData(
            idZrtva = zrtva.idZrtva,
            tipZrtve = zrtva.tipZrtve,
            detalji = zrtva.detalji,
            statusZrtva = zrtva.statusZrtva,
            zlocinId = zl.idZlocin,
            osobaId = os
        )

        insertZrtva(zr, zl,os)
        geminiResponseRetrofit.zrtvaRetrofit=zr

        //geminiResponseRetrofit.zrtvaRetrofit=zr

        // dokazi
        dokaziLista = insertGeminiDokaz(geminiResponse2,geminiResponseRetrofit,zl,zr)

        //obdukcija
        insertGeminiObdukcija(geminiResponse2,geminiResponseRetrofit,zl,zr,timestamp)

        //forenzicki dokazi
        forenzickiDokaziLista = insertGeminiForenzickiDokaz(geminiResponse2,geminiResponseRetrofit,zr)

        //telefon
        telefoniLista = insertGeminiTelefon(geminiResponse2,geminiResponseRetrofit,zr)
        //insertGeminiTelefon(geminiResponse2,geminiResponseRetrofit,zr)

        //kontakt
        val kontaktLista=insertGeminiKontakt(geminiResponse2, geminiResponseRetrofit,zr)

        //poruke
        insertGeminiPoruke(geminiResponse2,geminiResponseRetrofit,zr,kontaktLista,timestamp)

        //pozivi
        insertGeminiPozivi(geminiResponse2,geminiResponseRetrofit,zr,kontaktLista,timestamp)

        //galerija
        insertGeminiGalerija(geminiResponse2, geminiResponseRetrofit,zr,timestamp)

        //aplikacija
        insertGeminiAplikacija(geminiResponse2,geminiResponseRetrofit,zr)

        //osumnjiceni i osobe (izmeniti)
        var osumnjicenLista=insertGeminiOsumnjicen(geminiResponse2,geminiResponseRetrofit,timestamp,zl)
        //geminiResponseRetrofit.osumnjiceniRetrofit=osumnjicenLista

        insertGeminiTrag(geminiResponse2,geminiResponseRetrofit,forenzickiDokaziLista,osumnjicenLista)

        insertGeminiDokazOsumnjicen(geminiResponse2,geminiResponseRetrofit,dokaziLista,osumnjicenLista)

    }
    return SviDokaziOdZrtve(dokaziLista, telefoniLista, forenzickiDokaziLista, zr)
}

fun insertGeminiDokaz(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,zl:ZlocinData, zrtva:ZrtvaData): MutableList<DokazData> {
    val dokazi = geminiResponse2.dokazR
    var dokaziLista = mutableListOf<DokazData>()

    for(d in dokazi){
        val prev = d.idDokaz
        var dokaz:DokazData?=null
        if(zrtva!=null){
             dokaz = DokazData(
                idDokaz = d.idDokaz,
                tipDokaza = d.tipDokaza,
                opis = d.opis,
                zlocinId = zl.idZlocin,
                zrtvaId = zrtva.idZrtva,
                status = d.status
            )
        }


        if (dokaz != null) {
            insertDokazData(dokaz, zl, zrtva)
        }

        val dokazZadatak = geminiResponse2.dokazZadatakR.find { it.dokazId == prev }
        if (dokaz != null) {
            dokazZadatak?.dokazId = dokaz.idDokaz
            dokaziLista.add(dokaz)
        }

        geminiResponseRetrofit.dokaziRetrofit=dokaziLista
    }
    return dokaziLista
}

fun insertGeminiTelefon(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zrtva:ZrtvaData): MutableList<TelefonData> {
    val telefoni = geminiResponse2.telefonR
    var telefoniLista = mutableListOf<TelefonData>()

    for(t in telefoni){
        val prev = t.idTelefon
        val tel = TelefonData(
            idTelefon = t.idTelefon,
            model = t.model,
            os = t.os,
            sifra = t.sifra,
            informacije = t.informacije
        )

        insertTelefonData(tel, zrtva)


        val telefonZadatak = geminiResponse2.telefonZadatakR.find { it.telefonId == prev }
        telefonZadatak?.telefonId = tel.idTelefon

        telefoniLista.add(tel)

    }
    geminiResponseRetrofit.telefoniRetrofit=telefoniLista
    return telefoniLista
}

fun insertGeminiForenzickiDokaz(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zrtva:ZrtvaData): MutableList<ForenzickiDokazData> {
    val dokazi = geminiResponse2.forenzickiDokazR
    val dokaziLista = mutableListOf<ForenzickiDokazData>()

    for(d in dokazi){
        val prev = d.idForenzickiDokaz
        val dokaz = ForenzickiDokazData(
            idForenzickiDokaz = d.idForenzickiDokaz,
            tipForenzickiDokaz = d.tipForenzickiDokaz,
            opis = d.opis,
            statusS = d.statusS,
            veza = d.veza
        )

        if (zrtva!=null){
            insertForenzickiDokaz(dokaz, zrtva)
        }


        val forenzickiDokazZadatak = geminiResponse2.forenzickiDokazZadatakR.find { it.forenzickiDokazId == prev }
        forenzickiDokazZadatak?.forenzickiDokazId = dokaz.idForenzickiDokaz

        dokaziLista.add(dokaz)
    }
    geminiResponseRetrofit.forenzickiDokazRetrofit=dokaziLista
    return dokaziLista
}

fun insertGeminiObdukcija(geminiResponse2: GeminiResponse2,geminiResponseRetrofit: GeminiResponseRetrofit,zl: ZlocinData,zrtva: ZrtvaData,timestamp: Long){
    val obdukcija =geminiResponse2.obdukcijaR
    val datumStr = obdukcija.datum
    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
    var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

    if(timestamp2==null){
        timestamp2=timestamp
    }

    var ob=ObdukcijaData(
        idObdukcija = obdukcija.idObdukcija,
        izvestaj = obdukcija.izvestaj,
        datum = timestamp2!!,
        uzrokSmrti = obdukcija.uzrokSmrti,
        zrtvaId = obdukcija.zrtvaId,
        informacije = obdukcija.informacije
    )
    insertObdukcijaData(
        obdukcija = ob,
        zrtva = zrtva
    )

    geminiResponseRetrofit.obdukcijaRetrofit=ob
}

fun insertGeminiOsumnjicen(geminiResponse2: GeminiResponse2,geminiResponseRetrofit: GeminiResponseRetrofit,timestamp:Long,zl:ZlocinData): MutableList<OsumnjicenData> {
    val osumnjiceni=geminiResponse2.osumnjicenR
    val osumnjiceniLista = mutableListOf<OsumnjicenData>()

    for(o in osumnjiceni){
        val prev = o.idOsumnjicen
        val datumStr = o.osobaId?.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }
        val os= timestamp2?.let {
            o.osobaId?.let { it1 ->
                OsobaData(
                    idOsoba = it1.idOsoba,
                    ime = o.osobaId.ime,
                    kontakt = if(o.osobaId.kontakt==null)"" else o.osobaId.kontakt,
                    datum = it,
                    zanimanje = o.osobaId.zanimanje,
                    pol = o.osobaId.pol,
                    zlocinId = o.osobaId.zlocinId
                )
            }
        }
        if (os != null) {
            insertOsobaData(os,zl)
        }
        if (os != null) {
            val m= o.motiv?.let { it1 -> MotivData(it1.idMotiv,o.motiv.opis) }
            if (m != null) {
                insertMotivData(m)
            }

            if (m != null) {
                val osum=OsumnjicenData(
                    idOsumnjicen = o.idOsumnjicen,
                    status = o.status,
                    tipOsumnjicen = o.tipOsumnjicen,
                    motiv = m,
                    zlocinId = o.zlocinId,
                    kriv = o.kriv,
                    osobaId = os
                )
                insertOsumnjicenData(
                    osum,
                    zlocin = zl,
                    motiv = m,
                    zrtva = ZrtvaData(0,"","","",1,os)
                )

                val ispitivanjeOsumnjicenogZadatak = geminiResponse2.ispitivanjeOsumnjicenogZadatakR.find { it.osumnjicenId == prev }
                ispitivanjeOsumnjicenogZadatak?.osumnjicenId = osum.idOsumnjicen

                val odnosOsumnjicenZrtva = geminiResponse2.odnosOsumnjicenZrtvaR.find { it.osumnjicenId == prev }
                odnosOsumnjicenZrtva?.osumnjicenId = osum.idOsumnjicen

                val pitanjeIspitivanjeOsumnjicenog = geminiResponse2.pitanjeIspitivanjeOsumnjicenogR.find { it.osumnjicenId == prev }
                pitanjeIspitivanjeOsumnjicenog?.osumnjicenId = osum.idOsumnjicen

                osumnjiceniLista.add(osum)

                val pronadjenOsumnjicen = geminiResponse2.tragKtor.find { it.osumnjicenId==prev }
                pronadjenOsumnjicen?.osumnjicenId=osum.idOsumnjicen

                val pronadjen = geminiResponse2.dokazOsumnjicenKtor.find { it.osumnjicenId==prev }
                pronadjen?.osumnjicenId=osum.idOsumnjicen
                osumnjiceniLista.add(osum)
            }
        }
    }
    geminiResponseRetrofit.osumnjiceniRetrofit=osumnjiceniLista
    return osumnjiceniLista
}

fun insertGeminiSvedok(geminiResponse2: GeminiResponse2,geminiResponseRetrofit: GeminiResponseRetrofit,timestamp:Long,zl:ZlocinData): MutableList<SvedokData>{
    val svedoci=geminiResponse2.svedokR
    val svedociLista = mutableListOf<SvedokData>()

    for(s in svedoci){
        val prev = s.idSvedok
        val datumStr = s.osobaId?.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }
        val os= timestamp2?.let {
            s.osobaId?.let { it1 ->
                OsobaData(
                    idOsoba = it1.idOsoba,
                    ime = s.osobaId.ime,
                    kontakt = if(s.osobaId.kontakt==null)"" else s.osobaId.kontakt,
                    datum = it,
                    zanimanje = s.osobaId.zanimanje,
                    pol = s.osobaId.pol,
                    zlocinId = s.osobaId.zlocinId
                )
            }
        }
        if (os != null) {
            insertOsobaData(os,zl)
        }
        if (os != null) {
            val svedok = SvedokData(
                idSvedok = s.idSvedok,
                izjava = s.izjava,
                statusSvedok = s.statusSvedok,
                statusIspitan = s.statusIspitan,
                zlocinId = s.zlocinId,
                osobaId = os
            )
            insertSvedokData(
                svedok = svedok,
                zlocin = zl
            )

            val pronadjenoIspitivanjeSvedokaZadatak = geminiResponse2.ispitivanjeSvedokaZadatakR.find { it.svedokId == prev }
            pronadjenoIspitivanjeSvedokaZadatak?.svedokId = svedok.idSvedok

            val pitanjeIspitivanjeSvedoka = geminiResponse2.pitanjeIspitivanjeSvedokaR.find { it.svedokId == prev }
            pitanjeIspitivanjeSvedoka?.svedokId = svedok.idSvedok

            svedociLista.add(svedok)
        }
    }
    geminiResponseRetrofit.svedociRetrofit = svedociLista
    return svedociLista
}

fun insertGeminiOneContact(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl:ZlocinData): MutableList<OneContactData> {
    val kontakti = geminiResponse2.oneContactR
    val kontaktiLista = mutableListOf<OneContactData>()

    for(k in kontakti){
        val prev = k.idOneContact

        val kontakt = OneContactData(
            idOneContact = k.idOneContact,
            zlocinId = k.zlocinId,
            ime = k.ime,
            broj = k.broj,
            slika = k.slika,
        )

        insertOneContactData(kontakt, zl)

        val oneCall = geminiResponse2.oneCallR.find { it.kontakt == prev }
        oneCall?.kontakt = kontakt.idOneContact

        val kontaktKoSalje = geminiResponse2.obicnaPorukaR.find { it.kontaktKoSalje == prev }
        kontaktKoSalje?.kontaktKoSalje = kontakt.idOneContact

        val kontaktKomeSalje = geminiResponse2.obicnaPorukaR.find { it.kontaktKomeSalje == prev }
        kontaktKomeSalje?.kontaktKomeSalje = kontakt.idOneContact

        kontaktiLista.add(kontakt)
    }
    geminiResponseRetrofit.oneContactRetrofit = kontaktiLista
    return kontaktiLista
}

fun insertGeminiKontakt(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,zrtva: ZrtvaData):MutableList<KontaktData>{
    val kontakti =geminiResponse2.kontaktKtor
    var kontaktiLista = mutableListOf<KontaktData>()

    for(k in kontakti){
        val prev=k.idKontakt
        val kont=KontaktData(
            idKontakt =k.idKontakt,
            ime = k.ime,
            broj = k.broj,
            status = k.status,
            zrtvaId =zrtva
        )
        insertKontaktData(
            kontakt = kont,
            zrtva = zrtva
        )
        val pronadjenaPoruka = geminiResponse2.porukeKtor.find { it.posiljalacId == prev }
        pronadjenaPoruka?.posiljalacId = kont.idKontakt

        val pronadjenPoziv = geminiResponse2.poziviKtor.find { it.kontaktId==prev }
        pronadjenPoziv?.kontaktId=kont.idKontakt
        kontaktiLista.add(kont)
    }
    geminiResponseRetrofit.kontaktiRetrofit = kontaktiLista
    return kontaktiLista
}

fun insertGeminiPoruke(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,zrtva: ZrtvaData,kontaktLista: MutableList<KontaktData>,timestamp: Long){
    val poruke =geminiResponse2.porukeKtor
    var porukeLista = mutableListOf<PorukeData>()

    for(p in poruke){
        val kontakt=kontaktLista.find { it.idKontakt==p.posiljalacId }

        val datumStr = p.datumVreme
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH)
        val dateTime = LocalDateTime.parse(datumStr, formatter)
        var timestamp2 = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        if(timestamp2==null){
            timestamp2=timestamp
        }
        kontakt?.let {
            PorukeData(
                idPoruke = p.idPoruke,
                tipPoruke = p.tipPoruke,
                sadrzaj = p.sadrzaj,
                datumVreme = timestamp2,
                zrtvaId = zrtva,
                posiljalacId = it,
                statusPoruke = p.statusPoruke,
                sifrovana = p.sirovana
            )
        }?.let {
            insertPorukeData(
                poruke = it,
                zrtva = zrtva,
                kontakt = kontakt
            )
            porukeLista.add(it)
        }

    }
    geminiResponseRetrofit.porukeRetrofit = porukeLista
}

fun insertGeminiPozivi(geminiResponse2: GeminiResponse2,geminiResponseRetrofit: GeminiResponseRetrofit,zrtva: ZrtvaData,kontaktLista: MutableList<KontaktData>,timestamp: Long){
    val pozivi =geminiResponse2.poziviKtor
    var poziviLista = mutableListOf<PoziviData>()

    for(p in pozivi){
        val kontakt=kontaktLista.find { it.idKontakt == p.kontaktId }

        val datumStr = p.datumVreme
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH)
        val dateTime = LocalDateTime.parse(datumStr, formatter)
        var timestamp2 = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        if(timestamp2==null){
            timestamp2=timestamp
        }

        if (kontakt != null) {
            val poz=PoziviData(
                idPoziv = p.idPoziv,
                tip = p.tip,
                broj = p.broj,
                datumVreme = timestamp2,
                zrtvaId = zrtva,
                status = p.status,
                kontaktId = kontakt
            )
            insertPoziviData(
                pozivi = poz,
                zrtva = zrtva,
                kontakt = kontakt
            )

            poziviLista.add(poz)
        }
    }
    geminiResponseRetrofit.poziviRetrofit = poziviLista
}


fun insertGeminiGalerija(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,zrtva: ZrtvaData,timestamp: Long){
    val galerija =geminiResponse2.galerijaKtor
    val galerijaLista = mutableListOf<GalerijaData>()

    for(g in galerija){
        val datumStr = g.datumVreme
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH)
        val dateTime = LocalDateTime.parse(datumStr, formatter)
        var timestamp2 = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
        if(timestamp2==null){
            timestamp2=timestamp
        }

        val gal=GalerijaData(
            idGalerija = g.idGalerija,
            tip = g.tip,
            putanja = g.putanja,
            zrtvaId = zrtva,
            datumVreme = timestamp2,
            lokacija = g.lokacija
        )
        insertGalerijaData(
            galerija = gal,
            zrtva = zrtva
        )
        galerijaLista.add(gal)
    }
    geminiResponseRetrofit.galerijaRetrofit = galerijaLista
}

fun insertGeminiAplikacija(geminiResponse2: GeminiResponseCommon2,geminiResponseRetrofit: GeminiResponseRetrofitCommon,zrtva: ZrtvaData){
    val aplikacija =geminiResponse2.aplikacijaKtor
    val aplikacijaLista = mutableListOf<AplikacijaData>()

    for(a in aplikacija){
        val ap=AplikacijaData(
            idAplikacije = a.idAplikacije,
            naziv = a.naziv,
            tip = a.tip,
            zrtvaId = zrtva,
            aktivna = a.aktivna,
            informacije = a.informacije
        )

        insertAplikacijaData(
            aplikacija = ap,
            zrtva = zrtva
        )
        aplikacijaLista.add(ap)
    }
    geminiResponseRetrofit.aplikacijeRetrofit = aplikacijaLista
}

fun insertGeminiTrag(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,forenzickiDokaz: MutableList<ForenzickiDokazData>, osumnjicen: MutableList<OsumnjicenData>){
    val tragovi =geminiResponse2.tragKtor
    val tragLista = mutableListOf<TragData>()

    for(t in tragovi){
        val fz=forenzickiDokaz.find { it.idForenzickiDokaz==t.forenzickiDokazId }
        val os=osumnjicen.find { it.idOsumnjicen==t.osumnjicenId }

        if (fz!=null && os!=null){
            val tr =TragData(
                idTrag = t.idTrag,
                forenzickiDokazId = fz,
                osumnjicenId = os
            )
            insertTragData(
                trag = tr ,
                forenzickiDokaz = fz,
                osumnjicen = os
            )
            tragLista.add(tr)
        }
    }
    geminiResponseRetrofit.tragoviRetrofit = tragLista
}

fun insertGeminiDokazOsumnjicen(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,dokazi: MutableList<DokazData>, osumnjicen: MutableList<OsumnjicenData>){
    val dokaziOsumnjiceni =geminiResponse2.dokazOsumnjicenKtor
    val dokaziOsumnjiceniLista = mutableListOf<DokazOsumnjicenData>()

    for(dok in dokaziOsumnjiceni){
        val d=dokazi.find { it.idDokaz==dok.dokazId }
        val os=osumnjicen.find { it.idOsumnjicen==dok.osumnjicenId }

        if (d!=null && os!=null){
            val dokOs=DokazOsumnjicenData(
                idDokazOsumnjicen = dok.idDokazOsumnjicen,
                dokazId = d,
                osumnjicenId = os
            )
            insertDokazOsumnjicenData(
                dokazOsumnjicen =dokOs ,
                dokaz = d,
                osumnjicen = os
            )
            dokaziOsumnjiceniLista.add(dokOs)
        }
    }
    geminiResponseRetrofit.dokaziOsumnjiceniRetrofit = dokaziOsumnjiceniLista
}

suspend fun queryGeminiRetrofit(prompt: String,tables:String): Any? {
    val request = GeminiRequest(
        contents = listOf(Content(parts = listOf(Part(text = prompt+tables)))),
    )

    try {
        // Model: gemini-1.5-flash je brz i efikasan. Možeš koristiti i gemini-pro.
        val response: HttpResponse = geminiClient.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$GEMINI_API_KEY") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        println("Gemini - Status: ${response.status}")


        return if (response.status == HttpStatusCode.OK) {
            val geminiResponse: GeminiResponse = response.body()
            println("${geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text}")

            val json2 = Json {
                ignoreUnknownKeys = true
            }
            val cleanJsonString =
                geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.replace("`", "")
            val cleanJsonString2 = cleanJsonString?.removePrefix("json")
            val geminiResponse2: GeminiResponse2? =
                cleanJsonString2?.let {
                    json2.decodeFromString(
                        it
                    )
                }
            geminiResponse2


        } else {
            val errorBody = response.bodyAsText()
            println("Error from Gemini: ${response.status} - $errorBody")
            "Error during communication with the Gemini API: ${response.status}"
        }

    } catch (e: Exception) {
        println("Exception during the Gemini API call: ${e.message}")
        e.printStackTrace()
        return "Internal error during communication with the AI service."
    }
}




fun insertGeminiBeleska(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData, timestamp: Long) {
    val beleske = geminiResponse2.beleskaR
    val beleskaLista = mutableListOf<BeleskaData>()

    for(b in beleske){
        val datumStr = b.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        val beleska = BeleskaData(
            idBeleska = b.idBeleska,
            zlocinId = b.zlocinId,
            tekst = b.tekst,
            datum = timestamp2,
        )

        insertBeleskaData(beleska, zl)
        beleskaLista.add(beleska)
    }
    geminiResponseRetrofit.beleskeRetrofit = beleskaLista
}

fun insertGeminiWhatsAppKontakt(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,zl: ZlocinData): MutableList<WhatsAppKontaktData> {
    val whatsAppKontakti = geminiResponse2.whatsAppKontaktR
    val whatsAppLista = mutableListOf<WhatsAppKontaktData>()

    for(w in whatsAppKontakti){
        val prev = w.idWhatsAppKontakt

        val whatsAppKontakt = WhatsAppKontaktData(
            idWhatsAppKontakt = w.idWhatsAppKontakt,
            zlocinId = w.zlocinId,
            ime = w.ime,
            broj = w.broj,
            slika = w.slika
        )

        insertWhatsAppKontaktData(whatsAppKontakt, zl)

        val waKontaktKoSalje = geminiResponse2.whatsAppPorukaR.find { it.kontaktKoSalje == prev }
        waKontaktKoSalje?.kontaktKoSalje = whatsAppKontakt.idWhatsAppKontakt

        val waKontaktKomeSalje = geminiResponse2.whatsAppPorukaR.find { it.kontaktKomeSalje == prev }
        waKontaktKomeSalje?.kontaktKomeSalje = whatsAppKontakt.idWhatsAppKontakt

        whatsAppLista.add(whatsAppKontakt)
    }
    geminiResponseRetrofit.whatsappKontaktRetrofit = whatsAppLista
    return whatsAppLista
}

fun insertGeminiWhatsAppPoruka(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,kontaktiLista: MutableList<WhatsAppKontaktData>, timestamp: Long) {
    val whatsAppPoruke = geminiResponse2.whatsAppPorukaR
    val whatsAppPorukaLista = mutableListOf<WhatsAppPorukaData>()


    for(w in whatsAppPoruke){
        val waKontaktKoSalje = kontaktiLista.find { it.idWhatsAppKontakt == w.kontaktKoSalje }
        val waKontaktKomeSalje = kontaktiLista.find { it.idWhatsAppKontakt == w.kontaktKomeSalje }

        val datumStr = w.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        if (waKontaktKoSalje != null && waKontaktKomeSalje != null) {
            val whatsAppPoruka = WhatsAppPorukaData(
                idWhatsAppPoruka = w.idWhatsAppPoruka,
                kontaktKoSalje = waKontaktKoSalje.idWhatsAppKontakt,
                kontaktKomeSalje = waKontaktKomeSalje.idWhatsAppKontakt,
                tekst = w.tekst,
                datum = timestamp2,
                procitana = w.procitana
            )

            insertWhatsAppPorukaData(whatsAppPoruka, waKontaktKoSalje, waKontaktKomeSalje)
            whatsAppPorukaLista.add(whatsAppPoruka)
        }
    }
    geminiResponseRetrofit.whatsappPorukaRetrofit = whatsAppPorukaLista
}

fun insertGeminiOneCall(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,kontaktiLista: MutableList<OneContactData>, timestamp: Long) {
    val pozivi = geminiResponse2.oneCallR
    val poziviLista = mutableListOf<OneCallData>()

    for(p in pozivi){
        val kontakt = kontaktiLista.find { it.idOneContact == p.kontakt }

        val datumStr = p.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        if (kontakt != null) {
            val poziv = OneCallData(
                idOneCall = p.idOneCall,
                kontakt = kontakt.idOneContact,
                datum = timestamp2,
                propusten = p.propusten,
                dolazni = p.dolazni
            )

            insertOneCallData(poziv, kontakt)
            poziviLista.add(poziv)
        }
    }
    geminiResponseRetrofit.oneCallRetrofit = poziviLista
}

fun insertGeminiGallery(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,zl: ZlocinData, timestamp: Long) {
    val galerija = geminiResponse2.galleryR
    val galleryLista = mutableListOf<GalleryData>()

    for(g in galerija){
        val datumStr = g.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        val slika = GalleryData(
            idPhoto = g.idPhoto,
            zlocinId = zl.idZlocin,
            slika = g.slika,
            datum = timestamp2,
            mesto = g.mesto
        )

        insertGalleryData(slika, zl)
        galleryLista.add(slika)
    }
    geminiResponseRetrofit.galleryRetrofit= galleryLista
}

fun insertGeminiObicnaPoruka(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, kontaktiLista: MutableList<OneContactData>, timestamp: Long) {
    val obicnePoruke = geminiResponse2.obicnaPorukaR
    val obicnePorukeLista = mutableListOf<ObicnaPorukaData>()

    for(p in obicnePoruke){
        val kontaktKoSalje = kontaktiLista.find { it.idOneContact == p.kontaktKoSalje }
        val kontaktKomeSalje = kontaktiLista.find { it.idOneContact == p.kontaktKomeSalje }

        val datumStr = p.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        if (kontaktKoSalje != null && kontaktKomeSalje != null) {
            val poruka = ObicnaPorukaData(
                idObicnaPoruka = p.idObicnaPoruka,
                kontaktKoSalje = kontaktKoSalje.idOneContact,
                kontaktKomeSalje = kontaktKomeSalje.idOneContact,
                tekst = p.tekst,
                datum = timestamp2,
                procitana = p.procitana
            )

            insertObicnaPorukaData(poruka, kontaktKoSalje, kontaktKomeSalje)
            obicnePorukeLista.add(poruka)
        }
    }
    geminiResponseRetrofit.obicnePorukeRetrofit = obicnePorukeLista
}

fun insertGeminiOdnosOsumnjicenZrtva(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit, osumnjicenLista: MutableList<OsumnjicenData>, zrtva: ZrtvaData) {
    val odnosi = geminiResponse2.odnosOsumnjicenZrtvaR
    val odnosOsumnjicenZrtvaLista = mutableListOf<OdnosOsumnjicenZrtvaData>()
    val validValues = listOf("poslovni", "licni", "porodicni", "rivalski", "slucajni", "ljubavni", "kolege")

    for(o in odnosi){
        val osumnjiceni = osumnjicenLista.find { it.idOsumnjicen == o.osumnjicenId }
        if (o.tipOdnosa !in validValues) {
            o.tipOdnosa = "slucajni"
        }

        if (osumnjiceni != null) {
            val odnos = OdnosOsumnjicenZrtvaData(
                idOdnos = o.idOdnos,
                osumnjicenId = osumnjiceni.idOsumnjicen,
                zrtvaId = zrtva.idZrtva,
                tipOdnosa = o.tipOdnosa
            )

            insertOdnosOsumnjicenZrtvaData(odnos, osumnjiceni, zrtva)
            odnosOsumnjicenZrtvaLista.add(odnos)
        }
    }
    geminiResponseRetrofit.odnosiOsumnjiceniZrtvaRetrofit = odnosOsumnjicenZrtvaLista
}

fun insertGeminiPrijavljeniKorisnik(geminiResponse2: GeminiResponse2) {
    val korisnici = geminiResponse2.prijavljeniKorisnikR

    for(k in korisnici){
        val korisnik = PrijavljeniKorisnikData(
            idKorisnik = k.idKorisnik,
            korisnickoIme = k.korisnickoIme,
            sifra = k.sifra
        )

        insertPrijavljeniKorisnikData(korisnik)
    }
}

fun insertGeminiPitanje(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, zl: ZlocinData): MutableList<PitanjeData> {
    val pitanja = geminiResponse2.pitanjeR
    val pitanjeList = mutableListOf<PitanjeData>()

    for(p in pitanja){
        val prev = p.idPitanje
        val pitanje = PitanjeData(
            idPitanje = p.idPitanje,
            zlocinId = zl.idZlocin,
            tekst = p.tekst
        )

        insertPitanjeData(pitanje, zl)

        val pronadjeniOdgovor = geminiResponse2.odgovorR.find { it.pitanjeId == prev }
        pronadjeniOdgovor?.pitanjeId = pitanje.idPitanje

        pitanjeList.add(pitanje)
    }
    geminiResponseRetrofit.pitanjaRetrofit = pitanjeList
    return pitanjeList
}

fun insertGeminiOdgovor(geminiResponse2: GeminiResponseCommon2,geminiResponseRetrofit: GeminiResponseRetrofitCommon, pitanjeLista: MutableList<PitanjeData>) {
    val odgovori = geminiResponse2.odgovorR
    val odgovoriLista = mutableListOf<OdgovorData>()

    for(o in odgovori){
        val pitanje = pitanjeLista.find { it.idPitanje == o.pitanjeId }

        if (pitanje != null) {
            val odgovor = OdgovorData(
                idOdogovor = o.idOdogovor,
                pitanjeId = pitanje.idPitanje,
                tekstOdgovora = o.tekstOdgovora,
                tacan = o.tacan,
                bodovi = o.bodovi
            )

            insertOdgovorData(odgovor, pitanje)
            odgovoriLista.add(odgovor)
        }
    }
    geminiResponseRetrofit.odgovoriRetrofit = odgovoriLista
}

fun insertGeminiPitanjeIspitivanjeOsumnjicenog(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,osumnjiceniLista: MutableList<OsumnjicenData>) {
    val pitanja = geminiResponse2.pitanjeIspitivanjeOsumnjicenogR
    val pitanjaIspitivanjeOsumnjicenogLista = mutableListOf<PitanjeIspitivanjeOsumnjicenogData>()
    val validValues = listOf("opsta", "alibi", "dokaz", "kontradikcija")

    for(p in pitanja){
        val osumnjiceni = osumnjiceniLista.find { it.idOsumnjicen == p.osumnjicenId }
        if (p.kategorija !in validValues) {
            p.kategorija = "opsta"
        }

        if (osumnjiceni != null) {
            val pitanje = PitanjeIspitivanjeOsumnjicenogData(
                idPitanjeIspitivanjeOsumnjicenog = p.idPitanjeIspitivanjeOsumnjicenog,
                kategorija = p.kategorija,
                tekst = p.tekst,
                odgovor = p.odgovor,
                komentar = p.komentar,
                osumnjicenId = osumnjiceni.idOsumnjicen
            )

            insertPitanjeIspitivanjeOsumnjicenogData(pitanje, osumnjiceni)
            pitanjaIspitivanjeOsumnjicenogLista.add(pitanje)
        }
    }
    geminiResponseRetrofit.pitanjeIspitivanjeOsumnjicenogRetrofit = pitanjaIspitivanjeOsumnjicenogLista
}

fun insertGeminiPitanjeIspitivanjeSvedoka(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,svedociLista: MutableList<SvedokData>) {
    val pitanja = geminiResponse2.pitanjeIspitivanjeSvedokaR
    val pitanjeIspitivanjeSvedokaLista = mutableListOf<PitanjeIspitivanjeSvedokaData>()

    for(p in pitanja){
        var svedok = svedociLista.find { it.idSvedok == p.svedokId }

        if (svedok != null) {
            val pitanje = PitanjeIspitivanjeSvedokaData(
                idPitanjeIspitivanjeSvedoka = p.idPitanjeIspitivanjeSvedoka,
                tekst = p.tekst,
                odgovor = p.odgovor,
                svedokId = svedok.idSvedok,
                nextPitanje = p.nextPitanje
            )

            insertPitanjeIspitivanjeSvedokaData(pitanje, svedok)
            pitanjeIspitivanjeSvedokaLista.add(pitanje)
        }
    }
    geminiResponseRetrofit.pitanjeIspitivanjeSvedokaRetrofit = pitanjeIspitivanjeSvedokaLista
}

fun insertGeminiOsoba(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,zlocin: ZlocinData, timestamp: Long) {
    val osobe = geminiResponse2.osobaR
    val osobeLista = mutableListOf<OsobaData>()

    for(o in osobe){
        val datumStr = o.datum
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dat = datumStr?.let { LocalDate.parse(it.toString(), formatter2) }
        var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

        if(timestamp2==null){
            timestamp2=timestamp
        }

        val osoba = OsobaData(
            idOsoba = o.idOsoba,
            ime = o.ime,
            kontakt = o.kontakt?.toString() ?: "",
            datum = timestamp2,
            zanimanje = o.zanimanje,
            pol = o.pol,
            zlocinId = zlocin.idZlocin
        )

        insertOsobaData(osoba, zlocin)
        osobeLista.add(osoba)
    }
    geminiResponseRetrofit.osobeRetrofit = osobeLista
}

fun insertGeminiZadatak(geminiResponse2: GeminiResponse2, zlocin: ZlocinData): MutableList<ZadatakData> {
    val zadaci = geminiResponse2.zadatakR
    var zadaciLista = mutableListOf<ZadatakData>()

    for(z in zadaci){
        val prev = z.idZadatak
        val zad = ZadatakData(
            idZadatak = z.idZadatak,
            tekst = z.tekst,
            korak = z.korak,
            uradjen = z.uradjen,
            nextZadatak = z.nextZadatak,
            zlocinId = zlocin.idZlocin
        )

        insertZadatakData(zad, zlocin)

        val pronadjenoIspitivanjeSvedokaZadatak = geminiResponse2.ispitivanjeSvedokaZadatakR.find { it.zadatakId == prev }
        pronadjenoIspitivanjeSvedokaZadatak?.zadatakId = zad.idZadatak

        val dokazZadatak = geminiResponse2.dokazZadatakR.find { it.zadatakId == prev }
        dokazZadatak?.zadatakId = zad.idZadatak

        val ispitivanjeOsumnjicenogZadatak = geminiResponse2.ispitivanjeOsumnjicenogZadatakR.find { it.zadatakId == prev }
        ispitivanjeOsumnjicenogZadatak?.zadatakId = zad.idZadatak

        val telefonZadatak = geminiResponse2.telefonZadatakR.find { it.zadatakId == prev }
        telefonZadatak?.zadatakId = zad.idZadatak

        val forenzickiDokazZadatak = geminiResponse2.forenzickiDokazZadatakR.find { it.zadatakId == prev }
        forenzickiDokazZadatak?.zadatakId = zad.idZadatak

        zadaciLista.add(zad)
    }

    return zadaciLista
}

fun updateGeminiZadatakList(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,zlocin: ZlocinData) {
    val zadaci = geminiResponse2.zadatakR
    var lista: List<ZadatakData> = emptyList()
    lista = getZadatakListaData()
    updateZadatakListData(lista,zlocin)
    //geminiResponseRetrofit.zadaciRetrofit =lista
}

fun insertGeminiDokazZadatak(geminiResponse2: GeminiResponseCommon2,geminiResponseRetrofit: GeminiResponseRetrofitCommon, dokazList: MutableList<DokazData>, zadatakList: MutableList<ZadatakData>) {
    val dokazi = geminiResponse2.dokazZadatakR
    val dokaziLista = mutableListOf<DokazZadatakData>()

    for(d in dokazi) {
        val dokaz = dokazList.find { it.idDokaz == d.dokazId }
        val zadatak = zadatakList.find { it.idZadatak == d.zadatakId }

        if (dokaz != null && zadatak != null) {
            val dokazZadatak = DokazZadatakData(
                idDokazZadatak = d.idDokazZadatak,
                tekst = d.tekst,
                dokazId = dokaz.idDokaz,
                uradjen = d.uradjen,
                zadatakId = zadatak.idZadatak
            )

            insertDokazZadatakData(dokazZadatak, dokaz, zadatak)
            dokaziLista.add(dokazZadatak)
        }
    }
    geminiResponseRetrofit.dokaziZadaciRetrofit = dokaziLista
}

fun insertGeminiIspitivanjeOsumnjicenogZadatak(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,osumnjicenList: MutableList<OsumnjicenData>, zadatakList: MutableList<ZadatakData>) {
    val ispitivanja = geminiResponse2.ispitivanjeOsumnjicenogZadatakR
    val ispitivanjeOsumnjicenogZadatakLista = mutableListOf<IspitivanjeOsumnjicenogZadatakData>()

    for(i in ispitivanja) {
        val osumnjicen = osumnjicenList.find { it.idOsumnjicen == i.osumnjicenId }
        val zadatak = zadatakList.find { it.idZadatak == i.zadatakId }

        if (osumnjicen != null && zadatak != null) {
            val ispitivanjeOsumnjicenogZadatak = IspitivanjeOsumnjicenogZadatakData(
                idIspitivanjeOsumnjicenogZadatak = i.idIspitivanjeOsumnjicenogZadatak,
                osumnjicenId = osumnjicen.idOsumnjicen,
                zadatakId = zadatak.idZadatak,
                uradjen = i.uradjen
            )

            insertIspitivanjeOsumnjicenogZadatakData(ispitivanjeOsumnjicenogZadatak, osumnjicen, zadatak)
            ispitivanjeOsumnjicenogZadatakLista.add(ispitivanjeOsumnjicenogZadatak)
        }
    }
    geminiResponseRetrofit.ispitivanjeOsumnjicenogZadaciRetrofit = ispitivanjeOsumnjicenogZadatakLista
}

fun insertGeminiIspitivanjeSvedokaZadatak(geminiResponse2: GeminiResponse2, geminiResponseRetrofit: GeminiResponseRetrofit,svedokList: MutableList<SvedokData>, zadatakList: MutableList<ZadatakData>) {
    val ispitivanja = geminiResponse2.ispitivanjeSvedokaZadatakR
    val ispitivanjeSvedokaZadatakLista = mutableListOf<IspitivanjeSvedokaZadatakData>()

    for(i in ispitivanja) {
        val zadatak = zadatakList.find { it.idZadatak == i.zadatakId }
        val svedok = svedokList.find { it.idSvedok == i.svedokId }

        if (svedok != null && zadatak != null) {
            val ispitivanjeSvedokaZadatak = IspitivanjeSvedokaZadatakData(
                idIspitivanjeSvedokaZadatak = i.idIspitivanjeSvedokaZadatak,
                svedokId = svedok.idSvedok,
                zadatakId = zadatak.idZadatak,
                uradjen = i.uradjen
            )

            insertIspitivanjeSvedokaZadatakData(ispitivanjeSvedokaZadatak, svedok, zadatak)
            ispitivanjeSvedokaZadatakLista.add(ispitivanjeSvedokaZadatak)
        }
    }
    geminiResponseRetrofit.ispitivanjeSvedokaZadaciRetrofit = ispitivanjeSvedokaZadatakLista
}

fun insertGeminiTelefonZadatak(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon,telefonList: MutableList<TelefonData>, zadatakList: MutableList<ZadatakData>) {
    val zadaci = geminiResponse2.telefonZadatakR
    val telefonZadatakLista = mutableListOf<TelefonZadatakData>()

    for(z in zadaci) {
        val zadatak = zadatakList.find { it.idZadatak == z.zadatakId }
        val telefon = telefonList.find { it.idTelefon == z.telefonId }

        if (telefon != null && zadatak != null) {
            val telefonZadatak = TelefonZadatakData(
                idTelefonZadatak = z.idTelefonZadatak,
                telefonId = telefon.idTelefon,
                zadatakId = zadatak.idZadatak,
                uradjen = z.uradjen
            )

            insertTelefonZadatakData(telefonZadatak, telefon, zadatak)
            telefonZadatakLista.add(telefonZadatak)
        }
    }
    geminiResponseRetrofit.telefonZadaciRetrofit = telefonZadatakLista
}

fun insertGeminiForenzickiDokazZadatak(geminiResponse2: GeminiResponseCommon2, geminiResponseRetrofit: GeminiResponseRetrofitCommon, forenzickiDokazList: MutableList<ForenzickiDokazData>, zadatakList: MutableList<ZadatakData>) {
    val zadaci = geminiResponse2.forenzickiDokazZadatakR
    val forenzickiDokazZadaciLista = mutableListOf<ForenzickiDokazZadatakData>()

    for(z in zadaci) {
        val zadatak = zadatakList.find { it.idZadatak == z.zadatakId }
        val forenzickiDokaz = forenzickiDokazList.find { it.idForenzickiDokaz == z.forenzickiDokazId }

        if (forenzickiDokaz != null && zadatak != null) {
            val forenzickiDokazZadatak = ForenzickiDokazZadatakData(
                idForenzickiDokazZadatak = z.idForenzickiDokazZadatak,
                tekst = z.tekst,
                forenzickiDokazId = forenzickiDokaz.idForenzickiDokaz,
                uradjen = z.uradjen,
                zadatakId = zadatak.idZadatak
            )

            insertForenzickiDokazZadatakData(forenzickiDokazZadatak, forenzickiDokaz, zadatak)
            forenzickiDokazZadaciLista.add(forenzickiDokazZadatak)
        }
    }
    geminiResponseRetrofit.forenzickiDokazZadaciRetrofit = forenzickiDokazZadaciLista
}

//fun insertGeminiPorukeZadatak(geminiResponse2: GeminiResponse2, porukeList: MutableList<PorukeData>, zadatakList: MutableList<ZadatakData>) {
//    val zadaci = geminiResponse2.porukeZadatakR
//
//    for(z in zadaci){
//        val zadatak = zadatakList.find { it.idZadatak == z.zadatakId }
//        val poruka = porukeList.find { it.idPoruke == z.porukeId }
//
//        if (poruka != null && zadatak != null) {
//            val porukaZadatak = PorukeZadatakData(
//                idPorukeZadatak = z.idPorukeZadatak,
//                porukeId = poruka.idPoruke,
//                zadatakId = zadatak.idZadatak,
//                uradjen = z.uradjen
//            )
//
//            insertPorukeZadatakData(porukaZadatak, poruka, zadatak)
//        }
//    }
//}

suspend fun suspendInsertKontakti(
    whatsAppKontaktiLista: MutableList<WhatsAppKontaktData>,
    geminiResponse2: GeminiResponse2,
    geminiResponseRetrofit: GeminiResponseRetrofit,
    timestamp: Long,
    kontaktiLista: MutableList<OneContactData>,
    zl: ZlocinData
) = coroutineScope {

    //  whatsAppPoruka
    val whatsAppPorukaDeferred = async(Dispatchers.IO) {
        insertGeminiWhatsAppPoruka(geminiResponse2, geminiResponseRetrofit, whatsAppKontaktiLista, timestamp)
    }
    // one call
    val oneCallDeferred = async(Dispatchers.IO) {
        insertGeminiOneCall(geminiResponse2, geminiResponseRetrofit,kontaktiLista, timestamp)
    }
    // gallery
    val galleryDeferred = async(Dispatchers.IO) {
        insertGeminiGallery(geminiResponse2, geminiResponseRetrofit,zl, timestamp)
    }
    //  obicnaPoruka
    val obicnaPorukaDeferred = async(Dispatchers.IO) {
        insertGeminiObicnaPoruka(geminiResponse2,geminiResponseRetrofit, kontaktiLista, timestamp)
    }
    // cekanje rezultata
    whatsAppPorukaDeferred.await()
    oneCallDeferred.await()
    galleryDeferred.await()
    obicnaPorukaDeferred.await()
}



suspend fun suspendInsertPitanja(
    pitanjaLista: MutableList<PitanjeData>,
    geminiResponse2: GeminiResponse2,
    geminiResponseRetrofit: GeminiResponseRetrofit,
    timestamp: Long,
    osumnjiceniLista: MutableList<OsumnjicenData>,
    svedociLista: MutableList<SvedokData>,
    zl: ZlocinData
) = coroutineScope {

    // odgovor
    val odgovoriDeferred = async(Dispatchers.IO) {
        insertGeminiOdgovor(geminiResponse2, geminiResponseRetrofit,pitanjaLista)
    }
    // pitanjeIspitivanjeOsumnjicenog
    val pitanjeIspitivanjeOsumnjicenogDeferred = async(Dispatchers.IO) {
        insertGeminiPitanjeIspitivanjeOsumnjicenog(geminiResponse2, geminiResponseRetrofit,osumnjiceniLista)
    }
    // pitanjeIspitivanjeSvedoka
    val pitanjeIspitivanjeSvedokaDeferred = async(Dispatchers.IO) {
        insertGeminiPitanjeIspitivanjeSvedoka(geminiResponse2, geminiResponseRetrofit,svedociLista)
    }
    //  osoba
    val osobaDeferred = async(Dispatchers.IO) {
        insertGeminiOsoba(geminiResponse2, geminiResponseRetrofit, zl, timestamp)
    }
    // cekanje rezultata
    odgovoriDeferred.await()
    pitanjeIspitivanjeOsumnjicenogDeferred.await()
    pitanjeIspitivanjeSvedokaDeferred.await()
    osobaDeferred.await()
}


suspend fun suspendInsertZadaci(
    zadaciLista: MutableList<ZadatakData>,
    geminiResponse2: GeminiResponse2,
    geminiResponseRetrofit: GeminiResponseRetrofit,
    osumnjiceniLista: MutableList<OsumnjicenData>,
    svedociLista: MutableList<SvedokData>,
    sviDokaziZrtva:SviDokaziOdZrtve
) = coroutineScope {
    // dokazZadatak
    val dokazZadatakDeferred = async(Dispatchers.IO) {
        insertGeminiDokazZadatak(geminiResponse2, geminiResponseRetrofit,sviDokaziZrtva.dokaziLista, zadaciLista)
    }
    // ispitivanjeOsumnjicenogZadatak
    val ispitivanjeOsumnjicenogZadatakDeferred = async(Dispatchers.IO) {
        insertGeminiIspitivanjeOsumnjicenogZadatak(geminiResponse2, geminiResponseRetrofit, osumnjiceniLista, zadaciLista)
    }
    // ispitivanjeSvedokaZadatak
    val ispitivanjeSvedokaZadatakDeferred = async(Dispatchers.IO) {
        insertGeminiIspitivanjeSvedokaZadatak(geminiResponse2, geminiResponseRetrofit, svedociLista, zadaciLista)
    }
    // telefonZadatak
    val telefonZadatakDeferred = async(Dispatchers.IO) {
        insertGeminiTelefonZadatak(geminiResponse2, geminiResponseRetrofit,sviDokaziZrtva.telefoniLista, zadaciLista)
    }

    // forenzickiDokazZadatak
    val forenzickiDokazZadatakDeferred = async(Dispatchers.IO) {
        insertGeminiForenzickiDokazZadatak(geminiResponse2, geminiResponseRetrofit,sviDokaziZrtva.forenzickiDokaziLista, zadaciLista)

    }
    // cekanje rezultata
    dokazZadatakDeferred.await()
    ispitivanjeOsumnjicenogZadatakDeferred.await()
    ispitivanjeSvedokaZadatakDeferred.await()
    telefonZadatakDeferred.await()
    forenzickiDokazZadatakDeferred.await()
}