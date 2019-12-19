package com.rondao.shufflesongs.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun getFakeDatabase(): ITracksDatabase = object : ITracksDatabase {
    private val fakeDbSongs = mutableListOf<DbTrack>()

    override val trackDao: TrackDao = object : TrackDao {
        override fun getTracks(): LiveData<List<DbTrack>> {
            return MutableLiveData(fakeDbSongs)
        }

        override fun insertAll(vararg songs: DbTrack) {
            fakeDbSongs.addAll(songs)
        }
    }

    init {
        val localSongs = mutableListOf<DbTrack>()
        val size = 5
        for (artistId in 1..size) {
            for (songNum in 1..size) {
                localSongs.add(DbTrack(id = (size * artistId + songNum), artistId = artistId))
            }
        }
        trackDao.insertAll(*localSongs.toTypedArray())
    }
}