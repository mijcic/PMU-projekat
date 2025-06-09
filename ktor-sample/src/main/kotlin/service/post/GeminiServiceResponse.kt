package com.example.service.post

import com.example.models.dto.gemini.response.GeminiResponse
import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofit

interface GeminiServiceResponse {
    suspend fun getDataGeminiResponse(geminiResponse: GeminiResponse): GeminiResponseRetrofit
}
