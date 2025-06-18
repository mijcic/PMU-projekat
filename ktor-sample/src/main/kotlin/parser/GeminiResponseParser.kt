package com.example.parser

import com.example.data.remote.gemini.response.GeminiResponse
import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofit

//Parser treba da pretvori (parsira) sirovi odgovor od Gemini API-ja u tvoje konkretne domenske objekte, tj. DTO-ove koje koristiš za dalje procesiranje ili upis u ba

/**
 * Interface for parsing responses received from the Gemini API.
 */
interface GeminiResponseParser {
    /**
     * Parses a raw [GeminiResponse] object into a [GeminiResponseRetrofit] data model.
     *
     * This method is suspending to support asynchronous or blocking operations if needed.
     *
     * @param response The raw response object from the Gemini API.
     * @return Parsed and mapped [GeminiResponseRetrofit] object.
     */
    suspend fun parseGeminiResponse(response: GeminiResponse): GeminiResponseRetrofit
}