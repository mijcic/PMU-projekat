package rs.ac.bg.etf.projekat.data.retrofit

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import rs.ac.bg.etf.projekat.data.retrofit.models.GeminiResponseRetrofit
import rs.ac.bg.etf.projekat.data.retrofit.models.GeminiResponseRetrofitMysteriousSymptoms
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest
import rs.ac.bg.etf.projekat.data.retrofit.models.MessageResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.ScorePageKorisnikResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.Story

//const val BASE_URL = "http://192.168.0.12:8080/"
const val BASE_URL = "http://10.0.2.2:8080/"

interface Api {

    @POST("signUp")
    suspend fun signUp(@Body request: KorisnikRequest): MessageResponse

    @POST("logIn")
    suspend fun logIn(@Body request: KorisnikRequest): MessageResponse

    @GET("scoreKorisnika")
    suspend fun scoreKorisnika():List<ScorePageKorisnikResponse>

    @POST("gemini")
    suspend fun geminiData(@Body request: RequestBody):GeminiResponseRetrofit

    @POST("geminiMurderStory")
    suspend fun geminiMurderStory(@Body request: RequestBody):Story


    @POST("geminiMSStory")
    suspend fun geminiMSStory(@Body request: RequestBody):Story

    // RETROFIT 2

    @POST("geminiMS")
    suspend fun geminiDataMS(@Body request: RequestBody): GeminiResponseRetrofitMysteriousSymptoms

    @GET("geminiMurder")
    suspend fun geminiMurder(): GeminiResponseRetrofit

    @GET("geminiMysteriousSymptoms")
    suspend fun geminiMysteriousSymptoms(): GeminiResponseRetrofitMysteriousSymptoms

}