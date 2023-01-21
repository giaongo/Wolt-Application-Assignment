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
    val currentCoordinate = mutableListOf<String>()

    val venueList:LiveData<List<Venue>> = appRepository.getAllVenues()
    val venueFavoriteList:LiveData<List<Favorite>> = appRepository.getAllFavorites()

    fun addFavorite(favoriteVenue:Favorite) = viewModelScope.launch {
        appRepository.addFavorite(favoriteVenue)
    }
    fun removeFavorite(favoriteVenue: Favorite) = viewModelScope.launch {
        appRepository.removeFavorite(favoriteVenue)
    }
    fun setCurrentCoordinate(coordinate:List<String>) {
        currentCoordinate.addAll(coordinate)
    }

    /*
     * This function does network fetching and returns Venue object
     */
    fun getVenueData() {
        viewModelScope.launch {
            Log.d("resultFromModel",currentCoordinate.toString())
            appRepository.getDataFromNetworkAndSave(currentCoordinate)
        }
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