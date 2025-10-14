package com.example

import com.example.data.remote.client.GEMINI_API_KEY
import com.example.data.remote.client.GeminiClient
import com.example.models.domain.Story
import com.example.data.remote.gemini.request.GeminiRequest2
import com.example.data.remote.gemini.request.GeminiRequest2MysteriousSymptoms
import com.example.data.remote.tables.ScoreKorisnikaRequest
import com.example.data.remote.gemini.retrofit.GeminiResponse2
import com.example.data.remote.gemini.retrofit.GeminiResponse2MysteriousSymptoms
import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofit
import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofitMysteriousSymptoms
import com.example.data.remote.tables.*
import com.example.parser.DefaultGeminiResponseParser
import com.example.repository.GeminiProMysteriousSymptomsRepositoryImpl
import com.example.repository.GeminiProRepositoryImpl
import com.example.service.post.GeminiService
import com.example.service.post.GeminiServiceImpl
import com.example.repository.Repository
import com.example.repository.RepositoryInsert
import com.example.service.DatabaseService
import com.example.service.InitialDataService
import com.example.service.KorisnikService
import com.example.service.get.GeminiMurderService
import com.example.service.get.GeminiMysteriousSymptomsService
import com.example.utils.JsonLoader
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.sql.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import com.google.gson.Gson

/**
 * Configures the routing and endpoints for the Ktor application.
 *
 * - Establishes a connection to the database.
 * - Initializes the [Repository] and [GeminiService].
 * - Populates initial data in the "murder" table if it is empty.
 * - Sets up REST API endpoints for handling Gemini AI requests, user registration, login, and data retrieval.
 *
 * ## Endpoints
 * - `POST /gemini` — Generates Gemini content based on prompt and tables.
 * - `POST /geminiMS` — Processes prompts for "Mysterious Symptoms" via Gemini AI.
 * - `GET /geminiMurder` — Returns AI-generated murder data.
 * - `GET /geminiMysteriousSymptoms` — Returns AI-generated mysterious symptoms data.
 * - `GET /scoreKorisnika` — Returns user scores.
 * - `POST /signUp` — Registers a new user.
 * - `POST /logIn` — Authenticates an existing user.
 * - `GET /` — Returns a simple "Hello World!" response.
 * - `staticResources("/static")` — Serves static files.
 *
 * @receiver The Ktor [Application] instance.
 *
 * @throws IllegalStateException if the database connection cannot be established.
 */
