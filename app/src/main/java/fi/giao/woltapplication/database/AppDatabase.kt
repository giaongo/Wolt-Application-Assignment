package fi.giao.woltapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Venue::class, Favorite::class], version = 1, exportSchema = false )
abstract class AppDatabase:RoomDatabase() {
    abstract val venueDao: VenueDao
    abstract val favoriteDao: FavoriteDao

    companion object{
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "wolt_database.db"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}