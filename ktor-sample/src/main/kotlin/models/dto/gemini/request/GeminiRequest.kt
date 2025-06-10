package com.example.models.dto.gemini.request

import com.example.models.dto.gemini.response.Content
import kotlinx.serialization.Serializable

/**
 * The request model sent to the Gemini API.
 *
 * @property contents A list of content objects sent in the request.
 */
@Serializable
data class GeminiRequest(
    val contents: List<Content>
)