package fi.giao.woltapplication.viewmodel

import android.app.Application
import androidx.lifecycle.*
import fi.giao.woltapplication.database.AppDatabase
import fi.giao.woltapplication.database.Favorite
import fi.giao.woltapplication.database.VenueAndFavorite
import fi.giao.woltapplication.repository.AppRepository
import kotlinx.coroutines.launch

class VenueViewModel(application: Application): AndroidViewModel(application) {
    private val appRepository = AppRepository(AppDatabase.getInstance(application))
    val venueAndFavoriteList:LiveData<List<VenueAndFavorite>> = appRepository.getVenueAndFavorite()

    fun addFavorite(favoriteVenue:Favorite) = viewModelScope.launch {
        appRepository.addFavorite(favoriteVenue)
    }
    fun removeFavorite(venueId:String) = viewModelScope.launch {
        appRepository.removeFavorite(venueId)
    }

    fun getVenueData(currentCoordinate:List<String>) {
        viewModelScope.launch {
            appRepository.getDataFromNetworkAndSave(currentCoordinate)
        }
    }
}

// Factory class to initialize view model with application parameter
class VenueViewModelFactory(private val app:Application):ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass:Class<T>):T {
        return if (modelClass.isAssignableFrom(VenueViewModel::class.java)) {
            VenueViewModel(this.app) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}