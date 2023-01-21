package fi.giao.woltapplication.repository

import android.util.Log
import fi.giao.woltapplication.database.AppDatabase
import fi.giao.woltapplication.database.Favorite
import fi.giao.woltapplication.database.Venue
import fi.giao.woltapplication.network.VenueApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AppRepository(private val appDatabase: AppDatabase){

    /**
     * Function to retrieve data from network and save to local database
     */
    suspend fun getDataFromNetworkAndSave(coordinates: List<String>?) {
        Log.d("result","coordinate" + coordinates.toString())
        coordinates?.let {
            withContext(Dispatchers.IO) {
                insertAllVenues(getVenueData(it))
            }
        }

    }

    /*
    * This function does network fetching and returns Venue object
    */
    private suspend fun getVenueData(coordinates:List<String>): List<Venue> {
        return try {
            val stringVenue = VenueApi.retrofitService.getDataString(lat = coordinates[0], lon = coordinates[1])
            toVenue(stringVenue)
        } catch(e:Exception) {
            Log.e("Failure in fetching",e.message.toString())
            listOf()
        }
    }

    /**
     * This function takes venue json string from network fetch, filter and deserialize it to Venue object
     */
    private fun toVenue(venueString:String):List<Venue> {
        val venueList:MutableList<Venue> = mutableListOf()
        val obj = JSONObject(venueString)
        val jsonArray = obj.getJSONArray("sections")
        for (index in 1 until jsonArray.length()) {
            val items = jsonArray.getJSONObject(index).getJSONArray("items")
            val total = if(items.length() > 15) 15 else items.length()
            for (indexItem in 0 until total) {
                val venue = items.getJSONObject(indexItem).getJSONObject("venue")
                val image = items.getJSONObject(indexItem).getJSONObject("image")
                val url = image.getString("url")
                val id = venue.getString("id")
                val name = venue.getString("name")
                val shortDescription = venue.getString("short_description")
                venueList.add(
                    Venue(
                        id = id,
                        name = name,
                        short_description = shortDescription,
                        url = url
                    )
                )
            }
        }
        return venueList.toList()
    }


    // Functions for venueDao
    fun getAllVenues() = appDatabase.venueDao.getAllVenues()
    private suspend fun insertAllVenues(venueList:List<Venue>) = appDatabase.venueDao.insertAllVenues(venueList)

    // Functions for favoriteDao
    fun getAllFavorites() = appDatabase.favoriteDao.getAllFavorites()
    suspend fun addFavorite(favoriteVenue: Favorite) = appDatabase.favoriteDao.addFavorite(favoriteVenue)
    suspend fun removeFavorite(favoriteVenue: Favorite) = appDatabase.favoriteDao.removeFavorite(favoriteVenue)
}