package com.niyas.shoppinglisttdd.data.remote

import com.niyas.shoppinglisttdd.BuildConfig
import com.niyas.shoppinglisttdd.data.remote.imageresponse.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.PIXABAY_KEY
    ): Response<ImageResponse>
}