package com.example.parser

import com.example.models.dto.gemini.GeminiResponse
import com.example.models.dto.gemini.GeminiResponseRetrofit
import com.example.service.post.GeminiServiceResponseImpl

class DefaultGeminiResponseParser : GeminiResponseParser {
    override suspend fun parseGeminiResponse(response: GeminiResponse): GeminiResponseRetrofit {
        val gem = GeminiServiceResponseImpl(response)
        return gem.getDataGeminiResponse(response)
    }
}