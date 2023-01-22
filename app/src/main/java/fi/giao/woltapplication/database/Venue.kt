package fi.giao.woltapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Venue (
    @PrimaryKey
    val id:String,
    val name:String,
    val short_description:String,
    val url:String
)

@Dao
interface VenueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllVenues(venueList:List<Venue>)

}