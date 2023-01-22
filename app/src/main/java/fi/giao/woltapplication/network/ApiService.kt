package fi.giao.woltapplication.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://restaurant-api.wolt.com/v1/pages/"
private val retrofit = Retrofit
    .Builder().addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface ApiService {
    @GET("restaurants")
    suspend fun getDataString(@Query("lat") lat:String, @Query("lon") lon:String):String
}
object VenueApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
