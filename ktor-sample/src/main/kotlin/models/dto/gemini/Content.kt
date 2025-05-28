package com.example.models.dto.gemini


import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val parts: List<Part>
)