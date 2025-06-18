package com.example.parser

import com.example.data.remote.gemini.response.GeminiResponse
import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofit
import com.example.service.post.GeminiServiceResponseImpl

/**
 * Default implementation of [GeminiResponseParser] that parses
 * a [GeminiResponse] into a [GeminiResponseRetrofit].
 *
 * Delegates parsing to [GeminiServiceResponseImpl].
 */
class DefaultGeminiResponseParser : GeminiResponseParser {
    /**
     * Parses the given [response] from Gemini API into a [GeminiResponseRetrofit] model.
     *
     * @param response The raw [GeminiResponse] received from the API.
     * @return The parsed [GeminiResponseRetrofit] object.
     */
    override suspend fun parseGeminiResponse(response: GeminiResponse): GeminiResponseRetrofit {
        val gem = GeminiServiceResponseImpl(response)
        return gem.getDataGeminiResponse(response)
    }
}