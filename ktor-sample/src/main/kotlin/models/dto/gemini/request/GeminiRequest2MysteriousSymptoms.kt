package com.example.models.dto.gemini.request

import com.example.models.dto.TablesMysteriousSymptoms
import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest2MysteriousSymptoms(
    val prompt: String,
    val tables: TablesMysteriousSymptoms
)