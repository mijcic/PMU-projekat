package com.example.parser

import com.example.models.dto.gemini.response.GeminiResponse
import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofit
import com.example.service.post.GeminiServiceResponseImpl

class DefaultGeminiResponseParser : GeminiResponseParser {
    override suspend fun parseGeminiResponse(response: GeminiResponse): GeminiResponseRetrofit {
        val gem = GeminiServiceResponseImpl(response)
        return gem.getDataGeminiResponse(response)
    }
}