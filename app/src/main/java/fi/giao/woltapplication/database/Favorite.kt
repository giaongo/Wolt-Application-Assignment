package fi.giao.woltapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val venue_id:Int
)
@Dao
interface FavoriteDao{
    @Query("SELECT * FROM Favorite")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favoriteVenue:Favorite):Long

    @Delete
    suspend fun removeFavorite(favoriteVenue:Favorite):Int
}
