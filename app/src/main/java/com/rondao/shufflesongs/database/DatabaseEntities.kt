package com.rondao.shufflesongs.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rondao.shufflesongs.domain.Track

//data class DbArtist(val id: Int = 0,
//                    val artistType: String = "",
//                    val artistName: String = "",
//                    val primaryGenreName: String = "") {
//    fun asDomainModel(): Artist {
//        return Artist(id = id,
//                artistType = artistType,
//                artistName = artistName,
//                primaryGenreName = primaryGenreName)
//
//    }
//}

@Entity
data class DbTrack constructor (
        @PrimaryKey
        val id: Int = 0,
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
        val collectionId: Int = 0) {
    fun asDomainModel(): Track {
        return Track(id = id,
                artistId = artistId,
                artistName = artistName,
                primaryGenreName = primaryGenreName,
                trackName = trackName,
                artworkUrl = artworkUrl,
                releaseDate = releaseDate,
                trackTimeMillis = trackTimeMillis,
                trackExplicitness = trackExplicitness,
                trackCensoredName = trackCensoredName,
                country = country,
                collectionName = collectionName,
                collectionId = collectionId)
    }
}


