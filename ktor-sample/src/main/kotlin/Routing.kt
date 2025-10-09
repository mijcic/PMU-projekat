package com.example

import com.example.data.remote.client.GEMINI_API_KEY
import com.example.data.remote.client.GeminiClient
import com.example.data.remote.tables.KorisnikRequest
import com.example.data.remote.tables.MessageResponse
import com.example.models.domain.Story
import com.example.data.remote.gemini.request.GeminiRequest2
import com.example.data.remote.gemini.request.GeminiRequest2MysteriousSymptoms
import com.example.parser.DefaultGeminiResponseParser
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
import kotlinx.coroutines.delay
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


    val databaseService = DatabaseService(
        dbUrl = "jdbc:mysql://localhost:3306/whodunit?useSSL=false&allowPublicKeyRetrieval=true",
        user = "root",
         password = "1234"
       // password = "mia123"
    )
    val connection = databaseService.getDatabaseConnection() ?: error("Database connection failed — cannot start routing.")
    val repository: Repository = Repository(connection)
    val geminiService: GeminiService = GeminiServiceImpl(GeminiClient.geminiClient, GEMINI_API_KEY, DefaultGeminiResponseParser())
    val initialDataService = InitialDataService(geminiService, databaseService)
    val korisnikService = KorisnikService(databaseService)

    launch {
        initialDataService.insertInitialMurderIfEmpty()
    }
    launch {
        delay(2000)
        //initialDataService.insertInitialMysteriousSymptomsIfEmpty()
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
                val prettyPrinted = rawResultString
                    .replace(",", ",\n")
                    .replace("(", "(\n")
                    .replace(")", "\n)")
                    .replace("=", ": ")
                    .replace("Success", "✅ Success")

                println("===== Formatirani rezultat =====")
                println(prettyPrinted)
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