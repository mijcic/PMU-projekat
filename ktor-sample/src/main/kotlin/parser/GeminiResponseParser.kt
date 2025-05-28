package com.example.parser

import com.example.models.dto.gemini.GeminiResponse
import com.example.models.dto.gemini.GeminiResponseRetrofit

//Parser treba da pretvori (parsira) sirovi odgovor od Gemini API-ja u tvoje konkretne domenske objekte, tj. DTO-ove koje koristiš za dalje procesiranje ili upis u ba

interface GeminiResponseParser {
    fun parseGeminiResponse(response: GeminiResponse): GeminiResponseRetrofit
}