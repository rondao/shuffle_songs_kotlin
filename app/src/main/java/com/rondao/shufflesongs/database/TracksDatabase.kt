package com.rondao.shufflesongs.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrackDao {
    @Query("select * from dbtrack")
    fun getTracks(): LiveData<List<DbTrack>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg songs: DbTrack)
}

/**
 * Database interface allowing for fake database mocking.
 */
interface ITracksDatabase {
    val trackDao: TrackDao
}

@Database(entities = [DbTrack::class], version = 1)
abstract class TracksDatabase : RoomDatabase(), ITracksDatabase

private lateinit var INSTANCE: TracksDatabase

fun getDatabase(context: Context): TracksDatabase {
    synchronized(TracksDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    TracksDatabase::class.java,
                    "Tracks.db").build()
        }
    }
    return INSTANCE
}