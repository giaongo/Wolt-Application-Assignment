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
    private fun getVenueData() {
        viewModelScope.launch {
            try {
                val stringVenue = VenueApi.retrofitService.getDataString(lat = "60.170187", lon = "24.930599")
                val listOfVenue = toVenue(stringVenue)
                result.value = listOfVenue
            } catch (e:Exception) {
                Log.e("Failure in fetching",e.message.toString())
            }

        }
    }

    private fun toVenue(venueString:String):List<Venue> {
        val venueList:MutableList<Venue> = mutableListOf()
        val obj = JSONObject(venueString)
        val jsonArray = obj.getJSONArray("sections")
        for (index in 1 until jsonArray.length()) {
            val items = jsonArray.getJSONObject(index).getJSONArray("items")
            for (indexItem in 0 until items.length()) {
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



    fun printData() {
        Log.d("ViewModel","Data is printing")
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