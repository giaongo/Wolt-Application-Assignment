package fi.giao.woltapplication.network

import android.util.Log
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import fi.giao.woltapplication.database.Venue
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://restaurant-api.wolt.com/v1/pages/"
private val retrofit = Retrofit
    .Builder().addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface ApiService {
    @GET("restaurants?lat=60.170187&lon=24.930599")
    suspend fun getDataString():String
}
object VenueApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
