package com.example.data.remote.gemini.request

import com.example.models.domain.Tables
import kotlinx.serialization.Serializable

/**
 * Represents a request payload for the Gemini service.
 *
 * @property prompt A textual prompt used to guide the Gemini model's response.
 * @property tables A [Tables] object containing structured data relevant to the prompt.
 */
@Serializable
data class GeminiRequest2(
    val prompt: String,
    val tables: Tables
)