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
   // val osobaR: List<OsobaR>,
    //val motivR: List<MotivR>,
    val osumnjicenR: List<OsumnjicenR>,
    val dokazR: List<DokazR>,
    //val dokazZadatakR: DokazZadatakR,
    val svedokR: List<SvedokR>,
    val zrtvaR: ZrtvaR
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

@Serializable
data class DokazZadatakR(val idDokazZadatak: Int, val tekst: String,val dokazId:DokazR?, val uradjen:Boolean)

@Serializable
data class SvedokR(val idSvedok: Int, val izjava: String, val statusSvedok: String,val statusIspitan:Int,val zlocinId: Int, val osobaId: OsobaR?)

@Serializable
data class ZrtvaR(val idZrtva: Int, val tipZrtve: String, val detalji:String, val statusZrtva:String,val zlocinId: Int, val osobaId: OsobaR?)


// --- Konstante ---
// UPOZORENJE: NIKADA NE STAVLJAJ API KLJUČ DIREKTNO U KOD U PRODUKCIJI!
// Koristi environment variable ili configuration fajl.
const val GEMINI_API_KEY = ""

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
    //val osobaR:  List<OsobaR>,
    //val motivR: List<MotivR>,
    val osumnjicenR:  List<OsumnjicenR>,
    val dokazR:  List<DokazR>,
    //val dokazZadatakR:  List<DokazZadatakR>,
    val svedokR:  List<SvedokR>,
    val zrtvaR:  ZrtvaR
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
                insertGeminiZrtva(geminiResponse2,timestamp,zl)

                //osumnjiceni i osobe (izmeniti)
                insertGeminiOsumnjicen(geminiResponse2,timestamp,zl)

                //svedoci
                insertGeminiSvedok(geminiResponse2,timestamp,zl)

            }
        }
    }
}

fun insertGeminiZrtva(geminiResponse2: GeminiResponse2,timestamp:Long,zl:ZlocinData){
    val zrtva = geminiResponse2.zrtvaR
    val datumStr = zrtva.osobaId?.datum
    val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dat = datumStr?.let { LocalDate.parse(it, formatter2) }
    var timestamp2 = dat?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

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
        val zr=ZrtvaData(
            idZrtva = zrtva.idZrtva,
            tipZrtve = zrtva.tipZrtve,
            detalji = zrtva.detalji,
            statusZrtva = zrtva.statusZrtva,
            zlocinId = zl.idZlocin,
            osobaId = 1
        )

        insertZrtva(zr, zl,os)

        // dokazi
        insertGeminiDokaz(geminiResponse2,zl,zr)
    }
}

fun insertGeminiDokaz(geminiResponse2: GeminiResponse2,zl:ZlocinData,zrtva:ZrtvaData){
    val dokazi = geminiResponse2.dokazR
    for(d in dokazi){
        val dokaz = DokazData(
            idDokaz = d.idDokaz,
            tipDokaza = d.tipDokaza,
            opis = d.opis,
            zlocinId = d.zlocinId,
            zrtvaId = d.zrtvaId,
            status = d.status
        )
        insertDokazData(dokaz,zl,zrtva)
    }
}

fun insertGeminiOsumnjicen(geminiResponse2: GeminiResponse2,timestamp:Long,zl:ZlocinData){
    val osumnjiceni=geminiResponse2.osumnjicenR

    for(o in osumnjiceni){
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
                }
            }
        }
    }
}

fun insertGeminiSvedok(geminiResponse2: GeminiResponse2,timestamp:Long,zl:ZlocinData){
    val svedoci=geminiResponse2.svedokR

    for(s in svedoci){
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
            insertSvedokData(
                SvedokData(
                    idSvedok = s.idSvedok,
                    izjava = s.izjava,
                    statusSvedok = s.statusSvedok,
                    statusIspitan = s.statusIspitan,
                    zlocinId = s.zlocinId,
                    osobaId = os.idOsoba
                ),
                zlocin = zl
            )
        }
    }

}