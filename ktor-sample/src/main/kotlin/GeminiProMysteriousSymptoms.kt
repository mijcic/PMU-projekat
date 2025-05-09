package com.example

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@Serializable
data class TablesMysteriousSymptoms(
    val zlocinR: ZlocinR,
    /*
    val osobaR: List<OsobaR>,
    val dokazR: List<DokazR>,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
    val aplikacijaKtor: List<AplikacijaKtor>,
    val oneContactR: List<OneContactR>,
    val beleskaR: List<BeleskaR>,
    val whatsAppKontaktR: List<WhatsAppKontaktR>,
    val whatsAppPorukaR: List<WhatsAppPorukaR>,
    val oneCallR: List<OneCallR>,
    val galleryR: List<GalleryR>,
    val obicnaPorukaR: List<ObicnaPorukaR>,
    val pitanjeR: List<PitanjeR>,
    val odgovorR: List<OdgovorR>,
    val zadatakR: List<ZadatakR>,
    val dokazZadatakR: List<DokazZadatakR>,
    val telefonZadatakR: List<TelefonZadatakR>,
    val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,
    */
    val pacijentR: PacijentR,
    //val medicinskiIzvestajR: MedicinskiIzvestajR,
    //val lekarskiTestR: LekarskiTestR,
    //val lokacijeIstrageR: List<LokacijeIstrageR>
    //val izjavaZaPacijentaR: IzjavaZaPacijentaR
)

@Serializable
data class PacijentR (var idPacijent: Int, val simptomi: String, val statusPacijenta: String, val datumPrijave: String, var prijavio:OsobaR?, var zlocinId: Int, var osobaId: OsobaR?)


@Serializable
data class MedicinskiIzvestajR (var idMedicinskiIzvestaj: Int, val rezime: String, val CTnalaz: String, val MRInalaz: String, val krvnaSlika: String, val toksikoloskeAnalize: String, val zakljucak: String, var pacijentId: Int)

@Serializable
data class LekarskiTestR (var idLekarskiTest: Int, val izvestaj: String)

@Serializable
data class LokacijeIstrageR (var idLokacijeIstrage: Int, val mesto: String, val naziv: String, val opis: String)


@Serializable
data class GeminiResponse2MysteriousSymptoms(
    val zlocinR: ZlocinR,
    /*
    val osobaR: List<OsobaR>,
    val dokazR: List<DokazR>,
    val forenzickiDokazR: List<ForenzickiDokazR>,
    val telefonR: List<TelefonR>,
    val aplikacijaKtor: List<AplikacijaKtor>,
    val oneContactR: List<OneContactR>,
    val beleskaR: List<BeleskaR>,
    val whatsAppKontaktR: List<WhatsAppKontaktR>,
    val whatsAppPorukaR: List<WhatsAppPorukaR>,
    val oneCallR: List<OneCallR>,
    val galleryR: List<GalleryR>,
    val obicnaPorukaR: List<ObicnaPorukaR>,
    val pitanjeR: List<PitanjeR>,
    val odgovorR: List<OdgovorR>,
    val zadatakR: List<ZadatakR>,
    val dokazZadatakR: List<DokazZadatakR>,
    val telefonZadatakR: List<TelefonZadatakR>,
    val forenzickiDokazZadatakR: List<ForenzickiDokazZadatakR>,
    */
    val pacijentR: PacijentR,
   // val medicinskiIzvestajR: MedicinskiIzvestajR,
    //val lekarskiTestR: LekarskiTestR,
    //val lokacijeIstrageR: List<LokacijeIstrageR>
    //val izjavaZaPacijentaR: IzjavaZaPacijentaR
)


@Serializable
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

    val pacijentRetrofit: PacijentData?,
    val medicinskiIzvestajRetrofit: MedicinskiIzvestajData?,
    val lekarskiTestRetrofit: LekarskiTestData?,
    val lokacijeIstrageRetrofit: List<LokacijeIstrageData>?
    //val izjavaZaPacijentaR: IzjavaZaPacijentaData
)


fun insertGeminiPacijent(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms) {
    val pacijent = geminiResponse2.pacijentR

    val datumString = geminiResponse2.pacijentR.datumPrijave
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val datum = datumString?.let { LocalDate.parse(it, formatter) }
    val timestamp = datum?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

    val prijavio = pacijent.prijavio?.let {
        it.kontakt?.let { it1 ->
            if (timestamp != null) {
                OsobaData(
                    idOsoba = it.idOsoba,
                    ime = it.ime,
                    kontakt = it1,
                    datum = timestamp,
                    zanimanje = it.zanimanje,
                    pol = it.pol,
                    zlocinId = it.zlocinId
                )
            }
        }
    }

}

fun insertGeminiMedicinskiIzvestaj(geminiResponse2: GeminiResponse2MysteriousSymptoms, geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms) {
   // val pacijent = geminiResponse2.medicinskiIzvestajR
}



suspend fun queryGeminiMysteriousSymptoms(prompt: String,tables:String): Any {
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
            val geminiResponseRetrofit = getDataGeminiResponseMysteriousSymptoms(geminiResponse)

            val t3 = System.currentTimeMillis()
            println("Insert trajanje: ${t3 - t2}ms")

            geminiResponseRetrofit
            //geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            //  ?: "No textual response received from Gemini."
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

fun getDataGeminiResponseMysteriousSymptoms(geminiResponse:GeminiResponse): GeminiResponseRetrofitMysteriousSymptoms {
    val json2 = Json {
        ignoreUnknownKeys = true
    }
    val cleanJsonString = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.replace("`", "")
    val cleanJsonString2 = cleanJsonString?.removePrefix("json")
    val geminiResponse2: GeminiResponse2MysteriousSymptoms? =
        cleanJsonString2?.let {
            json2.decodeFromString(
                it
            )
        }

    val geminiResponseRetrofit:GeminiResponseRetrofitMysteriousSymptoms= GeminiResponseRetrofitMysteriousSymptoms(
        zlocinRetrofit = null,
        dokaziRetrofit = null,
        telefoniRetrofit = null,
        forenzickiDokazRetrofit = null,
        oneContactRetrofit = null,
        aplikacijeRetrofit = null,
        beleskeRetrofit = null,
        whatsappKontaktRetrofit = null,
        whatsappPorukaRetrofit = null,
        oneCallRetrofit = null,
        galleryRetrofit = null,
        obicnePorukeRetrofit = null,
        pitanjaRetrofit = null,
        odgovoriRetrofit = null,
        osobeRetrofit = null,
        zadaciRetrofit = null,
        dokaziZadaciRetrofit = null,
        telefonZadaciRetrofit = null,
        forenzickiDokazZadaciRetrofit = null,
        pacijentRetrofit = null,
        medicinskiIzvestajRetrofit = null,
        lekarskiTestRetrofit = null,
        lokacijeIstrageRetrofit = null
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

            }
        }
    }
    return geminiResponseRetrofit
}