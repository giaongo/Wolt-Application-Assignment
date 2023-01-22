package fi.giao.woltapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

data class VenueAndFavorite(
    @Embedded val venue: Venue,
    @Relation(
        parentColumn = "id",
        entityColumn = "venue_id"
    )
    val favorite: Favorite?
)

@Dao
interface VenueAndFavoriteDao {
    @Transaction
    @Query("SELECT * FROM Venue")
    fun getVenueAndFavorite():LiveData<List<VenueAndFavorite>>
}

