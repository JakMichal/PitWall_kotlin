package com.example.pitwall.data

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

/**
 * Room entity representing a favourite driver stored locally in the database.
 *
 * @property driverId Primary key — unique driver identifier from the API.
 */
@Entity(tableName = "favourite_drivers")
data class FavouriteDriver (
    @PrimaryKey val driverId: String
)

/**
 * Room DAO (Data Access Object) for operations on favourite drivers.
 *
 * All suspend functions are intended to be called from a coroutine context.
 * [getAll] returns a [Flow] — a reactive stream that automatically notifies
 * observers whenever the database content changes.
 */
@Dao
interface FavouriteDriverDao {

    /**
     * Inserts a driver into the favourites list.
     * If the driver already exists, the operation is silently ignored (IGNORE strategy).
     * @param favouriteDriver Driver entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteDriver: FavouriteDriver)

    /**
     * Removes a driver from the favourites list.
     * @param favouriteDriver Driver entity to delete.
     */
    @Delete
    suspend fun delete(favouriteDriver: FavouriteDriver)

    /**
     * Returns a reactive [Flow] of all favourite drivers.
     * The Flow automatically emits a new list whenever the table changes.
     */
    @Query("SELECT * FROM favourite_drivers")
    fun getAll(): Flow<List<FavouriteDriver>>
}

/**
 * Room entity representing a favourite constructor stored locally in the database.
 * @property constructorId Primary key — unique constructor identifier from the API.
 */
@Entity(tableName = "favourite_constructors")
data class FavouriteConstructor(
    @PrimaryKey val constructorId: String
)

/**
 * Room DAO for operations on favourite constructors.
 * Structure is analogous to [FavouriteDriverDao].
 */
@Dao
interface FavouriteConstructorDao {

    /** Inserts a constructor into the favourites list; silently ignores duplicates. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteConstructor: FavouriteConstructor)

    /** Removes a constructor from the favourites list. */
    @Delete
    suspend fun delete(favouriteConstructor: FavouriteConstructor)

    /** Returns a reactive [Flow] of all favourite constructors. */
    @Query("SELECT * FROM favourite_constructors")
    fun getAll(): Flow<List<FavouriteConstructor>>
}

/**
 * PitWall Room database.
 *
 * Contains tables for favourite drivers and constructors.
 * Implements the Singleton pattern using [@Volatile] and a [synchronized] block,
 * preventing multiple database instances from being created in a multithreaded environment.
 *
 * Database version: 2
 */
@Database(entities = [FavouriteDriver::class, FavouriteConstructor::class], version = 2)
abstract class PitWallDatabase : RoomDatabase() {

    /** Returns the DAO for favourite driver operations. */
    abstract fun favouriteDriverDao(): FavouriteDriverDao

    /** Returns the DAO for favourite constructor operations. */
    abstract fun favouriteConstructorDao(): FavouriteConstructorDao

    companion object {
        @Volatile
        private var Instance: PitWallDatabase? = null

        /**
         * Returns the singleton database instance.
         *
         * If no instance exists yet, it is created inside a [synchronized] block
         * to prevent race conditions during first-time initialization.
         *
         * @param context Application context required by Room.
         * @return Singleton instance of [PitWallDatabase].
         */
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
