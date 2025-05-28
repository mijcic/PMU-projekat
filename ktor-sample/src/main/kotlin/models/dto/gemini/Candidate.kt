package com.example.models.dto.gemini

import kotlinx.serialization.Serializable

@Serializable
data class Candidate(
    val content: Content? = null,
    val finishReason: String? = null,
    val index: Int? = null
)