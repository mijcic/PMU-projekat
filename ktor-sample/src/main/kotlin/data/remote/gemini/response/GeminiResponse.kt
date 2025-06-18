package com.example.data.remote.gemini.response

import kotlinx.serialization.Serializable

/**
 * The base response from the Gemini API containing a list of candidate responses.
 *
 * @property candidates The list of candidate responses returned by the API.
 */
@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null
)