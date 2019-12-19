package com.rondao.shufflesongs.network

import com.rondao.shufflesongs.network.converter.JEnvelopedResultConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://us-central1-tw-exercicio-mobile.cloudfunctions.net/"

/**
 * Json parser, using Polymorphic Adapter to build objects type of [NetworkArtist]
 * and [NetworkTrack] according to "wrapperType" value from Json.
 */
private val moshi = Moshi.Builder()
        .add(PolymorphicJsonAdapterFactory.of(NetworkWrapperType::class.java, "wrapperType")
                .withSubtype(NetworkWrapperType.NetworkArtist::class.java, NetworkWrapperTypes.artist.name)
                .withSubtype(NetworkWrapperType.NetworkTrack::class.java, NetworkWrapperTypes.track.name))
        .add(KotlinJsonAdapterFactory())
        .build()

/**
 * Using [JEnvelopedResultConverter] converter to remove result encapsulation from Json.
 */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(JEnvelopedResultConverter())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

private val defaultArtistsQueryIds = listOf(909253, 1171421960, 358714030, 1419227, 264111789)

interface ShuffleSongsApiService {
    @GET("lookup")
    suspend fun getSongs(@Query("id", encoded = true)
                         artist_ids: String = defaultArtistsQueryIds.joinToString(","),
                         @Query("limit")
                         limit: Int = 5): List<NetworkWrapperType>
}

object ShuffleSongsApi {
    val retrofitService: ShuffleSongsApiService by lazy { retrofit.create(ShuffleSongsApiService::class.java) }
}