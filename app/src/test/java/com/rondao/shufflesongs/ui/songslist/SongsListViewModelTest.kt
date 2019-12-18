package com.rondao.shufflesongs.ui.songslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rondao.shufflesongs.getOrAwaitValue
import com.rondao.shufflesongs.network.WrapperType.Track
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.Rule

class SongsListViewModelTest {
    // Generate fake data for tests
    private val songsByArtist: Map<Int, List<Track>>
    init {
        val map = mutableMapOf<Int, List<Track>>()
        val size = 5

        for (artistId in 1..size) {
            val songsList = mutableListOf<Track>()
            for (songNum in 1..size) {
                songsList.add(Track(id = (size * artistId + songNum), artistId = artistId))
            }
            map[artistId] = songsList
        }

        songsByArtist = map
    }

    private lateinit var songsListViewModel: SongsListViewModel
    private lateinit var shuffledSongsList: List<Track>

    @Before
    fun setupViewModel() {
        songsListViewModel = SongsListViewModel()
        songsListViewModel.shuffleSongs(songsByArtist)

        shuffledSongsList = songsListViewModel.songsList.getOrAwaitValue()
    }

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shuffleSongs_standardSongsList_containsEverySong() {
        songsByArtist.forEach { (_, songsList) ->
            songsList.forEach {
                assertThat(shuffledSongsList, hasItem(it))
            }
        }
    }

    @Test
    fun shuffleSongs_standardSongsList_noAdjacent() {
        var previousSong = shuffledSongsList[0]
        shuffledSongsList.drop(1).forEach { currentSong ->
            assertThat(previousSong.artistId, not(equalTo(currentSong.artistId)))
            previousSong = currentSong
        }
    }

    @Test
    fun shuffleSongs_nullOrEmptySongsList_noChange() {
        songsListViewModel.shuffleSongs(null)
        val shuffledSongsListAfterNull = songsListViewModel.songsList.getOrAwaitValue()
        assertThat(shuffledSongsList, equalTo(shuffledSongsListAfterNull))

        songsListViewModel.shuffleSongs(mapOf())
        val shuffledSongsListAfterEmpty = songsListViewModel.songsList.getOrAwaitValue()
        assertThat(shuffledSongsList, equalTo(shuffledSongsListAfterEmpty))
    }
}