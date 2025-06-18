package com.example.data.remote.gemini.request

import com.example.data.remote.gemini.response.Content
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