package com.example.service.post

import com.example.models.dto.gemini.response.GeminiResponse
import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofit

/**
 * Interface for parsing and transforming raw Gemini API responses into structured application data.
 */
interface GeminiServiceResponse {
    /**
     * Parses the raw [GeminiResponse] received from the Gemini API and transforms it into a [GeminiResponseRetrofit] object.
     *
     * @param geminiResponse The raw response received from the Gemini API.
     * @return A parsed and structured [GeminiResponseRetrofit] that can be used in the application.
     */
    suspend fun getDataGeminiResponse(geminiResponse: GeminiResponse): GeminiResponseRetrofit
}
