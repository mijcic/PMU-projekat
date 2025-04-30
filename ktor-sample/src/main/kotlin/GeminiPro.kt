package com.example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
data class ZlocinR(
    val idZlocin: Int,
    val tipZlocinaId: Int,
    val naziv: String,
    val datum: String?,
    val mesto: String,
    val opis: String,
    val status:String
)

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
data class Part(
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
    val osobaR:  List<OsobaR>,
    //val motivR: List<MotivR>,
    val osumnjicenR:  List<OsumnjicenR>,
    val dokazR:  List<DokazR>,
    //val dokazZadatakR:  List<DokazZadatakR>,
    val svedokR:  List<SvedokR>,
    val zrtvaR:  ZrtvaR,
    val obdukcijaR: ObdukcijaR,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
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

suspend fun queryGemini(prompt: String,tables:String): String {
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

            //to insert data into mysql database
            getDataGeminiResponse(geminiResponse)

            geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "No textual response received from Gemini."
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

fun getDataGeminiResponse(geminiResponse:GeminiResponse){
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
    println(geminiResponse2)

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



                /*
                val osobe = geminiResponse2.osobaR

                for(o in osobe){
                    println(o)
                    val datumStr = o.datum
                    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
                    var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

                    if(timestamp2==null){
                        timestamp2=timestamp
                    }
                    val os= timestamp2?.let {
                        OsobaData(
                            idOsoba = o.idOsoba,
                            ime = o.ime,
                            kontakt = o.kontakt,
                            datum = it,
                            zanimanje = o.zanimanje,
                            pol = o.pol,
                            zlocinId = o.zlocinId
                        )
                    }
                    if (os != null) {
                        insertOsobaData(os,zl)
                    }
                }
                */

                //zrtva i osoba
                val sviDokaziZrtva = insertGeminiZrtva(geminiResponse2,timestamp,zl)

                //osumnjiceni i osobe (izmeniti)
                val osumnjiceniLista = insertGeminiOsumnjicen(geminiResponse2,timestamp,zl)

                //svedoci
                val svedociLista = insertGeminiSvedok(geminiResponse2,timestamp,zl)

                // kontakti
                val kontaktiLista = insertGeminiOneContact(geminiResponse2, zl)

                // beleske
                insertGeminiBeleska(geminiResponse2, zl, timestamp)

                // whatsAppKontakt
                val whatsAppKontaktiLista = insertGeminiWhatsAppKontakt(geminiResponse2, zl)

                //  whatsAppPoruka

                insertGeminiWhatsAppPoruka(geminiResponse2, whatsAppKontaktiLista, timestamp)

                // one call

                insertGeminiOneCall(geminiResponse2, kontaktiLista, timestamp)

                // gallery

                insertGeminiGallery(geminiResponse2, zl, timestamp)

                //  obicnaPoruka

                insertGeminiObicnaPoruka(geminiResponse2, kontaktiLista, timestamp)

                // odnos osumnjicen zrtva

                sviDokaziZrtva.zrtva?.let { it1 ->
                    insertGeminiOdnosOsumnjicenZrtva(geminiResponse2, osumnjiceniLista,
                        it1
                    )
                }

                // prijavljeni korisnik

                insertGeminiPrijavljeniKorisnik(geminiResponse2)

                // pitanje

                val pitanjaLista = insertGeminiPitanje(geminiResponse2, zl)

                // odgovor

                insertGeminiOdgovor(geminiResponse2, pitanjaLista)
                
                // pitanjeIspitivanjeOsumnjicenog

                insertGeminiPitanjeIspitivanjeOsumnjicenog(geminiResponse2, osumnjiceniLista)
                
                // pitanjeIspitivanjeSvedoka

                insertGeminiPitanjeIspitivanjeSvedoka(geminiResponse2, svedociLista)

                // osoba

                insertGeminiOsoba(geminiResponse2, zl, timestamp)

                // zadatak

                val zadaciLista = insertGeminiZadatak(geminiResponse2, zl)
                updateGeminiZadatakList(geminiResponse2, zl)

                // dokazZadatak

                insertGeminiDokazZadatak(geminiResponse2, sviDokaziZrtva.dokaziLista, zadaciLista)

                // ispitivanjeOsumnjicenogZadatak

                insertGeminiIspitivanjeOsumnjicenogZadatak(geminiResponse2, osumnjiceniLista, zadaciLista)

                // ispitivanjeSvedokaZadatak

                insertGeminiIspitivanjeSvedokaZadatak(geminiResponse2, svedociLista, zadaciLista)

                // telefonZadatak

                insertGeminiTelefonZadatak(geminiResponse2, sviDokaziZrtva.telefoniLista, zadaciLista)

                // forenzickiDokazZadatak

                insertGeminiForenzickiDokazZadatak(geminiResponse2, sviDokaziZrtva.forenzickiDokaziLista, zadaciLista)

                // porukeZadatak
            }
        }
    }
}

