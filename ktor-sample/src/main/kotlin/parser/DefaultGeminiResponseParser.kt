package com.example.parser

import com.example.data.remote.gemini.response.GeminiResponse
import com.example.data.remote.gemini.retrofit.GeminiResponse2
import com.example.data.remote.gemini.retrofit.GeminiResponse2Step1
import com.example.data.remote.gemini.retrofit.GeminiResponse2Step2
import com.example.data.remote.gemini.retrofit.GeminiResponseRetrofit
import com.example.data.remote.tables.OsobaData
import com.example.data.remote.tables.UsedZlocinData
import com.example.data.remote.tables.ZlocinData
import com.example.data.remote.tables.ZrtvaData
import com.example.getDatabaseConnection
import com.example.repository.GeminiProRepositoryImpl
import com.example.repository.RepositoryInsert
import com.example.service.post.GeminiServiceResponseImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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

    override suspend fun parseGeminiResponseMurderStep1(geminiResponse: GeminiResponse): String {
        //val geminiResponse: GeminiResponse = GeminiResponse()

        val geminiProRepo = GeminiProRepositoryImpl()
        val json2 = Json {
            ignoreUnknownKeys = true
        }
        val cleanJsonString = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.replace("`", "")
        val cleanJsonString2 = cleanJsonString?.removePrefix("json")
        val geminiResponse2: GeminiResponse2Step1? =
            cleanJsonString2?.let {
                json2.decodeFromString(
                    it
                )
            }
        println(geminiResponse2)

        val geminiResponseRetrofit: GeminiResponseRetrofit = GeminiResponseRetrofit(
            zlocinRetrofit = null,
            zrtvaRetrofit = null,
            osumnjiceniRetrofit = null,
            dokaziRetrofit =null,
            telefoniRetrofit = null,
            forenzickiDokazRetrofit = null,
            obdukcijaRetrofit = null,
            svedociRetrofit =null,
            oneContactRetrofit = null,
            kontaktiRetrofit = null,
            porukeRetrofit = null,
            poziviRetrofit = null,
            galerijaRetrofit = null,
            aplikacijeRetrofit = null,
            tragoviRetrofit = null,
            dokaziOsumnjiceniRetrofit = null,
            beleskeRetrofit = null,
            whatsappKontaktRetrofit = null,
            whatsappPorukaRetrofit = null,
            oneCallRetrofit = null,
            galleryRetrofit = null,
            obicnePorukeRetrofit = null,
            odnosiOsumnjiceniZrtvaRetrofit = null,
            pitanjaRetrofit = null,
            odgovoriRetrofit = null,
            pitanjeIspitivanjeOsumnjicenogRetrofit = null,
            pitanjeIspitivanjeSvedokaRetrofit = null,
            osobeRetrofit = null,
            zadaciRetrofit = null,
            dokaziZadaciRetrofit = null,
            ispitivanjeOsumnjicenogZadaciRetrofit = null,
            ispitivanjeSvedokaZadaciRetrofit = null,
            telefonZadaciRetrofit = null,
            forenzickiDokazZadaciRetrofit = null
        )


        //conn?.close()
        return geminiResponse2.toString()




    }

    override suspend fun parseGeminiResponseMurderStep2(geminiResponse: GeminiResponse): String {

        val geminiProRepo = GeminiProRepositoryImpl()
        val json2 = Json {
            ignoreUnknownKeys = true
        }
        val cleanJsonString = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text?.replace("`", "")
        val cleanJsonString2 = cleanJsonString?.removePrefix("json")
        val geminiResponse2: GeminiResponse2Step2? =
            cleanJsonString2?.let {
                json2.decodeFromString(
                    it
                )
            }
        println(geminiResponse2)

        return geminiResponse2.toString()
    }
}