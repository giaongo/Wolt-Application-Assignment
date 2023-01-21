package fi.giao.woltapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import fi.giao.woltapplication.database.AppDatabase
import fi.giao.woltapplication.database.Favorite
import fi.giao.woltapplication.database.Venue
import fi.giao.woltapplication.network.VenueApi
import fi.giao.woltapplication.repository.AppRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

class VenueViewModel(application: Application): AndroidViewModel(application) {
    private val appRepository = AppRepository(AppDatabase.getInstance(application))
    val result = MutableLiveData<List<Venue>>()

    val venueList:LiveData<List<Venue>> = appRepository.getAllVenues()
    val venueFavoriteList:LiveData<List<Favorite>> = appRepository.getAllFavorites()

    init {
        getVenueData()
    }
    fun insertAllVenues(venueList:List<Venue>) = viewModelScope.launch {
        appRepository.insertAllVenues(venueList)
    }
    fun addFavorite(favoriteVenue:Favorite) = viewModelScope.launch {
        appRepository.addFavorite(favoriteVenue)
    }
    fun removeFavorite(favoriteVenue: Favorite) = viewModelScope.launch {
        appRepository.removeFavorite(favoriteVenue)
    }

    /**
     * This function does network fetching and returns Venue object
     */
    private fun getVenueData() {
        viewModelScope.launch {
            try {
                val stringVenue = VenueApi.retrofitService.getDataString(lat = "60.2374583", lon = "24.8783533")
                val listOfVenue = toVenue(stringVenue)
                result.value = listOfVenue
            } catch (e:Exception) {
                Log.e("Failure in fetching",e.message.toString())
            }

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
                val short_description = venue.getString("short_description")
                venueList.add(
                    Venue(
                        id = id,
                        name = name,
                        short_description = short_description,
                        url = url
                    )
                )
            }
        }
        return venueList.toList()
    }

}

class VenueViewModelFactory(private val app:Application):ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass:Class<T>):T {
        return if (modelClass.isAssignableFrom(VenueViewModel::class.java)) {
            VenueViewModel(this.app) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}