data class SviDokaziOdZrtve(
    val dokaziLista: MutableList<DokazData>,
    val telefoniLista: MutableList<TelefonData>,
    val forenzickiDokaziLista: MutableList<ForenzickiDokazData>,
    val zrtva: ZrtvaData?
)


fun insertGeminiZrtva(geminiResponse2: GeminiResponse2,timestamp:Long,zl:ZlocinData): SviDokaziOdZrtve {
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
            osobaId = 1
        )

        insertZrtva(zr, zl,os)

        // dokazi
        dokaziLista = insertGeminiDokaz(geminiResponse2,zl,zr)

        //obdukcija
        insertGeminiObdukcija(geminiResponse2,zl,zr,timestamp)

        //forenzicki dokazi
        forenzickiDokaziLista = insertGeminiForenzickiDokaz(geminiResponse2,zr)

        //telefon
        telefoniLista = insertGeminiTelefon(geminiResponse2,zr)
    }
    return SviDokaziOdZrtve(dokaziLista, telefoniLista, forenzickiDokaziLista, zr)
}

fun insertGeminiDokaz(geminiResponse2: GeminiResponse2, zl:ZlocinData, zrtva:ZrtvaData): MutableList<DokazData> {
    val dokazi = geminiResponse2.dokazR
    var dokaziLista = mutableListOf<DokazData>()

    for(d in dokazi){
        val prev = d.idDokaz
        val dokaz = DokazData(
            idDokaz = d.idDokaz,
            tipDokaza = d.tipDokaza,
            opis = d.opis,
            zlocinId = zl.idZlocin,
            zrtvaId = zrtva.idZrtva,
            status = d.status
        )

        insertDokazData(dokaz, zl, zrtva)

        val dokazZadatak = geminiResponse2.dokazZadatakR.find { it.dokazId == prev }
        dokazZadatak?.dokazId = dokaz.idDokaz

        dokaziLista.add(dokaz)
    }
    return dokaziLista
}

fun insertGeminiTelefon(geminiResponse2: GeminiResponse2, zrtva:ZrtvaData): MutableList<TelefonData> {
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
    return telefoniLista
}

fun insertGeminiForenzickiDokaz(geminiResponse2: GeminiResponse2, zrtva:ZrtvaData): MutableList<ForenzickiDokazData> {
    val dokazi = geminiResponse2.forenzickiDokazR
    var dokaziLista = mutableListOf<ForenzickiDokazData>()

    for(d in dokazi){
        val prev = d.idForenzickiDokaz
        val dokaz = ForenzickiDokazData(
            idForenzickiDokaz = d.idForenzickiDokaz,
            tipForenzickiDokaz = d.tipForenzickiDokaz,
            opis = d.opis,
            statusS = d.statusS,
            veza = d.veza
        )

        insertForenzickiDokaz(dokaz, zrtva)

        val forenzickiDokazZadatak = geminiResponse2.forenzickiDokazZadatakR.find { it.forenzickiDokazId == prev }
        forenzickiDokazZadatak?.forenzickiDokazId = dokaz.idForenzickiDokaz

        dokaziLista.add(dokaz)
    }
    return dokaziLista
}

