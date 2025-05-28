package com.example.service

import com.example.models.dto.gemini.GeminiResponseRetrofit

interface GeminiService {
    suspend fun generateContent(prompt: String, tables: String): Result<GeminiResponseRetrofit>
}
