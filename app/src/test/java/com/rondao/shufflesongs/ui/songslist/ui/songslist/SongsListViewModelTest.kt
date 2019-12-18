package com.rondao.shufflesongs.ui.songslist.ui.songslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rondao.shufflesongs.network.WrapperType.Track
import com.rondao.shufflesongs.ui.songslist.SongsListViewModel
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.Rule

class SongsListViewModelTest {
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

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shuffleSongs_standardSongsList_noAdjacent() {

        val songsListViewModel = SongsListViewModel()

        val observer = Observer<List<Track>> {}
        try {
            songsListViewModel.songsList.observeForever(observer)

            songsListViewModel.shuffleSongs(songsByArtist)

            val shuffledSongsList = songsListViewModel.songsList.value

            assertThat(shuffledSongsList, not(nullValue()))

            // Every song should be at shuffled list
            songsByArtist.forEach { (_, songsList) ->
                songsList.forEach {
                    assertThat(shuffledSongsList, hasItem(it))
                }
            }

            // There shouldn't be adjacent songs by same artist
            var previousSong = shuffledSongsList!![0]
            shuffledSongsList.drop(1).forEach { currentSong ->
                assertThat(previousSong.artistId, not(equalTo(currentSong.artistId)))
                previousSong = currentSong
            }
        } finally {
            songsListViewModel.songsList.removeObserver(observer)
        }
    }
}