fun insertGeminiObdukcija(geminiResponse2: GeminiResponse2,zl: ZlocinData,zrtva: ZrtvaData,timestamp: Long){
    val obdukcija =geminiResponse2.obdukcijaR
    val datumStr = obdukcija.datum
    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
    var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

    if(timestamp2==null){
        timestamp2=timestamp
    }

    insertObdukcijaData(
        ObdukcijaData(
            idObdukcija = obdukcija.idObdukcija,
            izvestaj = obdukcija.izvestaj,
            datum = timestamp2,
            uzrokSmrti = obdukcija.uzrokSmrti,
            zrtvaId = obdukcija.zrtvaId,
            informacije = obdukcija.informacije
        ),
        zrtva = zrtva
    )
}

fun insertGeminiOsumnjicen(geminiResponse2: GeminiResponse2,timestamp:Long,zl:ZlocinData): MutableList<OsumnjicenData> {
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

            o.motiv?.let { it1 ->
                o.osobaId?.let { it2 ->
                    OsumnjicenData(
                        idOsumnjicen = o.idOsumnjicen,
                        status = o.status,
                        tipOsumnjicen = o.tipOsumnjicen,
                        motiv = it1.idMotiv,
                        zlocinId = o.zlocinId,
                        kriv = o.kriv,
                        osobaId = os.idOsoba
                    )
                }
            }?.let { it2 ->
                if (m != null) {
                    insertOsumnjicenData(
                        it2,
                        zlocin = zl,
                        motiv = m,
                        zrtva = ZrtvaData(0,"","","",1,1)
                    )

                    val ispitivanjeOsumnjicenogZadatak = geminiResponse2.ispitivanjeOsumnjicenogZadatakR.find { it.osumnjicenId == prev }
                    ispitivanjeOsumnjicenogZadatak?.osumnjicenId = it2.idOsumnjicen

                    val odnosOsumnjicenZrtva = geminiResponse2.odnosOsumnjicenZrtvaR.find { it.osumnjicenId == prev }
                    odnosOsumnjicenZrtva?.osumnjicenId = it2.idOsumnjicen

                    val pitanjeIspitivanjeOsumnjicenog = geminiResponse2.pitanjeIspitivanjeOsumnjicenogR.find { it.osumnjicenId == prev }
                    pitanjeIspitivanjeOsumnjicenog?.osumnjicenId = it2.idOsumnjicen

                    osumnjiceniLista.add(it2)
                }
            }
        }
    }
    return osumnjiceniLista
}

fun insertGeminiSvedok(geminiResponse2: GeminiResponse2,timestamp:Long,zl:ZlocinData): MutableList<SvedokData>{
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
                osobaId = os.idOsoba
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
    return svedociLista
}

fun insertGeminiOneContact(geminiResponse2: GeminiResponse2, zl:ZlocinData): MutableList<OneContactData> {
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
    return kontaktiLista
}

fun insertGeminiBeleska(geminiResponse2: GeminiResponse2, zl: ZlocinData, timestamp: Long) {
    val beleske = geminiResponse2.beleskaR

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
    }
}

fun insertGeminiWhatsAppKontakt(geminiResponse2: GeminiResponse2, zl: ZlocinData): MutableList<WhatsAppKontaktData> {
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
    return whatsAppLista
}

fun insertGeminiWhatsAppPoruka(geminiResponse2: GeminiResponse2, kontaktiLista: MutableList<WhatsAppKontaktData>, timestamp: Long) {
    val whatsAppPoruke = geminiResponse2.whatsAppPorukaR

    println("PORUKEEEEE: " + geminiResponse2.whatsAppPorukaR)

    for(w in whatsAppPoruke){
        val waKontaktKoSalje = kontaktiLista.find { it.idWhatsAppKontakt == w.kontaktKoSalje }
        val waKontaktKomeSalje = kontaktiLista.find { it.idWhatsAppKontakt == w.kontaktKomeSalje }

        println("KONTAKT1: " + waKontaktKoSalje)
        println("KONTAKT2: " + waKontaktKomeSalje)
        println("KONTAKT3: " + w)
        println("PORUKA: " + w.tekst)

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
        }
    }
}

