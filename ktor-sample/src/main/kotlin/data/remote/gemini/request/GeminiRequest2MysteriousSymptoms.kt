package com.example.data.remote.gemini.request

import com.example.data.remote.tables.TablesMysteriousSymptoms
import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest2MysteriousSymptoms(
    val prompt: String,
    val tables: TablesMysteriousSymptoms
)