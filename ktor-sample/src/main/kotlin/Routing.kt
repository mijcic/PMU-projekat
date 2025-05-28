package com.example

import com.example.data.remote.GEMINI_API_KEY
import com.example.data.remote.geminiClient
import com.example.models.dto.gemini.GeminiResponseRetrofit
import com.example.parser.DefaultGeminiResponseParser
import com.example.service.GeminiService
import com.example.service.GeminiServiceImpl
import com.example.repository.Repository
import com.example.service.GeminiMurderService
import com.example.service.GeminiMysteriousSymptomsService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*

fun Application.configureRouting() {
    routing {
        val repository: Repository = Repository()

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
        //"1234"
        "mia123"
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