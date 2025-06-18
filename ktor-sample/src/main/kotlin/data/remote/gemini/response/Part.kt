package com.example.data.remote.gemini.response

import kotlinx.serialization.Serializable

/**
 * A single part of text in the Gemini API response.
 *
 * @property text The textual content of this part.
 */
@Serializable
data class Part(
    val text: String
)