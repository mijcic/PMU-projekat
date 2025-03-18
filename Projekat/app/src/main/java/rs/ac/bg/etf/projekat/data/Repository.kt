package rs.ac.bg.etf.projekat.data

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import rs.ac.bg.etf.projekat.data.retrofit.Api
import rs.ac.bg.etf.projekat.data.retrofit.models.Zlocin
import javax.inject.Inject

class Repository @Inject constructor(
    private val Api: Api,
) {
    suspend fun getZlocin():List<Zlocin> = Api.getZlocin()
}