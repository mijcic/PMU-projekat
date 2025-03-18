package rs.ac.bg.etf.projekat.data.retrofit

import retrofit2.http.GET
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin


const val BASE_URL = "http://192.168.0.18:8080/"

interface Api {
    @GET("zlocin")
    suspend fun getZlocin():List<Zlocin>
}
