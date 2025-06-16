package rs.ac.bg.etf.projekat.data

import okhttp3.RequestBody
import retrofit2.http.Body
import rs.ac.bg.etf.projekat.data.retrofit.Api
import rs.ac.bg.etf.projekat.data.retrofit.models.GeminiResponseRetrofit
import rs.ac.bg.etf.projekat.data.retrofit.models.GeminiResponseRetrofitMysteriousSymptoms
import rs.ac.bg.etf.projekat.data.retrofit.models.KorisnikRequest
import rs.ac.bg.etf.projekat.data.retrofit.models.MessageResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.ScorePageKorisnikResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.Story
import javax.inject.Inject

class Repository @Inject constructor(
    private val Api: Api,
) {
    suspend fun signUp(@Body request: KorisnikRequest): MessageResponse = Api.signUp(request)

    suspend fun logIn(@Body request: KorisnikRequest): MessageResponse = Api.logIn(request)

    suspend fun scoreKorisnika():List<ScorePageKorisnikResponse> = Api.scoreKorisnika()

    suspend fun geminiData(@Body request: RequestBody):GeminiResponseRetrofit= Api.geminiData(request)

    suspend fun geminiMurderStory(@Body request: RequestBody):Story = Api.geminiMurderStory(request)

    suspend fun geminiMSStory(@Body request: RequestBody):Story = Api.geminiMSStory(request)

    // RETROFIT 2

    suspend fun geminiDataMS(@Body request: RequestBody): GeminiResponseRetrofitMysteriousSymptoms = Api.geminiDataMS(request)

    suspend fun geminiMurder(): GeminiResponseRetrofit = Api.geminiMurder()

    suspend fun geminiMysteriousSymptoms(): GeminiResponseRetrofitMysteriousSymptoms = Api.geminiMysteriousSymptoms()
}