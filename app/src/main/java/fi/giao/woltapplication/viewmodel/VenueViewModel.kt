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

    val venueList:LiveData<List<Venue>> = appRepository.getAllVenues()

    fun addFavorite(favoriteVenue:Favorite) = viewModelScope.launch {
        appRepository.addFavorite(favoriteVenue)
    }
    fun removeFavorite(favoriteVenue: Favorite) = viewModelScope.launch {
        appRepository.removeFavorite(favoriteVenue)
    }

    /*
     * This function does network fetching and returns Venue object
     */
    fun getVenueData(currentCoordinate:List<String>) {
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