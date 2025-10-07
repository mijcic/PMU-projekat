package com.example.service.post

import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofit

/**
 * Service interface responsible for generating structured crime-related content
 * using a generative AI model (Gemini API).
 */

interface GeminiService {
    /**
     * Generates a [GeminiResponseRetrofit] based on the provided prompt and existing tables.
     *
     * @param prompt The input prompt that describes the content generation instructions,
     *               such as generating a murder mystery case with structured data.
     * @param tables A JSON-formatted string representing existing data (e.g., crime tables)
     *               to be used as context for generation.
     * @return A [Result] containing a [GeminiResponseRetrofit] if successful, or an exception otherwise.
     */
    suspend fun generateContent(prompt: String, tables: String): Result<GeminiResponseRetrofit>

    /**
     * Queries the Gemini API to generate or retrieve data specifically related to mysterious symptoms,
     * based on the provided prompt and context tables.
     *
     * This function sends a prompt and existing contextual data (in JSON format) to the Gemini API
     * and returns the raw or processed response relevant to mysterious symptoms.
     *
     * @param prompt The input prompt that describes the query or instructions related to mysterious symptoms.
     * @param tables A JSON-formatted string representing existing contextual data to assist in generation or retrieval.
     * @return The response from the Gemini API related to mysterious symptoms. The return type is [Any]
     *         as the exact response format may vary depending on the implementation.
     */
    suspend fun queryGeminiMysteriousSymptoms(prompt: String,tables:String): Any



    suspend fun generateContentStep1Murder(prompt: String, tables: String): Result<String>
    suspend fun generateContentStep2Murder(prompt: String, tables: String): Result<String>
    suspend fun generateContentStep3Murder(prompt: String, tables: String): Result<String>

    suspend fun generateContentStep4Murder(prompt: String, tables: String): Result<String>
    suspend fun generateContentStep5Murder(prompt: String, tables: String): Result<String>
}
