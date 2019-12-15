package com.rondao.shufflesongs.network

sealed class WrapperType(val wrapperType: String) {

    data class Artist(val id: Double,
                      val artistType: String,
                      val artistName: String,
                      val primaryGenreName: String) : WrapperType(WrapperTypes.artist.name)

    data class Track(val id: String,
                     val artistId: Double,
                     val artistName: String,
                     val primaryGenreName: String,
                     val trackName: String,
                     val artworkUrl: String,
                     val releaseDate: String,
                     val trackTimeMillis: Double,
                     val trackExplicitness: String,
                     val trackCensoredName: String,
                     val country: String,
                     val collectionName: String,
                     val collectionId: Double) : WrapperType(WrapperTypes.track.name)
}

enum class WrapperTypes {
    artist,
    track
}