fun insertGeminiOneCall(geminiResponse2: GeminiResponse2, kontaktiLista: MutableList<OneContactData>, timestamp: Long) {
    val pozivi = geminiResponse2.oneCallR

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
        }
    }
}

fun insertGeminiGallery(geminiResponse2: GeminiResponse2, zl: ZlocinData, timestamp: Long) {
    val galerija = geminiResponse2.galleryR

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
    }
}

fun insertGeminiObicnaPoruka(geminiResponse2: GeminiResponse2, kontaktiLista: MutableList<OneContactData>, timestamp: Long) {
    val obicnePoruke = geminiResponse2.obicnaPorukaR

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
        }
    }
}

fun insertGeminiOdnosOsumnjicenZrtva(geminiResponse2: GeminiResponse2, osumnjicenLista: MutableList<OsumnjicenData>, zrtva: ZrtvaData) {
    val odnosi = geminiResponse2.odnosOsumnjicenZrtvaR
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
        }
    }
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

fun insertGeminiPitanje(geminiResponse2: GeminiResponse2, zl: ZlocinData): MutableList<PitanjeData> {
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
    return pitanjeList
}

fun insertGeminiOdgovor(geminiResponse2: GeminiResponse2, pitanjeLista: MutableList<PitanjeData>) {
    val odgovori = geminiResponse2.odgovorR

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
        }
    }
}

fun insertGeminiPitanjeIspitivanjeOsumnjicenog(geminiResponse2: GeminiResponse2, osumnjiceniLista: MutableList<OsumnjicenData>) {
    val pitanja = geminiResponse2.pitanjeIspitivanjeOsumnjicenogR
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
        }
    }
}

fun insertGeminiPitanjeIspitivanjeSvedoka(geminiResponse2: GeminiResponse2, svedociLista: MutableList<SvedokData>) {
    val pitanja = geminiResponse2.pitanjeIspitivanjeSvedokaR

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
        }
    }
}

fun insertGeminiOsoba(geminiResponse2: GeminiResponse2, zlocin: ZlocinData, timestamp: Long) {
    val osobe = geminiResponse2.osobaR

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
    }
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

fun updateGeminiZadatakList(geminiResponse2: GeminiResponse2, zlocin: ZlocinData) {
    val zadaci = geminiResponse2.zadatakR
    var lista: List<ZadatakData> = emptyList()
    lista = getZadatakListaData()
    updateZadatakListData(lista)
}

fun insertGeminiDokazZadatak(geminiResponse2: GeminiResponse2, dokazList: MutableList<DokazData>, zadatakList: MutableList<ZadatakData>) {
    val dokazi = geminiResponse2.dokazZadatakR

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
        }
    }
}

fun insertGeminiIspitivanjeOsumnjicenogZadatak(geminiResponse2: GeminiResponse2, osumnjicenList: MutableList<OsumnjicenData>, zadatakList: MutableList<ZadatakData>) {
    val ispitivanja = geminiResponse2.ispitivanjeOsumnjicenogZadatakR

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
        }
    }
}

fun insertGeminiIspitivanjeSvedokaZadatak(geminiResponse2: GeminiResponse2, svedokList: MutableList<SvedokData>, zadatakList: MutableList<ZadatakData>) {
    val ispitivanja = geminiResponse2.ispitivanjeSvedokaZadatakR

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
        }
    }
}

fun insertGeminiTelefonZadatak(geminiResponse2: GeminiResponse2, telefonList: MutableList<TelefonData>, zadatakList: MutableList<ZadatakData>) {
    val zadaci = geminiResponse2.telefonZadatakR

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
        }
    }
}

fun insertGeminiForenzickiDokazZadatak(geminiResponse2: GeminiResponse2, forenzickiDokazList: MutableList<ForenzickiDokazData>, zadatakList: MutableList<ZadatakData>) {
    val zadaci = geminiResponse2.forenzickiDokazZadatakR

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
        }
    }
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