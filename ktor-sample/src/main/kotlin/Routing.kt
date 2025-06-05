package com.example

import com.example.data.remote.GEMINI_API_KEY
import com.example.data.remote.geminiClient
import com.example.models.dto.KorisnikRequest
import com.example.models.dto.MessageResponse
import com.example.models.dto.ScoreKorisnik
import com.example.models.dto.ZlocinRequest
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
import java.sql.*

fun Application.configureRouting() {
    routing {
        val connection = getDatabaseConnection()
            ?: error("Database connection failed — cannot start routing.")
        val repository: Repository = Repository(connection)
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
        //"1234"
        "mia123"
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