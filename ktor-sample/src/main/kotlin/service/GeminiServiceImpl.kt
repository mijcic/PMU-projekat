package com.example.service

import com.example.models.dto.gemini.*
import com.example.models.dto.gemini.GeminiRequest
import com.example.parser.GeminiResponseParser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class GeminiServiceImpl(
    private val client: HttpClient,
    private val apiKey: String,
    private val dataParser: GeminiResponseParser
) : GeminiService {

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
}