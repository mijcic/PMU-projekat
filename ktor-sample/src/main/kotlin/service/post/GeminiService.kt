package com.example.service.post

import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofit

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
}
