package com.example.service.post

import com.example.data.remote.gemini.response.GeminiResponse
import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofit
import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofitMysteriousSymptoms

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

    /**
     * Parses the raw [GeminiResponse] to extract and transform data related to mysterious symptoms.
     *
     * This function processes the input response and returns a structured
     * [GeminiResponseRetrofitMysteriousSymptoms] object which specifically contains
     * data regarding mysterious symptoms as defined by the application logic.
     *
     * @param geminiResponse The raw response data from the Gemini API.
     * @return A parsed and structured [GeminiResponseRetrofitMysteriousSymptoms] instance
     *         containing the mysterious symptoms data extracted from the response.
     */
    fun getDataGeminiResponseMysteriousSymptoms(geminiResponse: GeminiResponse): GeminiResponseRetrofitMysteriousSymptoms
}
