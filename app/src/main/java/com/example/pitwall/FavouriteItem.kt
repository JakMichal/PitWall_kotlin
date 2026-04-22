package com.example.pitwall

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "favourite_drivers")
data class FavouriteDriver (
    @PrimaryKey val driverId: String
)

@Dao
interface FavouriteDriverDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteDriver: FavouriteDriver)
    @Delete
    suspend fun delete(favouriteDriver: FavouriteDriver)
    @Query("SELECT * FROM favourite_drivers")
    fun getAll(): Flow<List<FavouriteDriver>>
}

@Entity(tableName = "favourite_constructors")
data class FavouriteConstructor(
    @PrimaryKey val constructorId: String
)

@Dao
interface FavouriteConstructorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteConstructor: FavouriteConstructor)
    @Delete
    suspend fun delete(favouriteConstructor: FavouriteConstructor)
    @Query("SELECT * FROM favourite_constructors")
    fun getAll(): Flow<List<FavouriteConstructor>>
}

@Database(entities = [FavouriteDriver::class, FavouriteConstructor::class], version = 2)
abstract class PitWallDatabase : RoomDatabase() {
    abstract fun favouriteDriverDao(): FavouriteDriverDao
    abstract fun favouriteConstructorDao(): FavouriteConstructorDao

    companion object {
        @Volatile
        private var Instance: PitWallDatabase? = null

        fun getDatabase(context: Context): PitWallDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    PitWallDatabase::class.java,
                    "pitwall_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
