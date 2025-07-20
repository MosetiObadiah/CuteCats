package com.moseti.cutecats.data.remote

import com.moseti.cutecats.BuildConfig
import com.moseti.cutecats.data.remote.dto.CatImage
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CatApiService {
    @GET("v1/images/search")
    suspend fun getCatImages(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 25,
        @Header("x-api-key") apiKey: String = BuildConfig.THE_CAT_API_KEY,
        @Query("size") size: String = "med",
        @Query("has_breeds") hasBreeds: Boolean = true,
        @Query("mime_types") mimeTypes: String = "jpg,png"
    ): List<CatImage>
}