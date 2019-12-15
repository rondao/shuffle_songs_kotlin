package com.rondao.shufflesongs.ui.songslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SongsListViewModel : ViewModel() {
    private val _songsList = MutableLiveData<String>()
    val songsList: LiveData<String>
        get() = _songsList

    init {
        fetchSongsList()
    }

    private fun fetchSongsList() {
        _songsList.value = "TEST"
    }
}
