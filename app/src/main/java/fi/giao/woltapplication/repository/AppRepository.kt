package fi.giao.woltapplication.repository

import fi.giao.woltapplication.database.AppDatabase
import fi.giao.woltapplication.database.Favorite
import fi.giao.woltapplication.database.Venue

class AppRepository(private val appDatabase: AppDatabase){

    // Functions for venueDao
    fun getAllVenues() = appDatabase.venueDao.getAllVenues()
    suspend fun insertAllVenues(venueList:List<Venue>) = appDatabase.venueDao.insertAllVenues(venueList)

    // Functions for favoriteDao
    fun getAllFavorites() = appDatabase.favoriteDao.getAllFavorites()
    suspend fun addFavorite(favoriteVenue: Favorite) = appDatabase.favoriteDao.addFavorite(favoriteVenue)
    suspend fun removeFavorite(favoriteVenue: Favorite) = appDatabase.favoriteDao.removeFavorite(favoriteVenue)
}