package com.example.service.post

import com.example.models.dto.gemini.GeminiResponse
import com.example.models.dto.gemini.GeminiResponseRetrofit

interface GeminiServiceResponse {
    suspend fun getDataGeminiResponse(geminiResponse: GeminiResponse): GeminiResponseRetrofit
}