fun Application.configureRouting() {

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Delete)
    }


    val databaseService = DatabaseService(
        dbUrl = "jdbc:mysql://localhost:3306/whodunit?useSSL=false&allowPublicKeyRetrieval=true",
        user = "root",
        password = "1234"
        //password = "mia123"
    )
    val connection = databaseService.getDatabaseConnection() ?: error("Database connection failed — cannot start routing.")
    val repository: Repository = Repository(connection)
    val geminiService: GeminiService = GeminiServiceImpl(GeminiClient.geminiClient, GEMINI_API_KEY, DefaultGeminiResponseParser())
    val initialDataService = InitialDataService(geminiService, databaseService)
    val korisnikService = KorisnikService(databaseService)

    //launch {
      //  initialDataService.insertInitialMurderIfEmpty()
    //}
    launch {
        delay(2000)
        //initialDataService.insertInitialMysteriousSymptomsIfEmpty()
    }

    routing {

        post("/admin/addJson"){
            val conn = getDatabaseConnection()

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

            if(conn == null){
                print("CONN JE NULL")
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Nevalidan JSON format")
                )

            }
            val repo = RepositoryInsert(conn!!)

            try {

                var jsonText = call.receiveText()
                jsonText = jsonText.replace("ΓÇÖ", "'")
                    .replace("─ç", "ć")
                    .replace("┼í", "i")

                // 2. Proveri da li je validan JSON
                val jsonElement = Json.parseToJsonElement(jsonText)

                val json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
                val geminiResponse2 = json.decodeFromString<GeminiResponse2>(jsonText)
                val geminiProRepo = GeminiProRepositoryImpl()
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
                        println("Greška pri parsiranju datuma: ${ex.message}")
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
                        println("JSON VALIDAN")
                        println(zl)
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

                        // ostali kontakti

                        //  whatsAppPoruka

                        geminiProRepo.insertGeminiWhatsAppPoruka(geminiResponse2, geminiResponseRetrofit, whatsAppKontaktiLista, timestamp, repo)
                        // one call
                        sviDokaziZrtva.zrtva?.let { it1 ->
                            geminiProRepo.insertGeminiOneCall(geminiResponse2, geminiResponseRetrofit,
                                it1, kontaktiLista, timestamp, repo)
                        }

                        // gallery
                        geminiProRepo.insertGeminiGallery(geminiResponse2, geminiResponseRetrofit,zl, timestamp,repo)

                        //  obicnaPoruka
                        geminiProRepo.insertGeminiObicnaPoruka(geminiResponse2,geminiResponseRetrofit, kontaktiLista, timestamp,repo)

                        val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
                        /*

                        scope.launch {
                            geminiProRepo.suspendInsertKontakti(whatsAppKontaktiLista,geminiResponse2,geminiResponseRetrofit,timestamp,kontaktiLista,zl,zrtva, repo)
                        }*/

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

                        // odgovor
                        val odgovoriDeferred = async(Dispatchers.IO) {
                            geminiProRepo.insertGeminiOdgovor(geminiResponse2, geminiResponseRetrofit,pitanjaLista, repo)
                        }
                        // pitanjeIspitivanjeOsumnjicenog
                        val pitanjeIspitivanjeOsumnjicenogDeferred = async(Dispatchers.IO) {
                            geminiProRepo.insertGeminiPitanjeIspitivanjeOsumnjicenog(geminiResponse2, geminiResponseRetrofit,osumnjiceniLista, repo)
                        }
                        // pitanjeIspitivanjeSvedoka
                        val pitanjeIspitivanjeSvedokaDeferred = async(Dispatchers.IO) {
                            geminiProRepo.insertGeminiPitanjeIspitivanjeSvedoka(geminiResponse2, geminiResponseRetrofit,svedociLista, repo)
                        }
                        //  osoba
                        val osobaDeferred = async(Dispatchers.IO) {
                            geminiProRepo.insertGeminiOsoba(geminiResponse2, geminiResponseRetrofit, zl, timestamp, repo)
                        }

                        /*
                        scope.launch {
                            geminiProRepo.suspendInsertPitanja(pitanjaLista,geminiResponse2,geminiResponseRetrofit,timestamp,osumnjiceniLista,svedociLista,zl, repo)
                        }*/

                        // zadatak

                        println("DOSLI DO ZADATAKA ")
                        val zadaciLista = geminiProRepo.insertGeminiZadatak(geminiResponse2, zl,repo)

                        println("DOSLI DO ZADATAKA ")
                        geminiProRepo.updateGeminiZadatakList(geminiResponse2,geminiResponseRetrofit, zl,repo)

                        println("DOSLI DO ZADATAKA ")
                        geminiResponseRetrofit.zadaciRetrofit = zadaciLista

                        println("DOSLI DO ZADATAKA ")
                       // scope.launch {
                        geminiProRepo.suspendInsertZadaci(zadaciLista, geminiResponse2, geminiResponseRetrofit, osumnjiceniLista, svedociLista, sviDokaziZrtva, repo)
                        //}

                        println("DOSLI DO ZADATAKA ")

                        // porukeZadatak



                        scope.launch {
                            geminiProRepo.insertGeminiIspitivanjeSvedokaZadatak(geminiResponse2, geminiResponseRetrofit, svedociLista,zadaciLista, repo)
                        }

                        scope.launch {
                            geminiProRepo.insertGeminiIspitivanjeOsumnjicenogZadatak(geminiResponse2, geminiResponseRetrofit, osumnjiceniLista,zadaciLista, repo)
                        }
                    }
                }

                // 4. Vrati potvrdu
                call.respond(HttpStatusCode.OK, mapOf("ok" to "Validc JSON format"))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Nevalidan JSON format")
                )

            }
        }


        post("/admin/addMSJson"){
            val conn = getDatabaseConnection()
            val geminiProMysteriousSymptomsRepository = GeminiProMysteriousSymptomsRepositoryImpl()
            val geminiProRepository = GeminiProRepositoryImpl()

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

            if(conn == null){
                print("CONN JE NULL")
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Nevalidan JSON format")
                )

            }
            val repo = RepositoryInsert(conn!!)

            try {

                var jsonText = call.receiveText()
                jsonText = jsonText.replace("ΓÇÖ", "'")
                    .replace("─ç", "ć")
                    .replace("┼í", "i")

                // 2. Proveri da li je validan JSON
                val jsonElement = Json.parseToJsonElement(jsonText)
                val json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
                val geminiResponse2 = json.decodeFromString<GeminiResponse2MysteriousSymptoms>(jsonText)

                val geminiProRepo = GeminiProRepositoryImpl()
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
                        println("Greška pri parsiranju datuma: ${ex.message}")
                    }
                }

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

                // 4. Vrati potvrdu
                call.respond(HttpStatusCode.OK, mapOf("ok" to "Validc JSON format"))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Nevalidan JSON format")
                )

            }
        }

        get("/admin/getAll") {
            val allStories = repository.getAllZlocin()?: emptyList()
            call.respond(allStories)
        }

        delete("/admin/delete/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Pogrešan ID")
                return@delete
            }

            val success = repository.deleteStoryById(id)

            if (success) {
                call.respond(HttpStatusCode.OK, "Priča obrisana")
            } else {
                call.respond(HttpStatusCode.NotFound, "Priča nije pronađena")
            }
        }

        get("/admin/stats") {
            val zlocini = repository.getAllZlocin() // tvoja funkcija za dohvat svih

            val ukupno = zlocini?.size
            val uIstr = zlocini?.count { it.status == "u_istrazi" }
            val zatvoreni = zlocini?.count { it.status == "resen" }

            //val prosecnoOsumnjicenih = if (ukupno > 0) {
              //  osumnjiceni.groupBy { it.zlocinId }.values.map { it.size }.average()
            //} else 0.0

            val stats = mapOf(
                "ukupnoZlocina" to ukupno,
                "uIstr" to uIstr,
                "zatvoreni" to zatvoreni,
                //"prosecnoOsumnjicenih" to String.format("%.2f", prosecnoOsumnjicenih)
            )

            call.respond(stats)
        }

        get("/admin/allUsers") {
            val users = repository.getAllUsers()
            call.respond(users ?: emptyList())
        }


        post("/admin/gemini") {
            val jsonMurder = JsonLoader.getJsonMurderSteps(1, "", "", "", "")
            val json = JSONObject(jsonMurder)
            val prompt = json.getString("prompt")
            val tables = json.getJSONObject("tables").toString()

            val result = geminiService.generateContentStep1Murder(prompt, tables)

            result.onSuccess { parsedResponse ->
                if (parsedResponse != null) {
                    call.respond(parsedResponse)  // šalje samo konkretan objekat
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Greška: prazni odgovor od Gemini API-ja")
                }
            }

            result.onFailure { e ->
                call.respond(HttpStatusCode.InternalServerError, "Greška: ${e.message}")
            }
        }



        //gemini
        post("/gemini") {
            val requestData = call.receive<GeminiRequest2>()
            val result = geminiService.generateContent(requestData.prompt, requestData.tables.toString())
            println(result)

            result
                .onSuccess { call.respond(it) }
                .onFailure {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to it.message))
                }
        }
        post("/geminiMurderStory") {
            val requestData = call.receive<Story>()
            val jsonMurder = JsonLoader.getJsonMurder()
            val json = JSONObject(jsonMurder)

            val prompt = json.getString("prompt")
            val tables = json.getJSONObject("tables").toString()

            val result = geminiService.generateContent(prompt, tables)
            println("Rezultat: $result")

            result.onSuccess { storyText ->
                call.respond(Story(story = "OKEJ")) }
                .onFailure {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to it.message))
                }
        }

        post("/geminiMurderStorySteps") {
            println("geminiMurderStorySteps")
            /*step1*/
            val jsonMurder = JsonLoader.getJsonMurderSteps(1,"","","","")
            val json = JSONObject(jsonMurder)
            val prompt = json.getString("prompt")
            val tables = json.getJSONObject("tables").toString()
            val result = geminiService.generateContentStep1Murder(prompt,tables)
            //println(result)

            val rawResultString = result.getOrNull()
            if (rawResultString != null) {

            } else {
                println("❌ Neuspešan rezultat: ${result.exceptionOrNull()?.message}")
            }
            println("\n\n")

            /*step2*/
            val jsonMurder2 = JsonLoader.getJsonMurderSteps(2,result.toString(),"","","")
            val json2 = JSONObject(jsonMurder2)
            val prompt2 = json2.getString("prompt")
            val tables2 = json2.getJSONObject("tables").toString()
            val result2 = geminiService.generateContentStep2Murder(prompt2,tables2)
           // println(result2)

            val rawResultString2 = result2.getOrNull()
            if (rawResultString2 != null) {
                val prettyPrinted = rawResultString2
                    .replace(",", ",\n")
                    .replace("(", "(\n")
                    .replace(")", "\n)")
                    .replace("=", ": ")
                    .replace("Success", "✅ Success")

                println("===== Formatirani rezultat 2=====")
                println(prettyPrinted)
            } else {
                println("❌ Neuspešan rezultat: ${result.exceptionOrNull()?.message}")
            }

            /*step3*/

            val jsonMurder3 = JsonLoader.getJsonMurderSteps(3,result.toString(),result2.toString(),"","")
            val json3 = JSONObject(jsonMurder3)
            val prompt3 = json3.getString("prompt")
            val tables3 = json3.getJSONObject("tables").toString()
            val result3 = geminiService.generateContentStep3Murder(prompt3,tables3)
            //println(result3)
            val rawResultString3 = result3.getOrNull()
            if (rawResultString3 != null) {
                val prettyPrinted3 = rawResultString3
                    .replace(",", ",\n")
                    .replace("(", "(\n")
                    .replace(")", "\n)")
                    .replace("=", ": ")
                    .replace("Success", "✅ Success")

                println("===== Formatirani rezultat 3=====")
                println(prettyPrinted3)
            } else {
                println("❌ Neuspešan rezultat: ${result.exceptionOrNull()?.message}")
            }

            /*step4*/
            val jsonMurder4 = JsonLoader.getJsonMurderSteps(4,result.toString(),result2.toString(),result3.toString(),"")
            val json4 = JSONObject(jsonMurder4)
            val prompt4 = json4.getString("prompt")
            val tables4 = json4.getJSONObject("tables").toString()
            val result4 = geminiService.generateContentStep4Murder(prompt4,tables4)
            //println(result3)
            val rawResultString4 = result4.getOrNull()
            if (rawResultString4 != null) {
                val prettyPrinted4 = rawResultString4
                    .replace(",", ",\n")
                    .replace("(", "(\n")
                    .replace(")", "\n)")
                    .replace("=", ": ")
                    .replace("Success", "✅ Success")

                println("===== Formatirani rezultat 4=====")
                println(prettyPrinted4)
            } else {
                println("❌ Neuspešan rezultat: ${result.exceptionOrNull()?.message}")
            }

            /*step5*/
            /*
            val jsonMurder5 = JsonLoader.getJsonMurderSteps(5,result.toString(),result2.toString(),result3.toString(),"")
            val json5 = JSONObject(jsonMurder5)
            val prompt5 = json5.getString("prompt")
            val tables5 = json5.getJSONObject("tables").toString()
            val result5 = geminiService.generateContentStep5Murder(prompt5,tables5)
            //println(result3)
            val rawResultString5 = result5.getOrNull()
            if (rawResultString5 != null) {
                val prettyPrinted5 = rawResultString5
                    .replace(",", ",\n")
                    .replace("(", "(\n")
                    .replace(")", "\n)")
                    .replace("=", ": ")
                    .replace("Success", "✅ Success")

                println("===== Formatirani rezultat 5=====")
                println(prettyPrinted5)
            } else {
                println("❌ Neuspešan rezultat: ${result.exceptionOrNull()?.message}")
            }*/


            call.respond(
                "OK"
            )
        }

        post("/geminiMSStory") {
            val requestData = call.receive<Story>()
            val jsonMS = JsonLoader.getJsonMysteriousSymptoms()
            val json = JSONObject(jsonMS)

            val prompt = json.getString("prompt")
            val tables = json.getJSONObject("tables").toString()

            try {
                val result = geminiService.queryGeminiMysteriousSymptoms(prompt, tables)
                println("Rezultat: $result")
                call.respond(Story(story = result.toString())) // ili "OKEJ" ako testiraš
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        //gemini mysterious symptoms
        post("/geminiMS") {
            try {
                val requestData = call.receive<GeminiRequest2MysteriousSymptoms>()
                val prompt = requestData.prompt
                val tables = requestData.tables

                if (prompt.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "The field 'prompt' is required and cannot be empty."))
                    return@post
                }
                if (tables == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "The field 'tables' is required and cannot be empty."))
                    return@post
                }

                val geminiResponseText = geminiService.queryGeminiMysteriousSymptoms(prompt, tables.toString())
                println(geminiResponseText)

                call.respond(geminiResponseText as Any)

            } catch (e: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid request format. A JSON object with the keys 'prompt' and 'tables' is expected."))
            } catch (e: Exception) {
                println("Unexpected error at the /gemini endpoint: ${e.message}")
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An internal server error occurred."))
            }
        }

        // get request

        get("/Murder"){
            val geminiMurderService: GeminiMurderService = GeminiMurderService(repository)
            val result = geminiMurderService.getGeminiMurderbezupdateUsedZlocinMurder()

            if (result != null) {
                call.respond(result)
            }
            else {
                call.respond(HttpStatusCode.NotFound, "Murder data not found.")
            }
        }

        get("/getFirstMurder"){
            val geminiMurderService: GeminiMurderService = GeminiMurderService(repository)
            val result = geminiMurderService.getGeminiMurder()

            if (result != null) {
                call.respond(result)
            }
            else {
                call.respond(HttpStatusCode.NotFound, "Murder data not found.")
            }
        }

        get("/geminiMurder") {
            val geminiMurderService: GeminiMurderService = GeminiMurderService(repository)
            val result = geminiMurderService.getGeminiMurder()

            if (result != null) {
                call.respond(result)
            }
            else {
                call.respond(HttpStatusCode.NotFound, "Murder data not found.")
            }
        }

        get("/geminiMysteriousSymptoms") {
            val geminiMysteriousSymptomsService: GeminiMysteriousSymptomsService = GeminiMysteriousSymptomsService(repository)
            val result = geminiMysteriousSymptomsService.getGeminiMysteriousSymtoms()

            if (result != null) {
                call.respond(result)
            }
            else {
                call.respond(HttpStatusCode.NotFound, "Mysterious Symptoms data not found.")
            }
        }

        get("/") {
            call.respondText("Hello World!")
        }
        get("/scoreKorisnika"){
            print("scoreKorisnika")
            val korisnici =korisnikService.fetchTopScored()
            call.respond(korisnici)
        }

        //post request

        post("/signUp"){
            try {
                val conn = databaseService.getDatabaseConnection()
                val repo = conn?.let { RepositoryInsert(it) }
                val korisnik = call.receive<KorisnikRequest>()
                val exists = repo?.checkKorisnik(korisnik)

                if (exists == true) {
                    println("User already exists")
                    call.respond(MessageResponse("Korisnik already exists."))
                } else {
                    repo?.signUpKorisnik(korisnik)
                    println("User inserted successfully")
                    call.respond(MessageResponse("Korisnik inserted successfully"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, MessageResponse("Failed to insert Korisnik"))
            }
        }

        post("/logIn"){
            try{
                val conn = databaseService.getDatabaseConnection()
                val repo = conn?.let { RepositoryInsert(it) }
                println("logIn")
                val korisnik = call.receive<KorisnikRequest>()
                val result = repo?.logIn(korisnik)
                if (result == true) {
                    println("User is logged in")
                    call.respond(MessageResponse("TRUE"))
                }
                else {
                    println("User is not logged in")
                    call.respond(MessageResponse("FALSE"))
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, MessageResponse("Failed to log in Korisnik"))
            }
        }

        post("/setScoreKorisnika"){
            try{
                val conn = databaseService.getDatabaseConnection()
                val repo = conn?.let { RepositoryInsert(it) }
                println("setScoreKorisnika")
                val scoreKorisnika = call.receive<ScoreKorisnikaRequest>()
                val result = repo?.insertScoreKorisnika(scoreKorisnika)
                if (result == true) {
                    println("Score is set")
                    call.respond(MessageResponse("TRUE"))
                }
                else {
                    println("Score is not set")
                    call.respond(MessageResponse("FALSE"))
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, MessageResponse("Failed to set score for korisnik"))
            }
        }

        get("/getAllScores") {
            val allScores= repository.getAllScores()?: emptyList()
            call.respond(allScores)
        }

        staticResources("/static", "static")
    }
}

fun getDatabaseConnection(): Connection? {
    return DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/whodunit?useSSL=false&allowPublicKeyRetrieval=true",
        "root",
         "1234"
        //"mia123"
    )
}

fun closeResources(conn: Connection?, statement: PreparedStatement?, resultSet: ResultSet?) {
    resultSet?.close()
    statement?.close()
    //conn?.close()
}
/*
fun <T> executeQuery(query: String, rowMapper: (ResultSet) -> T): List<T> {
    val resultList = mutableListOf<T>()
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        val conn = getDatabaseConnection()
        statement = conn?.prepareStatement(query)
        resultSet = statement?.executeQuery()

        while (resultSet?.next() == true) {
            resultList.add(rowMapper(resultSet))
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        closeResources(conn, statement, resultSet)
    }

    return resultList
}*/