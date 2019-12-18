package com.rondao.shufflesongs.ui.songslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rondao.shufflesongs.network.ShuffleSongsApi
import com.rondao.shufflesongs.network.WrapperType.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class SongsListViewModel : ViewModel() {
    private val artistsId = listOf(909253, 1171421960, 358714030, 1419227, 264111789)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var songsByArtist: Map<Double, List<Track>>? = null

    private val _songsList = MutableLiveData<List<Track>>()
    val songsList: LiveData<List<Track>>
        get() = _songsList

    init {
        fetchSongsList()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun shuffleSongs() {
        val queue = convertToPriorityQueue(songsByArtist)
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

        _songsList.value = shuffledList
    }


    private fun fetchSongsList() {
        coroutineScope.launch {
            try {
                val songsAndArtists = ShuffleSongsApi
                        .retrofitService.getSongs(artistsId.joinToString(","))
                val songsList = songsAndArtists.filterIsInstance<Track>()

                songsByArtist = songsList.groupBy { it.artistId }
                _songsList.value = songsList
            } catch (e: Exception) {
                // TODO: Add SnackBar event for network failure
            }
        }
    }

    private fun convertToPriorityQueue(map: Map<Double, List<Track>>?) = map?.let {
        val queue = PriorityQueue<CompareListSize>()
        it.forEach { (artist, songsList) ->
            queue.add(CompareListSize(songsList.toMutableList()))
        }
        queue
    }

    private fun addRandomMusic(biggestList: CompareListSize, shuffledList: MutableList<Track>): CompareListSize {
        val indexSong = Random.nextInt(biggestList.list.size)
        shuffledList.add(biggestList.list.removeAt(indexSong))

        return biggestList
    }
}

/**
 * Envelop a [MutableList] of [Track] making it [Comparable] by list size.
 * Bigger list will have higher priority at [PriorityQueue] to be extracted.
 */
class CompareListSize(val list: MutableList<Track>) : Comparable<CompareListSize> {
    override fun compareTo(other: CompareListSize) = other.list.size - this.list.size
}