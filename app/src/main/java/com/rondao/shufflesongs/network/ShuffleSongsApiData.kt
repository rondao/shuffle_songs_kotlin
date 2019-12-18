package com.rondao.shufflesongs.network

sealed class WrapperType(val wrapperType: String) {

    data class Artist(val id: Int,
                      val artistType: String,
                      val artistName: String,
                      val primaryGenreName: String) : WrapperType(WrapperTypes.artist.name)

    data class Track(val id: Int = 0,
                     val artistId: Int = 0,
                     val artistName: String = "",
                     val primaryGenreName: String = "",
                     val trackName: String = "",
                     val artworkUrl: String = "",
                     val releaseDate: String = "",
                     val trackTimeMillis: Int = 0,
                     val trackExplicitness: String = "",
                     val trackCensoredName: String = "",
                     val country: String = "",
                     val collectionName: String = "",
                     val collectionId: Int = 0) : WrapperType(WrapperTypes.track.name)
}

enum class WrapperTypes {
    artist,
    track
}