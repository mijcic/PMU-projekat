package com.example.service.post

import com.example.models.dto.gemini.retrofit.GeminiResponseRetrofit

interface GeminiService {
    suspend fun generateContent(prompt: String, tables: String): Result<GeminiResponseRetrofit>
}
