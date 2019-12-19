package com.rondao.shufflesongs.ui.songslist

import android.app.Application
import androidx.lifecycle.*
import com.rondao.shufflesongs.database.ITracksDatabase
import com.rondao.shufflesongs.database.getDatabase
import com.rondao.shufflesongs.domain.Track
import com.rondao.shufflesongs.repository.TracksRepository
import com.rondao.shufflesongs.utils.LiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

enum class SongsListApiStatus { LOADING, ERROR, DONE }

class SongsListViewModel(application: Application,
                         database: ITracksDatabase = getDatabase(application)) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val tracksRepository: TracksRepository = TracksRepository(database)

    private val songsSource = tracksRepository.tracks
    private val songsShuffled = MutableLiveData<List<Track>>(songsSource.value)

    /**
     * [_status] Mediator will check if LiveData from Database is not empty.
     * Which means there is content available and status is completed.
     */
    private val _status = MediatorLiveData<SongsListApiStatus>()
    val status: LiveData<SongsListApiStatus>
        get() = _status

    /**
     * [_songs] Mediator will join two LiveData.
     * Fetched from Database or a shuffled list.
     */
    private val _songs = MediatorLiveData<List<Track>>()
    val songs: LiveData<List<Track>>
        get() = _songs

    init {
        _songs.addSource(songsSource) { value -> _songs.value = value }
        _songs.addSource(songsShuffled) { value -> _songs.value = value }

        _status.addSource(songsSource) { value ->
            if (value.isNotEmpty()) _status.value = SongsListApiStatus.DONE
        }

        fetchTracksList()
    }

    val isSongsListNotEmpty = Transformations.map(songs) { it?.isNotEmpty() ?: true }

    val eventStatusFailed = Transformations.map(status) { status ->
        if (status == SongsListApiStatus.ERROR) LiveEvent(status) else null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun shuffleSongs(songs: List<Track>? = songsSource.value) {
        val tracksByArtists = songs?.groupBy { it.artistId }

        val queue = convertToPriorityQueue(tracksByArtists)
        if (queue == null || queue.isEmpty()) return

        val shuffledList = mutableListOf<Track>()

        // [lastList] is always absent when adding random music inside loop.
        // This guarantees two Artists' song are never adjacent.
        var lastList = addRandomMusic(queue.poll(), shuffledList)
        while (queue.isNotEmpty()) {
            val currentList = addRandomMusic(queue.poll(), shuffledList)

            if (lastList.list.isNotEmpty()) queue.add(lastList)
            lastList = currentList
        }

        /* It is possible that [lastList] has 2 or more songs, which means songs
             from same Artist will be adjacent.
           However, if it happens, it means one Artist had some many more songs
             than all others that it would be impossible to avoid it. */
        if (lastList.list.isNotEmpty()) shuffledList.addAll(lastList.list)

        songsShuffled.value = shuffledList
    }


    private fun fetchTracksList() = coroutineScope.launch {
        try {
            _status.value = SongsListApiStatus.LOADING
            tracksRepository.refreshTracks()
        } catch (e: Exception) {
            if (songs.value.isNullOrEmpty()) {
                _status.value = SongsListApiStatus.ERROR
            } else {
                _status.value = SongsListApiStatus.DONE
            }
        }
    }

    private fun convertToPriorityQueue(map: Map<Int, List<Track>>?) = map?.let {
        val queue = PriorityQueue<CompareListSize>()
        it.forEach { (_, songsList) ->
            queue.add(CompareListSize(songsList.toMutableList()))
        }
        queue
    }

    private fun addRandomMusic(biggestList: CompareListSize, shuffledList: MutableList<Track>): CompareListSize {
        val indexSong = Random.nextInt(biggestList.list.size)
        shuffledList.add(biggestList.list.removeAt(indexSong))

        return biggestList
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SongsListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SongsListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

/**
 * Envelop a [MutableList] of [Track] making it [Comparable] by list size.
 * Bigger list will have higher priority at [PriorityQueue] to be extracted.
 */
class CompareListSize(val list: MutableList<Track>) : Comparable<CompareListSize> {
    override fun compareTo(other: CompareListSize) = other.list.size - this.list.size
}