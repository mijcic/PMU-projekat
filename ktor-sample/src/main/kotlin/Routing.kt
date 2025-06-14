package com.example

import com.example.data.remote.GEMINI_API_KEY
import com.example.data.remote.geminiClient
import com.example.models.dto.KorisnikRequest
import com.example.models.dto.MessageResponse
import com.example.models.dto.ScoreKorisnik
import com.example.parser.DefaultGeminiResponseParser
import com.example.service.post.GeminiService
import com.example.service.post.GeminiServiceImpl
import com.example.repository.Repository
import com.example.repository.RepositoryInsert
import com.example.service.get.GeminiMurderService
import com.example.service.get.GeminiMysteriousSymptomsService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.sql.*

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
    val connection = getDatabaseConnection()
        ?: error("Database connection failed — cannot start routing.")
    val repository: Repository = Repository(connection)
    val geminiService: GeminiService = GeminiServiceImpl(geminiClient, GEMINI_API_KEY, DefaultGeminiResponseParser())

    if (isMurderTableEmpty(connection)) {
        println("yes if")
        launch {
            insertInitialMurder(connection, geminiService)
        }
    }else{
        println("no if")
    }

    if (connection != null && isMysteriousSymptomsTableEmpty(connection)) {
        //insertInitialKorisnici(conn)
    }

    routing {
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

                val geminiResponseText = queryGeminiMysteriousSymptoms(prompt, tables.toString())
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
            val korisnici =fetchScoreKorisnici()
            call.respond(korisnici)
        }

        //post request

        post("/signUp"){
            try {
                val conn = getDatabaseConnection()
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
                val conn = getDatabaseConnection()
                val repo = conn?.let { RepositoryInsert(it) }
                println("logIn")
                val korisnik = call.receive<KorisnikRequest>()
                val result = repo?.logIn(korisnik)
                if (result == true) {
                    call.respond(MessageResponse("TRUE"))
                }
                else {
                    call.respond(MessageResponse("FALSE"))
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, MessageResponse("Failed to log in Korisnik"))
            }
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

fun <T> executeQuery(query: String, rowMapper: (ResultSet) -> T): List<T> {
    val resultList = mutableListOf<T>()
    var conn: Connection? = null
    var statement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        conn = getDatabaseConnection()
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
}

fun fetchScoreKorisnici(): List<ScoreKorisnik> {
    val query = "SELECT * FROM korisnik ORDER BY poeni DESC LIMIT 5"
    val rawList = mutableListOf<ScoreKorisnik>()

    executeQuery(query) { resultSet ->
        val korisnickoIme = resultSet.getString("korisnickoIme")
        val poeni = resultSet.getInt("poeni")
        rawList.add(ScoreKorisnik(0,korisnickoIme, poeni))
    }

    return rawList.mapIndexed { index, korisnik ->
        korisnik.copy(mesto = index + 1)
    }
}

fun isMurderTableEmpty(connection: Connection): Boolean {
    println("isMurderTableEmpty")
    val query = "SELECT COUNT(*) FROM zlocin WHERE tipZlocinaId=1"
    connection.prepareStatement(query).use { statement ->
        statement.executeQuery().use { rs ->
            return if (rs.next()) rs.getInt(1) == 0 else true
        }
    }
}

fun isMysteriousSymptomsTableEmpty(connection: Connection): Boolean {
    val query = "SELECT COUNT(*) FROM zlocin WHERE tipZlocinaId=9"
    connection.prepareStatement(query).use { statement ->
        statement.executeQuery().use { rs ->
            return if (rs.next()) rs.getInt(1) == 0 else true
        }
    }
}


fun getJsonMurder(): String {
    val jsonString = """
        {
          "prompt": "Smisli priču za detektivsku aplikaciju o ubistvu. Popuni sve podatke u tabelama kao u primeru koji dajem ispod, ali ne zelim da mi prica i podaci budu isti vec generisi neku novu pricu o ubistvu i na osnovu toga popuni tabele. Tip osumnjicenog moze biti samo pojedinac ili organizacija. Tip dokaza moze biti fizicki, digitalni ili svedok. statusSvedok moze biti 'aktivno', 'zasticen', 'nesaradnja'.  tipForenzickiDokaz moze biti 'otisak', 'DNK', 'dokument'.  os moze biti 'IOS' ili 'Android'. Mora da postoji samo jedan zlocinR, nemoj da mi pravis listu. Koristi sledeće tabele za popunjavanje podataka. Popuni mi sve tabele koje ti prosledim kao primer. Popuni mi i primere za tabelu zadatakR sa njenim poljima idZadatak, tekst, korak koji je tipa String, uradjen, next, zlocinId. Popuni mi i tabelu telefonZadatakR i obicnaPorukaR. Obavezno dodaj i jedan whatsAppKontaktR zrtve cije ce ime biti 'Me' i sa njim se obavlja komunikacija sa drugim objektima tipa whatsAppKontaktR. Obavezno dodaj i jedan oneContactR zrtve cije ce ime biti 'Me' i sa njim se obavlja komunikacija sa drugim objektima tipa oneContactR. Zelim da mi dodas vise od jednog objekta tipa whatsAppKontaktR. Popuni mi i tabelu whatsAppPorukaR. OBAVEZNO mi popuni i tabelu obicnaPorukaR. OBAVEZNO mi popuni i tabelu oneCallR. Nemoj da vracas null vrednosti za polja. Zlocin tabela  je jedna nije lista. Popuni tabele DokazZadatak sa odgovarajucim dokazom i zadatkom. Sve tabele mi popuni sa podacima, bas sve! Pitanja za ispitivanje svedoka i osumnjicenih ne smeju biti prazna! Tabele dokazR treaba da ima vise od 5 objekata, svedokR vise od 2, forenzickiDokazR vise od 2, pitanjeR vise od 5, odgovorR vise od 5, pitanjeIspitivanjeOsumnjicenogR vise od 5, pitanjeIspitivanjeSvedokaR vise od 5, zadatakR vise od 5, ispitivanjeSvedokaZadatakR vise od 5...  Ali odgovor napisi samo u json obliku i ne ubacuj dodatne [].",
          "tables": {
          "zlocinR": {
            "idZlocin": 1,
            "tipZlocinaId": 1,
            "naziv": "Murder of Isabelle Moreau",
            "datum": "2025-04-17",
            "mesto": "Casino Hotel, Paris",
            "opis": "Isabelle Moreau, a high-profile gambler, was found dead in her hotel room with a knife wound. The investigation is ongoing.",
            "status": "u_istrazi"
          },
          "zrtvaR": {
            "idZrtva": 1,
            "tipZrtve": "Individual",
            "detalji": "Isabelle Moreau, a 32-year-old gambler known for her luxurious lifestyle and turbulent relationships, was found murdered in her hotel room.",
            "statusZrtva": "ziva",
            "zlocinId": 1,
            "osobaId": {
              "idOsoba": 1,
              "ime": "Isabelle Moreau",
              "kontakt": "+33612345678",
              "datum": "1993-04-12",
              "zanimanje": "Gambler",
              "pol": "zenski",
              "zlocinId": 1
            }
          },
          "osumnjicenR": [
            {
              "idOsumnjicen": 1,
              "status": 0,
              "tipOsumnjicen": "Pojedinac",
              "motiv": {
                "idMotiv": 1,
                "opis": "Financial struggles and jealousy."
              },
              "zlocinId": 1,
              "kriv": 0,
              "osobaId": {
                "idOsoba": 2,
                "ime": "Amelia Fontaine",
                "kontakt": "+33623456789",
                "datum": "1990-06-14",
                "zanimanje": "Casino Dealer",
                "pol": "zenski",
                "zlocinId": 1
              }
            },
            {
              "idOsumnjicen": 2,
              "status": 0,
              "tipOsumnjicen": "Pojedinac",
              "motiv": {
                "idMotiv": 2,
                "opis": "Financial problems linked to Isabelle's gambling habits."
              },
              "zlocinId": 1,
              "kriv": 0,
              "osobaId": {
                "idOsoba": 3,
                "ime": "Marco Bellini",
                "kontakt": "+33698765432",
                "datum": "1985-02-21",
                "zanimanje": "Gambler",
                "pol": "muski",
                "zlocinId": 1
              }
            }
          ],
          "dokazR": [
            {
              "idDokaz": 1,
              "tipDokaza": "fizicki",
              "opis": "A knife with blood traces found near the victim's room.",
              "zlocinId": 1,
              "zrtvaId": 1,
              "status": 0
            },
            {
              "idDokaz": 2,
              "tipDokaza": "digitalni",
              "opis": "Threatening messages found on Isabelle's phone.",
              "zlocinId": 1,
              "zrtvaId": 1,
              "status": 0
            }
          ],
          "svedokR": [
            {
              "idSvedok": 1,
              "izjava": "Amelia Fontaine was seen leaving Isabelle's room shortly before the body was discovered. She seemed anxious.",
              "statusSvedok": "aktivno",
              "statusIspitan": 0,
              "zlocinId": 1,
              "osobaId": {
                "idOsoba": 4,
                "ime": "Luc Moreau",
                "kontakt": "+33622334455",
                "datum": "1989-08-05",
                "zanimanje": "Hotel Staff",
                "pol": "muski",
                "zlocinId": 1
              }
            },
            {
              "idSvedok": 2,
              "izjava": "I overheard a heated argument between Isabelle and Marco, but I couldn't understand what was being said.",
              "statusSvedok": "aktivno",
              "statusIspitan": 0,
              "zlocinId": 1,
              "osobaId": {
                "idOsoba": 5,
                "ime": "Vincent Duval",
                "kontakt": "+33644455566",
                "datum": "1987-12-01",
                "zanimanje": "Casino Manager",
                "pol": "muski",
                "zlocinId": 1
              }
            }
          ],
          "obdukcijaR": {
            "idObdukcija": 1,
            "izvestaj": "The victim died from a single stab wound to the chest. There was also evidence of struggle before her death.",
            "datum": "2025-04-17",
            "uzrokSmrti": "Stab wound to the chest.",
            "zrtvaId": 1,
            "informacije": "No signs of sexual assault. The victim's hands showed defensive wounds."
          },
          "forenzickiDokazR": [
            {
              "idForenzickiDokaz": 1,
              "tipForenzickiDokaz": "DNK",
              "opis": "DNA traces found on the knife match those of Amelia Fontaine.",
              "statusS": 0,
              "veza": "The evidence strongly links Amelia Fontaine to the murder.",
              "zrtvaId": 1
            }
          ],
          "telefonR": [
            {
              "idTelefon": 1,
              "model": "iPhone 12",
              "os": "IOS",
              "sifra": "123456",
              "informacije": "The phone showed messages between the victim and the suspects. Some were threatening in nature.",
              "zrtvaId": 1
            },
            {
              "idTelefon": 2,
              "model": "Samsung Galaxy S20",
              "os": "Android",
              "sifra": "654321",
              "informacije": "The phone had records of Marco Bellini's calls with Isabelle the day before her death.",
              "zrtvaId": 1
            }
          ],
          "oneContactR": [
            {
              "idOneContact": 1,
              "zlocinId": 1,
              "ime": "Marco Bellini",
              "broj": "+33698765432",
              "slika": 1
            },
            {
              "idOneContact": 2,
              "zlocinId": 1,
              "ime": "Amelia Fontaine",
              "broj": "+33623456789",
              "slika": 1
            }
          ],
          "beleskaR": [
            {
              "idBeleska": 1,
              "zlocinId": 1,
              "tekst": "Witnesses reported seeing Amelia Fontaine near the scene of the crime.",
              "datum": "2025-04-17"
            },
            {
              "idBeleska": 2,
              "zlocinId": 1,
              "tekst": "Security footage showed Marco Bellini near Isabelle's room earlier that evening.",
              "datum": "2025-04-17"
            }
          ],
          "whatsAppKontaktR": [
          {
            "idWhatsAppKontakt": 1,
            "zlocinId": 1,
            "ime": "Oliver Chase",
            "broj": "+12065559900",
            "slika": 1
          },
          {
            "idWhatsAppKontakt": 2,
            "zlocinId": 1,
            "ime": "Sophia Blake",
            "broj": "+12067771122",
            "slika": 1
          }],
          "whatsAppPorukaR": [
              {
                "idWhatsAppPoruka": 1,
                "kontaktKoSalje": 1,
                "kontaktKomeSalje": 2,
                "tekst": "Nathan was getting too close. We had to act.",
                "datum": "2025-04-17",
                "procitana": true
              },
              {
                "idWhatsAppPoruka": 2,
                "kontaktKoSalje": 2,
                "kontaktKomeSalje": 1,
                "tekst": "I hope nobody traces this back to us.",
                "datum": "2025-04-17",
                "procitana": false
              }
            ],
            "oneCallR": [
            {
              "idOneCall": 1,
              "kontakt": 1,
              "datum": "2025-04-17",
              "propusten": false,
              "dolazni": true,
              "zrtvaId": 1
            },
            {
              "idOneCall": 2,
              "kontakt": 2,
              "datum": "2025-04-17",
              "propusten": true,
              "dolazni": false,
              "zrtvaId": 1
            }
          ],
          "galleryR": [
          {
            "idPhoto": 1,
            "zlocinId": 1,
            "slika": 1,
            "datum": "2025-04-17",
            "mesto": "Casino Hotel, Paris"
          },
          {
            "idPhoto": 2,
            "zlocinId": 1,
            "slika": 2,
            "datum": "2025-04-17",
            "mesto": "Casino Hotel Lobby, Paris"
          }
        ],
        "obicnaPorukaR": [
            {
              "idObicnaPoruka": 1,
              "kontaktKoSalje": 1,
              "kontaktKomeSalje": 2,
              "tekst": "Videli su me u hotelu. Sta da radim?",
              "datum": "2025-04-17",
              "procitana": true
            },
            {
              "idObicnaPoruka": 2,
              "kontaktKoSalje": 2,
              "kontaktKomeSalje": 1,
              "tekst": "Samo se pravi da ništa ne znaš. Sve će biti u redu.",
              "datum": "2025-04-17",
              "procitana": false
            }
          ],
          "prijavljeniKorisnikR": [
          {
            "idKorisnik": 1,
            "korisnickoIme": "detektiv.paris",
            "sifra": "securePassword123"
          },
          {
            "idKorisnik": 2,
            "korisnickoIme": "inspektor.moreau",
            "sifra": "investigate456"
          },
          {
            "idKorisnik": 3,
            "korisnickoIme": "analiticar.bellini",
            "sifra": "analyze789"
          }
        ],
        "pitanjeR": [
          {
            "idPitanje": 1,
            "zlocinId": 1,
            "tekst": "Ko je poslednji put viđen sa Isabelle Moreau pre njene smrti?"
          },
          {
            "idPitanje": 2,
            "zlocinId": 1,
            "tekst": "Da li su pronađeni tragovi borbe u hotelskoj sobi?"
          },
          {
            "idPitanje": 3,
            "zlocinId": 1,
            "tekst": "Koji su motivi osumnjičenih Amelije Fontaine i Marca Bellinija?"
          }
        ],
        "odnosOsumnjicenZrtvaR": [
          {
            "idOdnos": 1,
            "osumnjicenId": 1,
            "zrtvaId": 1,
            "tipOdnosa": "koleginice sa posla"
          },
          {
            "idOdnos": 2,
            "osumnjicenId": 2,
            "zrtvaId": 1,
            "tipOdnosa": "kockarski rivali"
          }
        ],
        "odgovorR": [
          {
            "idOdogovor": 1,
            "pitanjeId": 1,
            "tekstOdgovora": "Amelia Fontaine je bila viđena kako izlazi iz sobe žrtve.",
            "tacan": true,
            "bodovi": 10
          },
          {
            "idOdogovor": 2,
            "pitanjeId": 1,
            "tekstOdgovora": "Marco Bellini je bio na drugom kraju grada.",
            "tacan": false,
            "bodovi": 0
          },
          {
            "idOdogovor": 3,
            "pitanjeId": 1,
            "tekstOdgovora": "Niko nije viđen u blizini sobe žrtve.",
            "tacan": false,
            "bodovi": 0
          }
        ],
        "pitanjeIspitivanjeOsumnjicenogR": [
          {
            "idPitanjeIspitivanjeOsumnjicenog": 1,
            "kategorija": "Alibi",
            "tekst": "Gde ste bili u noći kada je Nathan Clarke ubijen?",
            "odgovor": "Bio sam kod kuće, sam, gledajući TV.",
            "komentar": "Nema potvrde alibija od treće strane.",
            "osumnjicenId": 2
          },
          {
            "idPitanjeIspitivanjeOsumnjicenog": 2,
            "kategorija": "Motiv",
            "tekst": "Da li ste imali neki razlog da naudite Nathanu?",
            "odgovor": "Ne, nismo imali nikakve probleme.",
            "komentar": "Svedoci tvrde da su imali žestoku raspravu nedelju dana ranije.",
            "osumnjicenId": 2
          },
          {
            "idPitanjeIspitivanjeOsumnjicenog": 3,
            "kategorija": "Pristup mestu zločina",
            "tekst": "Da li imate ključ ili način da uđete u Nathanuov stan?",
            "odgovor": "Ne, nikada nisam imao ključ.",
            "komentar": "Forenzičari nisu pronašli tragove provale.",
            "osumnjicenId": 1
          }],
          "pitanjeIspitivanjeSvedokaR": [
            {
              "idPitanjeIspitivanjeSvedoka": 1,
              "tekst": "Gde ste bili u trenutku kada je zločin izveden?",
              "odgovor": "Bio sam kod kuće.",
              "svedokId": 2,
              "nextPitanje": 3
            },
            {
              "idPitanjeIspitivanjeSvedoka": 2,
              "tekst": "Da li ste ikada imali konflikata sa osumnjičenim?",
              "odgovor": "Ne, nikada.",
              "svedokId": 2,
              "nextPitanje": 0
            },
            {
              "idPitanjeIspitivanjeSvedoka": 3,
              "tekst": "Da li možete potvrditi alibi osumnjičenog?",
              "odgovor": "Da, bio je sa mnom.",
              "svedokId": 3,
              "nextPitanje": 0
            }
          ],
          "osobaR": [
            {
              "idOsoba": 1,
              "ime": "Marko Marković",
              "kontakt": "123456789",
              "datum": "2025-04-17",
              "zanimanje": "Detektiv",
              "pol": "Muški",
              "zlocinId": 101
            },
            {
              "idOsoba": 2,
              "ime": "Jovana Jovanović",
              "kontakt": "987654321",
              "datum": "2025-04-17",
              "zanimanje": "Advokat",
              "pol": "Ženski",
              "zlocinId": 102
            },
            {
              "idOsoba": 3,
              "ime": "Nikola Nikolić",
              "kontakt": "1122334455",
              "datum": "2025-04-17",
              "zanimanje": "Novinar",
              "pol": "Muški",
              "zlocinId": 103
            }
          ],
          "zadatakR": [
          {
            "idZadatak": 1,
            "tekst": "Ispitati mesto zločina",
            "korak": "1",
            "uradjen": false,
            "nextZadatak": 2,
            "zlocinId": 101
          },
          {
            "idZadatak": 2,
            "tekst": "Pronaći svedoke",
            "korak": "2",
            "uradjen": false,
            "nextZadatak": 3,
            "zlocinId": 101
          }
        ],
        "ispitivanjeSvedokaZadatakR":[
          {
            "idIspitivanjeSvedokaZadatak": 1,
            "svedokId": 101,
            "zadatakId": 1001,
            "uradjen": false
          },
          {
            "idIspitivanjeSvedokaZadatak": 2,
            "svedokId": 102,
            "zadatakId": 1002,
            "uradjen": true
          },
          {
            "idIspitivanjeSvedokaZadatak": 3,
            "svedokId": 103,
            "zadatakId": 1003,
            "uradjen": false
          }
        ],
        "dokazZadatakR": [
          {
            "idDokazZadatak": 1,
            "tekst": "Analiziraj DNK tragove pronađene na nožu.",
            "dokazId": 1,
            "uradjen": false,
            "zadatakId": 2
          },
          {
            "idDokazZadatak": 2,
            "tekst": "Uporedi otiske prstiju sa čaše sa bazom osumnjičenih.",
            "dokazId": 2,
            "uradjen": false,
            "zadatakId": 3
          }],
          "ispitivanjeOsumnjicenogZadatakR":[
          {
            "idIspitivanjeOsumnjicenogZadatak": 1,
            "osumnjicenId": 42,
            "zadatakId": 7,
            "uradjen": false
          },
          {
            "idIspitivanjeOsumnjicenogZadatak": 2,
            "osumnjicenId": 43,
            "zadatakId": 8,
            "uradjen": true
          },
          {
            "idIspitivanjeOsumnjicenogZadatak": 3,
            "osumnjicenId": 42,
            "zadatakId": 9,
            "uradjen": false
          }
        ],
        "telefonZadatakR": [
              {
                "idTelefonZadatak": 1,
                "telefonId": 10,
                "zadatakId": 3,
                "uradjen": false
              },
              {
                "idTelefonZadatak": 2,
                "telefonId": 11,
                "zadatakId": 4,
                "uradjen": true
              }
        ],
        "forenzickiDokazZadatakR": [
          {
            "idForenzickiDokazZadatak": 1,
            "tekst": "Uporedi DNK tragove sa uzorcima osumnjičenih.",
            "forenzickiDokazId": 1,
            "uradjen": false,
            "zadatakId": 1
          },
          {
            "idForenzickiDokazZadatak": 2,
            "tekst": "Proveri da li postoji još tragova DNK na dršci noža.",
            "forenzickiDokazId": 1,
            "uradjen": false,
            "zadatakId": 2
          }
        ],
            "kontaktKtor":[{
                "idKontakt":0,
                "ime":"",
                "broj":"",
                "status":0,
                "zrtvaId":0
            }],
            "porukeKtor":[{
                "idPoruke":0,
                "tipPoruke":"",
                "sadrzaj":"",
                "datumVreme":"2023-11-11 8:30AM",
                "zrtvaId":0,
                "posiljalacId":0,
                "statusPoruke":"",
                "sirovana":false
            }],
            "poziviKtor":[{
                "idPoziv":0,
                "tip":0,
                "broj":"",
                "datumVreme":"2023-11-11 9:30AM",
                "zrtvaId":0,
                "status":0,
                "kontaktId":0
            }],
            "galerijaKtor":[{
                "idGalerija":0,
                "tip":0,
                "putanja":"",
                "zrtvaId":0,
                "datumVreme":"2023-11-11 9:30AM",
                "lokacija":""
            }],
            "aplikacijaKtor":[{
                "idAplikacije":0,
                "naziv": "",
                "tip": 0,
                "zrtvaId": 0,
                "aktivna": false,
                "informacije": ""
            }],
            "tragKtor":[{
                "idTrag":0,
                "forenzickiDokazId":0,
                "osumnjicenId":0
            }],
            "dokazOsumnjicenKtor":[{
                "idDokazOsumnjicen":0,
                "dokazId":0,
                "osumnjicenId":0
            }]
        }
        }
        
        """.trimIndent()

    return jsonString
}


suspend fun insertInitialMurder(connection: Connection, geminiService: GeminiService) {

    val jsonString = getJsonMurder()
    val json = JSONObject(jsonString)

    val prompt = json.getString("prompt")
    val tables = json.getJSONObject("tables").toString()

    val result = geminiService.generateContent(prompt, tables)
    println("Rezultat: $result")
}
