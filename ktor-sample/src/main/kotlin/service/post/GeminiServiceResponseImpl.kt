package com.example.service.post

import com.example.*
import com.example.models.dto.OsobaData
import com.example.models.dto.UsedZlocinData
import com.example.models.dto.ZlocinData
import com.example.models.dto.ZrtvaData
import com.example.models.dto.gemini.response.GeminiResponse
import com.example.models.dto.gemini.retrofit.GeminiResponse2
import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofit
import com.example.repository.RepositoryInsert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Parses the Gemini AI JSON response, maps it to internal data structures,
 * inserts the parsed data into the database, and returns a Retrofit-compatible
 * response object with the inserted data references.
 *
 * The method:
 * - Extracts and cleans the raw JSON string from the GeminiResponse.
 * - Parses it into a `GeminiResponse2` data model.
 * - Parses and converts date strings into timestamps.
 * - Inserts core entities such as crimes (`Zlocin`), victims (`Zrtva`), suspects (`Osumnjiceni`), and related data into the database.
 * - Launches background coroutine jobs for inserting related entities like contacts, questions, and tasks.
 * - Closes the database connection after all operations.
 *
 * @param geminiResponse The raw response object from the Gemini AI containing nested JSON data.
 * @return [GeminiResponseRetrofit] containing references to inserted entities, or empty if parsing or database connection fails.
 *
 * @throws Exception If JSON decoding or date parsing fails, it logs the error and continues.
 * @throws SQLException If database operations fail, they should be handled by the repository layer.
 *
 */
class GeminiServiceResponseImpl(
    private val geminiResponse: GeminiResponse
) : GeminiServiceResponse {
    override suspend fun getDataGeminiResponse(geminiResponse: GeminiResponse): GeminiResponseRetrofit {

        val json2 = Json {
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

        val geminiResponseRetrofit: GeminiResponseRetrofit = GeminiResponseRetrofit(
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

        val conn = getDatabaseConnection()
        if(conn == null){
            return geminiResponseRetrofit
        }
        val repo = RepositoryInsert(conn)

        if (geminiResponse2 != null) {
            val datumString = geminiResponse2.zlocinR.datum
            var timestamp: Long? = null

            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val datum = datumString?.let { LocalDate.parse(it, formatter) }
                timestamp = datum?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
            } catch (e: Exception) {
                try {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val datum = datumString?.let { LocalDateTime.parse(it, formatter) }
                    timestamp = datum?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
                } catch (ex: Exception) {
                    println("Greska pri parsiranju datuma: ${ex.message}")
                }
            }

            geminiResponse2.zlocinR.datum?.let {
                if (timestamp != null) {

                    val zl = ZlocinData(
                        idZlocin = geminiResponse2.zlocinR.idZlocin,
                        tipZlocinaId = 1,
                        naziv = geminiResponse2.zlocinR.naziv,
                        datum = timestamp,
                        mesto = geminiResponse2.zlocinR.mesto,
                        opis = geminiResponse2.zlocinR.opis,
                        status = geminiResponse2.zlocinR.status,
                    )
                    repo.insertZlocinData(zl)
                    geminiResponseRetrofit.zlocinRetrofit=zl

                    val datumStr = geminiResponse2.zrtvaR.osobaId?.datum
                    val datumLong = datumStr?.let {
                        LocalDate.parse(it)
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                    } ?: timestamp
                    val osoba = OsobaData(
                        idOsoba = geminiResponse2.zrtvaR.osobaId?.idOsoba ?: -1,
                        ime = geminiResponse2.zrtvaR.osobaId?.ime ?: "Nepoznato",
                        kontakt = geminiResponse2.zrtvaR.osobaId?.kontakt ?: "Nepoznato",
                        datum = datumLong ?: timestamp,
                        zanimanje = geminiResponse2.zrtvaR.osobaId?.zanimanje ?: "Nepoznato",
                        pol = geminiResponse2.zrtvaR.osobaId?.pol ?: "M",
                        zlocinId = geminiResponse2.zrtvaR.osobaId?.zlocinId ?: -1
                    )
                    repo.insertOsobaData(osoba, zl)

                    val zrtva = ZrtvaData(
                        idZrtva = geminiResponse2.zrtvaR.idZrtva,
                        tipZrtve = geminiResponse2.zrtvaR.tipZrtve,
                        detalji = geminiResponse2.zrtvaR.detalji,
                        statusZrtva = geminiResponse2.zrtvaR.statusZrtva,
                        zlocinId = geminiResponse2.zrtvaR.zlocinId,
                        osobaId = osoba
                    )
                    val usedZlocin = UsedZlocinData(
                        idUsedZlocin = 1,
                        zlocinId = zl,
                        used = false
                    )
                    //repo.insertZlocinData(zl)
                    repo.insertUsedZlocinData(usedZlocin)
                    geminiResponseRetrofit.zlocinRetrofit=zl


                    //zrtva i osoba
                    val sviDokaziZrtva = insertGeminiZrtva(geminiResponse2,geminiResponseRetrofit,timestamp,zl,repo)

                    //osumnjiceni i osobe (izmeniti)
                    val osumnjiceniLista = insertGeminiOsumnjicen(geminiResponse2,geminiResponseRetrofit,timestamp,zl,repo)

                    //svedoci
                    val svedociLista = insertGeminiSvedok(geminiResponse2,geminiResponseRetrofit,timestamp,zl,repo)

                    // kontakti
                    val kontaktiLista = insertGeminiOneContact(geminiResponse2, geminiResponseRetrofit, zl,repo)

                    // beleske
                    insertGeminiBeleska(geminiResponse2, geminiResponseRetrofit, zl, timestamp,repo)

                    // whatsAppKontakt
                    val whatsAppKontaktiLista = insertGeminiWhatsAppKontakt(geminiResponse2, geminiResponseRetrofit,zl,repo)

                    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

                    scope.launch {
                        suspendInsertKontakti(whatsAppKontaktiLista,geminiResponse2,geminiResponseRetrofit,timestamp,kontaktiLista,zl,zrtva, repo)
                    }

                    // odnos osumnjicen zrtva

                    sviDokaziZrtva.zrtva?.let { it1 ->
                        insertGeminiOdnosOsumnjicenZrtva(geminiResponse2, geminiResponseRetrofit, osumnjiceniLista,
                            it1,repo
                        )
                    }

                    // prijavljeni korisnik

                    insertGeminiPrijavljeniKorisnik(geminiResponse2,repo)

                    // pitanje

                    val pitanjaLista = insertGeminiPitanje(geminiResponse2, geminiResponseRetrofit,zl,repo)

                    scope.launch {
                        suspendInsertPitanja(pitanjaLista,geminiResponse2,geminiResponseRetrofit,timestamp,osumnjiceniLista,svedociLista,zl, repo)
                    }

                    // zadatak

                    val zadaciLista = insertGeminiZadatak(geminiResponse2, zl,repo)
                    updateGeminiZadatakList(geminiResponse2,geminiResponseRetrofit, zl,repo)
                    geminiResponseRetrofit.zadaciRetrofit = zadaciLista

                    scope.launch {
                        suspendInsertZadaci(zadaciLista, geminiResponse2, geminiResponseRetrofit, osumnjiceniLista, svedociLista, sviDokaziZrtva, repo)
                    }

                    // porukeZadatak
                }
            }
        }
        conn?.close()
        return geminiResponseRetrofit
    }
}
