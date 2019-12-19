package com.rondao.shufflesongs.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.rondao.shufflesongs.database.ITracksDatabase
import com.rondao.shufflesongs.domain.Track
import com.rondao.shufflesongs.network.NetworkWrapperType
import com.rondao.shufflesongs.network.ShuffleSongsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: For proper tests, should also have DI for remote source and Dispatcher.
//  Currently [getSongs()] is really trying to access network during tests.
//  Also, it will allow to perform tests on [TracksRepository] itself.
class TracksRepository(private val database: ITracksDatabase) {

    val tracks: LiveData<List<Track>> =
            Transformations.map(database.trackDao.getTracks()) { tracks ->
                tracks.map { it.asDomainModel() }
            }

    suspend fun refreshTracks() {
        withContext(Dispatchers.IO) {
            val songsAndArtists = ShuffleSongsApi.retrofitService.getSongs()

            // Filtering Network object. Maybe better to be done at Network side...
            val networkSongsList = songsAndArtists.filterIsInstance<NetworkWrapperType.NetworkTrack>()
            val dbDongsList = networkSongsList.map { it.asDatabaseModel() }

            database.trackDao.insertAll(*dbDongsList.toTypedArray())
        }
    }
}