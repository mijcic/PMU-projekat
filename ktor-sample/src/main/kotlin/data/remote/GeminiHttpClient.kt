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


// http klijent za komunikaciju sa gemini api-jem
val geminiClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    engine {
        requestTimeout = 60_000 // 60 sekundi timeout za API poziv
    }
}