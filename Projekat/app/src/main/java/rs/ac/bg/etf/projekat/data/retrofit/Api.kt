package rs.ac.bg.etf.projekat.data.retrofit

import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import rs.ac.bg.etf.projekat.data.retrofit.models.GeminiResponseRetrofit
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest
import rs.ac.bg.etf.projekat.data.retrofit.models.MessageResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.ScorePageKorisnikResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
import rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinRequest

//const val BASE_URL = "http://192.168.0.14:8080/"
const val BASE_URL = "http://10.0.2.2:8080/"

interface Api {
    @GET("zlocin")
    suspend fun getZlocin():List<Zlocin>

    @POST("postZlocin")
    @Headers("Content-Type: application/json")
    suspend fun postZlocin(@Body request: Zlocin): MessageResponse

    @POST("insertData")
    suspend fun insertData(@Body request: ZlocinRequest): MessageResponse

    @POST("signUp")
    suspend fun signUp(@Body request: KorisnikRequest): MessageResponse

    @POST("logIn")
    suspend fun logIn(@Body request: KorisnikRequest): MessageResponse

    @GET("scoreKorisnika")
    suspend fun scoreKorisnika():List<ScorePageKorisnikResponse>


    @POST("gemini")
    suspend fun geminiData(@Body request: RequestBody):GeminiResponseRetrofit
}