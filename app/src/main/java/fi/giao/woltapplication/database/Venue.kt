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
) {
    override fun toString(): String {
        return "id $id name $name description $short_description url $url"
    }
}

@Dao
interface VenueDao {
    @Query("SELECT * FROM Venue")
    fun getAllVenues(): LiveData<List<Venue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllVenues(venueList:List<Venue>)
}