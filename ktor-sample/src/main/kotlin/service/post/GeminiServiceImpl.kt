package com.example.service.post

import com.example.data.remote.client.GEMINI_API_KEY
import com.example.data.remote.client.GeminiClient
import com.example.data.remote.gemini.request.GeminiRequest
import com.example.data.remote.gemini.response.Content
import com.example.data.remote.gemini.response.GeminiResponse
import com.example.data.remote.gemini.response.Part
import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofit
import com.example.parser.GeminiResponseParser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * Implementation of [GeminiService] that communicates with the Gemini API
 * to generate structured crime-related content.
 *
 * @property client The HTTP client used to send requests.
 * @property apiKey The API key for authenticating with the Gemini API.
 * @property dataParser A parser used to convert the raw Gemini API response into a [GeminiResponseRetrofit].
 */
class GeminiServiceImpl(
    private val client: HttpClient,
    private val apiKey: String,
    private val dataParser: GeminiResponseParser
) : GeminiService {

    /**
     * Sends a prompt and table data to the Gemini API and parses the response into a [GeminiResponseRetrofit].
     *
     * @param prompt The main instruction text to be used as a prompt for content generation.
     * @param tables A JSON string representing structured data (tables) to provide context to the model.
     * @return A [Result] containing a [GeminiResponseRetrofit] if the call and parsing are successful, or an exception if an error occurs.
     *
     * @throws Exception if the API call fails or the response cannot be parsed correctly.
     */
    override suspend fun generateContent(prompt: String, tables: String): Result<GeminiResponseRetrofit> {
        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt + tables))))
        )

        return try {
            val t0 = System.currentTimeMillis()
            val response = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val t1 = System.currentTimeMillis()
            println("Gemini API trajanje: ${t1 - t0}ms")

            if (response.status == HttpStatusCode.OK) {
                val geminiResponse: GeminiResponse = response.body()
                val t2 = System.currentTimeMillis()
                println("Parsiranje trajanje: ${t2 - t1}ms")

                val parsed = dataParser.parseGeminiResponse(geminiResponse)
                val t3 = System.currentTimeMillis()
                println("Insert trajanje: ${t3 - t2}ms")

                Result.success(parsed)
            } else {
                val error = response.bodyAsText()
                println("Error from Gemini: ${response.status} - $error")
                Result.failure(Exception("Gemini API error: ${response.status}"))
            }
        } catch (e: Exception) {
            println("Exception during Gemini API call: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun queryGeminiMysteriousSymptoms(prompt: String,tables:String): Any {
        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt+tables)))),
        )

        try {
            val t0 = System.currentTimeMillis()
            val response: HttpResponse = GeminiClient.geminiClient.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$GEMINI_API_KEY") {
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
                println(geminiResponse)
                val geminiServiceResponseImpl = GeminiServiceResponseImpl(geminiResponse)
                val geminiResponseRetrofit = geminiServiceResponseImpl.getDataGeminiResponseMysteriousSymptoms(geminiResponse)

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
}