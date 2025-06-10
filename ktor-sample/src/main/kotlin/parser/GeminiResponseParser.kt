package com.example.parser

import com.example.models.dto.gemini.response.GeminiResponse
import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofit

//Parser treba da pretvori (parsira) sirovi odgovor od Gemini API-ja u tvoje konkretne domenske objekte, tj. DTO-ove koje koristiš za dalje procesiranje ili upis u ba

interface GeminiResponseParser {
    suspend fun parseGeminiResponse(response: GeminiResponse): GeminiResponseRetrofit
}