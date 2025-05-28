package com.example

import com.example.data.remote.GEMINI_API_KEY
import com.example.data.remote.geminiClient
import com.example.models.dto.gemini.GeminiResponseRetrofit
import com.example.parser.DefaultGeminiResponseParser
import com.example.service.GeminiService
import com.example.service.GeminiServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*

fun Application.configureRouting() {
    routing {

        val geminiService: GeminiService = GeminiServiceImpl(geminiClient, GEMINI_API_KEY, DefaultGeminiResponseParser())

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

        post("/geminiData") {
            try {
                val requestData = call.receive<GeminiRequest2>()
                println("REQUEST DATA")
                println(requestData)
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

                val geminiResponseText = queryGeminiRetrofit(prompt, tables.toString())

                call.respond(geminiResponseText as Any)

            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid request format. A JSON object with the keys 'prompt' and 'tables' is expected."))
            } catch (e: Exception) {
                println("Unexpected error at the /gemini endpoint: ${e.message}")
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "An internal server error occurred."))
            }
        }



        //gemini mysterious symptoms

        //gemini
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

                //call.respond(mapOf("response" to geminiResponseText))
                call.respond(geminiResponseText as Any)

            } catch (e: ContentTransformationException) {
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
            println("\nGEMINI MURDER\n")
            val id =getUsedZlocinMurder()
            val zl = id?.let { getZlocin(it) }
            val tipZl= zl?.let { getTipZlocina(it.tipZlocinaId) }
            val zrtva = id?.let { getZrtva(it) }
            val osumnjiceni = id?.let { getOsumnjiceni(it) }
            val dokazi = zrtva?.let { getDokazi(id, it) }
            val telefoni = zrtva?.let { getTelefon(it.idZrtva) }
            val forenzickiDokazi = zrtva?.let { getForenzickiDokazi(it.idZrtva) }
            val obdukcija = zrtva?.let { getObdukcija(it.idZrtva) }
            val svedoci = id?.let { getSvedoci(it) }
            val oneContact = id?.let { getOneContact(it) }
            val kontakti = zrtva?.let { getKontakti(id, it) }
            val poruke = zrtva?.let { getPoruke(id, it,kontakti) }
            val pozivi = zrtva?.let { getPozivi(id, it,kontakti) }
            val galerija = zrtva?.let { getGalerija(id, it) }
            val aplikacija = zrtva?.let { getAplikacije(id, it) }
            val tragovi = zrtva?.let { getTragovi(id, it,forenzickiDokazi,osumnjiceni) }
            val dokazOsumnjiceni = zrtva?.let { getDokaziOsumnjiceni(id, it,dokazi,osumnjiceni) }
            val beleske = zrtva?.let { getBeleske(id, it) }
            val whatsAppKontakti = zrtva?.let { getWhatsAppKontakt(id, it) }
            val whatsAppPoruke = id?.let { getWhatsAppPoruka(it,whatsAppKontakti) }
            val gallery = id?.let { getGallery(it) }
            val odnosOsumnjicenZrtva = id?.let { getOdnosOsumnjicenZrtva(it) }
            val pitanja = id?.let { getPitanja(it) }
            val odgovori = id?.let { getOdgovor(it) }
            val pitanjaIspitivanjeOsumnjicenog = id?.let { getPitanjeIspitivanjeOsumnjicenog(it) }
            val pitanjaIspitivanjeSvedoka = id?.let { getPitanjeIspitivanjeSvedoka(it) }
            val osobe = id?.let { getOsobe(it) }
            val zadaci = id?.let { getZadaci(it) }
            val dokazZadatak = id?.let { getDokaziZadaci(it,zadaci) }
            val ispitivanjeOsumnjicenogZadatak = id?.let { getIspitivanjeOsumnjicenogZadatak(it,zadaci) }
            val ispitivanjeSvedokaZadatak = id?.let { getIspitivanjeSvedokaZadatak(it,zadaci) }
            val telefonZadaci = id?.let { getTelefonZadaci(it, zadaci) }
            val forenzickiDokazZadaci = id?.let { getForenzickiDokazZadatak(it,zadaci) }
            val oneCall = id?.let { getOneCall(it,oneContact) }
            val obicnaPoruka = id?.let { getObicnaPoruka(it,oneContact) }


            val geminiResponseRetrofit:GeminiResponseRetrofit= GeminiResponseRetrofit(
                zlocinRetrofit = zl,
                zrtvaRetrofit = zrtva,
                osumnjiceniRetrofit = osumnjiceni,
                dokaziRetrofit =dokazi,
                telefoniRetrofit = telefoni,
                forenzickiDokazRetrofit = forenzickiDokazi,
                obdukcijaRetrofit = obdukcija,
                svedociRetrofit =svedoci,
                oneContactRetrofit = oneContact,
                kontaktiRetrofit = kontakti,
                porukeRetrofit = poruke,
                poziviRetrofit = pozivi,
                galerijaRetrofit = galerija,
                aplikacijeRetrofit = aplikacija,
                tragoviRetrofit = tragovi,
                dokaziOsumnjiceniRetrofit = dokazOsumnjiceni,
                beleskeRetrofit = beleske,
                whatsappKontaktRetrofit = whatsAppKontakti,
                whatsappPorukaRetrofit = whatsAppPoruke,
                oneCallRetrofit = oneCall,
                galleryRetrofit = gallery,
                obicnePorukeRetrofit = obicnaPoruka,
                odnosiOsumnjiceniZrtvaRetrofit = odnosOsumnjicenZrtva,
                pitanjaRetrofit = pitanja,
                odgovoriRetrofit = odgovori,
                pitanjeIspitivanjeOsumnjicenogRetrofit = pitanjaIspitivanjeOsumnjicenog,
                pitanjeIspitivanjeSvedokaRetrofit = pitanjaIspitivanjeSvedoka,
                osobeRetrofit = osobe,
                zadaciRetrofit = zadaci,
                dokaziZadaciRetrofit = dokazZadatak,
                ispitivanjeOsumnjicenogZadaciRetrofit = ispitivanjeOsumnjicenogZadatak,
                ispitivanjeSvedokaZadaciRetrofit = ispitivanjeSvedokaZadatak,
                telefonZadaciRetrofit = telefonZadaci,
                forenzickiDokazZadaciRetrofit = forenzickiDokazZadaci
            )

            println("GEMINI MURDER")
            call.respond(geminiResponseRetrofit)
        }

        get("/geminiMysteriousSymptoms") {
            println("MysteriousSymptoms")
            val id =getUsedZlocinMysteriousSymptoms()
            val zl = id?.let { getZlocin(it) }
            val zrtva = id?.let { getZrtva(it) }
            val dokazi = zrtva?.let { getDokazi(id, it) }
            val telefoni = zrtva?.let { getTelefon(it.idZrtva) }
            val forenzickiDokazi = zrtva?.let { getForenzickiDokazi(it.idZrtva) }
            val oneContact = id?.let { getOneContact(it) }
            val aplikacija = zrtva?.let { getAplikacije(id, it) }
            val beleske = zrtva?.let { getBeleske(id, it) }
            val whatsAppKontakti = zrtva?.let { getWhatsAppKontakt(id, it) }
            val whatsAppPoruke = id?.let { getWhatsAppPoruka(it,whatsAppKontakti) }
            val gallery = id?.let { getGallery(it) }
            val pitanja = id?.let { getPitanja(it) }
            val odgovori = id?.let { getOdgovor(it) }
            val osobe = id?.let { getOsobe(it) }
            val zadaci = id?.let { getZadaci(it) }
            val dokazZadatak = id?.let { getDokaziZadaci(it,zadaci) }
            val telefonZadaci = id?.let { getTelefonZadaci(it, zadaci) }
            val forenzickiDokazZadaci = id?.let { getForenzickiDokazZadatak(it,zadaci) }
            val oneCall = id?.let { getOneCall(it,oneContact) }
            val obicnaPoruka = id?.let { getObicnaPoruka(it,oneContact) }
            var pacijent: PacijentData? = null
            if (zrtva != null && osobe != null && zl!=null) {
                pacijent= getPacijent(id, zl,zrtva,osobe)
            }
            val medicinskiIzvestaj = id?.let { getMedicinskiIzvetaj(it,pacijent) }
            val lekarskiTest = id?.let { getLekarskiTest(it,pacijent) }
            val lokacijeIstrage = id?.let { getLokacijeIstrage(it) }
            val izjavaZaPacijenta = pacijent?.let { getIzjavaZaPacijenta(it,osobe) }

            val geminiResponseRetrofit:GeminiResponseRetrofitMysteriousSymptoms= GeminiResponseRetrofitMysteriousSymptoms(
                zlocinRetrofit = zl,
                dokaziRetrofit = dokazi,
                telefoniRetrofit = telefoni,
                forenzickiDokazRetrofit = forenzickiDokazi,
                oneContactRetrofit = oneContact,
                aplikacijeRetrofit = aplikacija,
                beleskeRetrofit = beleske,
                whatsappKontaktRetrofit = whatsAppKontakti,
                whatsappPorukaRetrofit = whatsAppPoruke,
                oneCallRetrofit = oneCall,
                galleryRetrofit = gallery,
                obicnePorukeRetrofit = obicnaPoruka,
                pitanjaRetrofit = pitanja,
                odgovoriRetrofit = odgovori,
                osobeRetrofit = osobe,
                zadaciRetrofit = zadaci,
                dokaziZadaciRetrofit = dokazZadatak,
                telefonZadaciRetrofit = telefonZadaci,
                forenzickiDokazZadaciRetrofit = forenzickiDokazZadaci,
                pacijentRetrofit = pacijent,
                medicinskiIzvestajRetrofit = medicinskiIzvestaj,
                lekarskiTestRetrofit = lekarskiTest,
                lokacijeIstrageRetrofit = lokacijeIstrage,
                izjavaZaPacijentaRetrofit = izjavaZaPacijenta
            )

            println("\nGEMINI MS\n")
            call.respond(geminiResponseRetrofit)
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

        post("/insertData"){
            try{
                val zlocin = call.receive<ZlocinRequest>()
                insertZlocinData(zlocin.zlocin)
                //insertZrtva(zlocin.zrtva,zlocin.zlocin)
                //insertObdukcijaData(zlocin.obdukcija, zlocin.zrtva)
                //insertTelefonData(zlocin.telefon, zlocin.zrtva)

                for (i in zlocin.motivi.indices) {
                    val motiv = zlocin.motivi[i]
                    val osumnjicen = zlocin.osumnjicen[i]

                    insertMotivData(motiv)
                    //insertOsumnjicenData(osumnjicen, zlocin.zlocin, motiv, zlocin.zrtva)
                }
                for(dokaz in zlocin.dokazi){
                    //insertDokazData(dokaz, zlocin.zlocin, zlocin.zrtva, zlocin.osumnjicen)
                }
                for(svedok in zlocin.svedok){
                    insertSvedokData(svedok, zlocin.zlocin)
                }
                for(alibi in zlocin.alibi){
                    insertAlibiData(alibi,zlocin.zlocin,zlocin.osumnjicen,zlocin.svedok)
                }
                for(forenzickiDokaz in zlocin.forenzickiDokazi){
                   // insertForenzickiDokaz(forenzickiDokaz, zlocin.zrtva)
                }
                for (misijaPoruka in zlocin.misijaPoruka){
                    insertMisijaPorukaData(misijaPoruka, zlocin.zlocin)
                }

                call.respond("Zlocin inserted successfully")
            }
            catch (e: Exception){
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, MessageResponse("Failed to insert Zlocin"))
            }
        }

        post("/signUp"){
            try {
                val korisnik = call.receive<KorisnikRequest>()
                val exists = checkKorisnik(korisnik)

                if (exists) {
                    println("User already exists")
                    call.respond(MessageResponse("Korisnik already exists."))
                } else {
                    signUpKorisnik(korisnik)
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
                println("logIn")
                val korisnik = call.receive<KorisnikRequest>()
                val result = logIn(korisnik)
                if (result) {
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
    conn?.close()
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