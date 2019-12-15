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

private val moshi = Moshi.Builder()
        .add(PolymorphicJsonAdapterFactory.of(WrapperType::class.java, "wrapperType")
                .withSubtype(WrapperType.Artist::class.java, WrapperTypes.artist.name)
                .withSubtype(WrapperType.Track::class.java, WrapperTypes.track.name))
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(JEnvelopedResultConverter())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface ShuffleSongsApiService {
    @GET("lookup")
    suspend fun getSongs(@Query("id", encoded = true) artist_ids: String,
                         @Query("limit") limit: Int = 5): List<WrapperType>
}

object ShuffleSongsApi {
    val retrofitService: ShuffleSongsApiService by lazy { retrofit.create(ShuffleSongsApiService::class.java) }
}