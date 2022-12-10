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
        var id: Int = 0,
        var artistId: Int = 0,
        var artistName: String = "",
        var primaryGenreName: String = "",
        var trackName: String = "",
        var artworkUrl: String = "",
        var releaseDate: String = "",
        var trackTimeMillis: Int = 0,
        var trackExplicitness: String = "",
        var trackCensoredName: String = "",
        var country: String = "",
        var collectionName: String = "",
        var collectionId: Int = 0) {
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


