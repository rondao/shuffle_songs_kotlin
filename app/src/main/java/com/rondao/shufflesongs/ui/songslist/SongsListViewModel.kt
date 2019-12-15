package com.rondao.shufflesongs.ui.songslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rondao.shufflesongs.network.ShuffleSongsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SongsListViewModel : ViewModel() {
    private val artists_id = listOf(909253, 1171421960, 358714030, 1419227, 264111789)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _songsList = MutableLiveData<String>()
    val songsList: LiveData<String>
        get() = _songsList

    init {
        fetchSongsList()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun fetchSongsList() {
        coroutineScope.launch {
            try {
                _songsList.value = ShuffleSongsApi
                        .retrofitService.getSongs(artists_id.joinToString(","))
            } catch (e: Exception) {
                _songsList.value = "Failure: ${e.message}"
            }
        }
    }
}