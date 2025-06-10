package com.example.models.dto.gemini.response

import kotlinx.serialization.Serializable

/**
 * Represents a single candidate response from the Gemini API.
 *
 * @property content Optional content of the response.
 * @property finishReason Reason why the response finished (e.g., "stop").
 * @property index The index of the candidate in the list of responses.
 */
@Serializable
data class Candidate(
    val content: Content? = null,
    val finishReason: String? = null,
    val index: Int? = null
)