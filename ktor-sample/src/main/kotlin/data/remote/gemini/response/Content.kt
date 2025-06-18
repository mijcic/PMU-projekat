package com.example.data.remote.gemini.response

import kotlinx.serialization.Serializable

/**
 * The content of the response consisting of a list of text parts.
 *
 * @property parts A list of text parts that together form the complete response.
 */
@Serializable
data class Content(
    val parts: List<Part>
)