package com.rondao.shufflesongs.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://us-central1-tw-exercicio-mobile.cloudfunctions.net/"

private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

interface ShuffleSongsApiService {
    @GET("lookup")
    suspend fun getSongs(@Query("id", encoded = true) artist_ids: String,
                         @Query("limit") limit: Int = 5): String
}

object ShuffleSongsApi {
    val retrofitService: ShuffleSongsApiService by lazy { retrofit.create(ShuffleSongsApiService::class.java) }
}