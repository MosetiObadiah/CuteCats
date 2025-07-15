package com.moseti.cutecats.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.moseti.cutecats.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

object RetrofitClient {
    private const val BASE_URL = "https://api.thecatapi.com/"

    // logging for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}

interface CatApiService {
    @GET("v1/images/search")
    suspend fun getCatImages(
        @Header("x-api-key") apiKey: String = BuildConfig.THE_CAT_API_KEY,
        @Query("size") size: String = "med",
        @Query("mime_types") mimeTypes: String = "jpg",
        @Query("format") format: String = "json",
        @Query("has_breeds") hasBreeds: Boolean = true,
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10
    ): List<CatImage>
}