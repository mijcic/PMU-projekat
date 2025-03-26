package rs.ac.bg.etf.projekat.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.http.Body
import rs.ac.bg.etf.projekat.data.retrofit.Api
import rs.ac.bg.etf.projekat.data.retrofit.models.MessageResponse
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
import rs.ac.bg.etf.projekat.data.retrofit.models.ZlocinRequest
import javax.inject.Inject

class Repository @Inject constructor(
    private val Api: Api,
) {
    suspend fun getZlocin():List<Zlocin> = Api.getZlocin()

    suspend fun postZlocin(@Body request: Zlocin): MessageResponse = Api.postZlocin(request)

    suspend fun insertData(@Body request: ZlocinRequest): MessageResponse = Api.insertData(request)
}