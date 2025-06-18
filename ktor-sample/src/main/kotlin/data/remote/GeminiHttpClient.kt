package com.example.data.remote

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// --- Konstante ---
// UPOZORENJE: NIKADA NE STAVLJAJ API KLJUČ DIREKTNO U KOD U PRODUKCIJI!
// Koristi environment variable ili configuration fajl.
const val GEMINI_API_KEY = "AIzaSyD0Fssx_oFXYrO4dSoRuSfxhGpn4ziWPQk"


/**
 * HTTP client configured for communication with the Gemini API.
 *
 * This client uses the [CIO] engine and installs [ContentNegotiation] with a JSON serializer.
 * It is configured to:
 * - Ignore unknown JSON keys during deserialization.
 * - Allow lenient parsing of malformed JSON.
 * - Apply a request timeout of 60 seconds.
 *
 * @see io.ktor.client.HttpClient
 * @see io.ktor.client.engine.cio.CIO
 * @see io.ktor.client.plugins.contentnegotiation.ContentNegotiation
 * @see kotlinx.serialization.json.Json
 */
object GeminiClient {
    val geminiClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        engine {
            requestTimeout = 60_000
        }
    }
}
