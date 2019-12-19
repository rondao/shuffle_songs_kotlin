package com.rondao.shufflesongs.ui.songslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rondao.shufflesongs.database.ITracksDatabase
import com.rondao.shufflesongs.database.getFakeDatabase
import com.rondao.shufflesongs.domain.Track
import com.rondao.shufflesongs.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SongsListViewModelTest {
    private lateinit var songsListViewModel: SongsListViewModel
    private lateinit var shuffledSongsList: List<Track>

    private lateinit var fakeDatabase: ITracksDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        fakeDatabase = getFakeDatabase()
        songsListViewModel = SongsListViewModel(ApplicationProvider.getApplicationContext(), fakeDatabase)

        // Need to observe for [songs] LiveData, so its value is available for ViewModel
        songsListViewModel.songs.getOrAwaitValue()
        songsListViewModel.shuffleSongs()

        shuffledSongsList = songsListViewModel.songs.getOrAwaitValue()
    }

    @Test
    fun shuffleSongs_fakeSongsList_containsEverySong() {
        val songs = songsListViewModel.songs.getOrAwaitValue()

        assertThat(songs, not(nullValue()))
        songs.forEach {
            assertThat(shuffledSongsList, hasItem(it))
        }
    }

    @Test
    fun shuffleSongs_fakeSongsList_containsEverySongFromDatabase() {
        val dbSongs = fakeDatabase.trackDao.getTracks().value

        assertThat(dbSongs, not(nullValue()))
        val songs = dbSongs?.map { track ->
            track.asDomainModel()
        }

        assertThat(songs, not(nullValue()))
        songs?.forEach {
            assertThat(shuffledSongsList, hasItem(it))
        }
    }

    @Test
    fun shuffleSongs_fakeSongsList_noAdjacent() {
        var previousSong = shuffledSongsList[0]
        shuffledSongsList.drop(1).forEach { currentSong ->
            assertThat(previousSong.artistId, not(equalTo(currentSong.artistId)))
            previousSong = currentSong
        }
    }

    @Test
    fun shuffleSongs_nullOrEmptySongsList_noChange() {
        songsListViewModel.shuffleSongs(null)
        val shuffledSongsListAfterNull = songsListViewModel.songs.getOrAwaitValue()
        assertThat(shuffledSongsList, equalTo(shuffledSongsListAfterNull))

        songsListViewModel.shuffleSongs(listOf())
        val shuffledSongsListAfterEmpty = songsListViewModel.songs.getOrAwaitValue()
        assertThat(shuffledSongsList, equalTo(shuffledSongsListAfterEmpty))
    }
}