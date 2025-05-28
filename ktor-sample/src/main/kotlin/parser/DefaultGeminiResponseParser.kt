package com.example.parser

import com.example.getDataGeminiResponse
import com.example.models.dto.gemini.GeminiResponse
import com.example.models.dto.gemini.GeminiResponseRetrofit

class DefaultGeminiResponseParser : GeminiResponseParser {
    override fun parseGeminiResponse(response: GeminiResponse): GeminiResponseRetrofit {
        return getDataGeminiResponse(response)
    }
}