package com.rondao.shufflesongs.domain

// Artist model was not needed for business logic.
// But could be used if needed.
//
//data class Artist(val id: Int = 0,
//                    val artistType: String = "",
//                    val artistName: String = "",
//                    val primaryGenreName: String = "")

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
                   val collectionId: Int = 0)