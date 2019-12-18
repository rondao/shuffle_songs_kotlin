package com.rondao.shufflesongs.network

import com.rondao.shufflesongs.database.DbTrack
import com.rondao.shufflesongs.domain.Track

sealed class NetworkWrapperType(val wrapperType: String) {

    data class NetworkArtist(val id: Int,
                      val artistType: String,
                      val artistName: String,
                      val primaryGenreName: String) : NetworkWrapperType(NetworkWrapperTypes.artist.name) {
//        fun asDomainModel(): Artist {
//            return Artist(id = id,
//                    artistType = artistType,
//                    artistName = artistName,
//                    primaryGenreName = primaryGenreName)
//
//        }
//        fun asDatabaseModel(): DbArtist {
//            return DbArtist(id = id,
//                    artistType = artistType,
//                    artistName = artistName,
//                    primaryGenreName = primaryGenreName)
//
//        }
    }

    data class NetworkTrack(val id: Int = 0,
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
                     val collectionId: Int = 0) : NetworkWrapperType(NetworkWrapperTypes.track.name) {
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
//        fun asDatabaseModel(): DbTrack {
//            return DbTrack(id = id,
//                    artistId = artistId,
//                    artistName = artistName,
//                    primaryGenreName = primaryGenreName,
//                    trackName = trackName,
//                    artworkUrl = artworkUrl,
//                    releaseDate = releaseDate,
//                    trackTimeMillis = trackTimeMillis,
//                    trackExplicitness = trackExplicitness,
//                    trackCensoredName = trackCensoredName,
//                    country = country,
//                    collectionName = collectionName,
//                    collectionId = collectionId)
//        }
    }
}

/**
 * Encapsulates the possible values of WrapperTypes.
 * Variable names starts Lowercase to match name with json.
 */
enum class NetworkWrapperTypes {
    artist,
    track
}