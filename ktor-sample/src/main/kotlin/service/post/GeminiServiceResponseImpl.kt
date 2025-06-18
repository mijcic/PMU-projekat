package com.example.service.post

import com.example.*
import com.example.models.dto.*
import com.example.models.dto.gemini.response.GeminiResponse
import com.example.models.dto.gemini.retrofit.GeminiResponse2
import com.example.models.dto.gemini.retrofit.GeminiResponse2MysteriousSymptoms
import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofit
import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofitMysteriousSymptoms
import com.example.repository.GeminiProMysteriousSymptomsRepositoryImpl
import com.example.repository.GeminiProRepositoryImpl
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

        val geminiProRepo = GeminiProRepositoryImpl()
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
                    val sviDokaziZrtva = geminiProRepo.insertGeminiZrtva(geminiResponse2,geminiResponseRetrofit,timestamp,zl,repo)

                    //osumnjiceni i osobe (izmeniti)
                    val osumnjiceniLista = geminiProRepo.insertGeminiOsumnjicen(geminiResponse2,geminiResponseRetrofit,timestamp,zl,repo)

                    //svedoci
                    val svedociLista = geminiProRepo.insertGeminiSvedok(geminiResponse2,geminiResponseRetrofit,timestamp,zl,repo)

                    // kontakti
                    val kontaktiLista = geminiProRepo.insertGeminiOneContact(geminiResponse2, geminiResponseRetrofit, zl,repo)

                    // beleske
                    geminiProRepo.insertGeminiBeleska(geminiResponse2, geminiResponseRetrofit, zl, timestamp,repo)

                    // whatsAppKontakt
                    val whatsAppKontaktiLista = geminiProRepo.insertGeminiWhatsAppKontakt(geminiResponse2, geminiResponseRetrofit,zl,repo)

                    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

                    scope.launch {
                        geminiProRepo.suspendInsertKontakti(whatsAppKontaktiLista,geminiResponse2,geminiResponseRetrofit,timestamp,kontaktiLista,zl,zrtva, repo)
                    }

                    // odnos osumnjicen zrtva

                    sviDokaziZrtva.zrtva?.let { it1 ->
                        geminiProRepo.insertGeminiOdnosOsumnjicenZrtva(geminiResponse2, geminiResponseRetrofit, osumnjiceniLista,
                            it1,repo
                        )
                    }

                    // prijavljeni korisnik

                    geminiProRepo.insertGeminiPrijavljeniKorisnik(geminiResponse2,repo)

                    // pitanje

                    val pitanjaLista = geminiProRepo.insertGeminiPitanje(geminiResponse2, geminiResponseRetrofit,zl,repo)

                    scope.launch {
                        geminiProRepo.suspendInsertPitanja(pitanjaLista,geminiResponse2,geminiResponseRetrofit,timestamp,osumnjiceniLista,svedociLista,zl, repo)
                    }

                    // zadatak

                    val zadaciLista = geminiProRepo.insertGeminiZadatak(geminiResponse2, zl,repo)
                    geminiProRepo.updateGeminiZadatakList(geminiResponse2,geminiResponseRetrofit, zl,repo)
                    geminiResponseRetrofit.zadaciRetrofit = zadaciLista

                    scope.launch {
                        geminiProRepo.suspendInsertZadaci(zadaciLista, geminiResponse2, geminiResponseRetrofit, osumnjiceniLista, svedociLista, sviDokaziZrtva, repo)
                    }

                    // porukeZadatak
                }
            }
        }
        //conn?.close()
        return geminiResponseRetrofit
    }

    override fun getDataGeminiResponseMysteriousSymptoms(geminiResponse: GeminiResponse): GeminiResponseRetrofitMysteriousSymptoms {

        val geminiProRepository = GeminiProRepositoryImpl()
        val geminiProMysteriousSymptomsRepository = GeminiProMysteriousSymptomsRepositoryImpl()

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

        val geminiResponseRetrofit: GeminiResponseRetrofitMysteriousSymptoms = GeminiResponseRetrofitMysteriousSymptoms(
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
            lokacijeIstrageRetrofit = null,
            izjavaZaPacijentaRetrofit = null
        )

        val conn = getDatabaseConnection()
        if(conn==null){
            return geminiResponseRetrofit
        }
        val repo = RepositoryInsert(conn)

        if (geminiResponse2 != null) {
            val datumString = geminiResponse2.zlocinR.datum
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val datum = datumString?.let { LocalDate.parse(it, formatter) }
            val timestamp = datum?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()

            geminiResponse2.zlocinR.datum?.let {
                if (timestamp != null) {

                    val zl = ZlocinData(
                        idZlocin = geminiResponse2.zlocinR.idZlocin,
                        tipZlocinaId = 9,
                        naziv = geminiResponse2.zlocinR.naziv,
                        datum = timestamp,
                        mesto = geminiResponse2.zlocinR.mesto,
                        opis = geminiResponse2.zlocinR.opis,
                        status = geminiResponse2.zlocinR.status,
                    )
                    repo.insertZlocinData(zl)
                    geminiResponseRetrofit.zlocinRetrofit=zl

                    val datumStr = geminiResponse2.pacijentR.zrtvaId?.osobaId?.datum
                    val datumLong = datumStr?.let {
                        LocalDate.parse(it)
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                    } ?: timestamp
                    val osoba = OsobaData(
                        idOsoba = geminiResponse2.pacijentR.zrtvaId?.osobaId?.idOsoba ?: -1,
                        ime = geminiResponse2.pacijentR.zrtvaId?.osobaId?.ime ?: "Nepoznato",
                        kontakt = geminiResponse2.pacijentR.zrtvaId?.osobaId?.kontakt ?: "Nepoznato",
                        datum = datumLong ?: timestamp,
                        zanimanje = geminiResponse2.pacijentR.zrtvaId?.osobaId?.zanimanje ?: "Nepoznato",
                        pol = geminiResponse2.pacijentR.zrtvaId?.osobaId?.pol ?: "M",
                        zlocinId = geminiResponse2.pacijentR.zrtvaId?.osobaId?.zlocinId ?: -1
                    )
                    repo.insertOsobaData(osoba, zl)

                    val zrtva = ZrtvaData(
                        idZrtva = geminiResponse2.pacijentR.zrtvaId?.idZrtva ?: -1,
                        tipZrtve = geminiResponse2.pacijentR.zrtvaId?.tipZrtve ?: "osoba",
                        detalji = geminiResponse2.pacijentR.zrtvaId?.detalji ?: "",
                        statusZrtva = geminiResponse2.pacijentR.zrtvaId?.statusZrtva ?: "ziva",
                        zlocinId = geminiResponse2.pacijentR.zrtvaId?.zlocinId ?: -1,
                        osobaId = osoba
                    )
                    //repo.insertZlocinData(zl)
                    geminiResponseRetrofit.zlocinRetrofit=zl
                    val usedZlocin = UsedZlocinData(
                        idUsedZlocin = 1,
                        zlocinId = zl,
                        used = false
                    )
                    repo.insertUsedZlocinData(usedZlocin)

                    var pacijent= geminiProMysteriousSymptomsRepository.insertGeminiPacijent(geminiResponse2,geminiResponseRetrofit,zl,repo)

                    var dokaziLista: MutableList<DokazData> = mutableListOf()
                    var telefoniLista: MutableList<TelefonData> = mutableListOf()
                    var forenzickiDokaziLista: MutableList<ForenzickiDokazData> = mutableListOf()

                    if (pacijent != null) {
                        geminiProMysteriousSymptomsRepository.insertGeminiMedicinskiIzvestaj(geminiResponse2,geminiResponseRetrofit,pacijent, repo)
                        geminiProMysteriousSymptomsRepository.insertGeminiIzjavaZaPacijenta(geminiResponse2, geminiResponseRetrofit, pacijent,zl, repo)
                        geminiProMysteriousSymptomsRepository.insertGeminiLekarskiTest(geminiResponse2,geminiResponseRetrofit,pacijent, repo)

                        // dokazi
                        dokaziLista = geminiProRepository.insertGeminiDokaz(geminiResponse2,geminiResponseRetrofit,zl,pacijent.zrtvaId, repo)

                        //forenzicki dokazi
                        forenzickiDokaziLista = geminiProRepository.insertGeminiForenzickiDokaz(geminiResponse2,geminiResponseRetrofit,pacijent.zrtvaId, repo)

                        //telefon
                        telefoniLista = geminiProRepository.insertGeminiTelefon(geminiResponse2,geminiResponseRetrofit,pacijent.zrtvaId, repo)

                        //aplikacija
                        geminiProRepository.insertGeminiAplikacija(geminiResponse2,geminiResponseRetrofit,pacijent.zrtvaId, repo)
                    }

                    geminiProMysteriousSymptomsRepository.insertGeminiLokacijeIstrage(geminiResponse2,geminiResponseRetrofit,zl, repo)

                    //osoba
                    geminiProRepository.insertGeminiOsoba(geminiResponse2, geminiResponseRetrofit, zl, timestamp, repo)

                    // beleske
                    geminiProRepository.insertGeminiBeleska(geminiResponse2, geminiResponseRetrofit, zl, timestamp, repo)

                    // whatsAppKontakt
                    val whatsAppKontaktiLista = geminiProRepository.insertGeminiWhatsAppKontakt(geminiResponse2, geminiResponseRetrofit,zl, repo)

                    //  whatsAppPoruka
                    geminiProRepository.insertGeminiWhatsAppPoruka(geminiResponse2, geminiResponseRetrofit, whatsAppKontaktiLista, timestamp, repo)

                    // kontakti - One Contact
                    val kontaktiLista = geminiProRepository.insertGeminiOneContact(geminiResponse2, geminiResponseRetrofit, zl, repo)

                    // one call
                    geminiProRepository.insertGeminiOneCall(geminiResponse2, geminiResponseRetrofit, zrtva, kontaktiLista, timestamp, repo)

                    // gallery
                    geminiProRepository.insertGeminiGallery(geminiResponse2, geminiResponseRetrofit,zl, timestamp, repo)

                    //  obicnaPoruka
                    geminiProRepository.insertGeminiObicnaPoruka(geminiResponse2,geminiResponseRetrofit, kontaktiLista, timestamp, repo)

                    // pitanje
                    val pitanjaLista = geminiProRepository.insertGeminiPitanje(geminiResponse2, geminiResponseRetrofit,zl, repo)

                    //odgovor
                    geminiProRepository.insertGeminiOdgovor(geminiResponse2, geminiResponseRetrofit,pitanjaLista, repo)

                    // zadatak

                    val zadaciLista = geminiProMysteriousSymptomsRepository.insertGeminiZadatakPacijent(geminiResponse2, zl, repo)
                    geminiProMysteriousSymptomsRepository.updateGeminiZadatakListPacijent(geminiResponse2,geminiResponseRetrofit, zl, repo)
                    geminiResponseRetrofit.zadaciRetrofit = zadaciLista

                    // dokazZadatak
                    geminiProRepository.insertGeminiDokazZadatak(geminiResponse2, geminiResponseRetrofit,dokaziLista, zadaciLista, repo)

                    // telefonZadatak
                    geminiProRepository.insertGeminiTelefonZadatak(geminiResponse2, geminiResponseRetrofit,telefoniLista, zadaciLista, repo)

                    // forenzickiDokazZadatak
                    geminiProRepository.insertGeminiForenzickiDokazZadatak(geminiResponse2, geminiResponseRetrofit,forenzickiDokaziLista, zadaciLista, repo)

                }
            }
        }
        return geminiResponseRetrofit
    }
}