package com.moseti.cutecats.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.moseti.cutecats.BuildConfig
import com.moseti.cutecats.data.CatRepository
import com.moseti.cutecats.data.local.CatDatabase
import com.moseti.cutecats.data.local.CatImageDao
import com.moseti.cutecats.data.remote.CatApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // dependencies will live as long as the application.
object AppModule {

    /**
     * Provides a singleton instance of OkHttpClient.
     * Includes a logging interceptor for debugging network requests in debug builds.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(loggingInterceptor)
                }
            }
            .build()
    }

    /**
     * Provides a singleton instance of the CatApiService using Retrofit.
     */
    @Provides
    @Singleton
    fun provideCatApiService(okHttpClient: OkHttpClient): CatApiService {
        val json = Json {
            ignoreUnknownKeys = true // Safely ignore properties from the API that are not in our data classes.
        }
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CatApiService::class.java)
    }

    /**
     * Provides a singleton instance of the Room database.
     */
    @Provides
    @Singleton
    fun provideCatDatabase(@ApplicationContext context: Context): CatDatabase {
        return Room.databaseBuilder(
            context,
            CatDatabase::class.java,
            "cat_database"
        ).build()
    }

    /**
     * Provides the CatImageDao from the CatDatabase.
     */
    @Provides
    @Singleton
    fun provideCatImageDao(database: CatDatabase): CatImageDao {
        return database.catImageDao()
    }

    /**
     * Provides the CatRepository. Hilt will automatically provide the apiService and dao dependencies.
     */
    @Provides
    @Singleton
    fun provideCatRepository(apiService: CatApiService, dao: CatImageDao): CatRepository {
        return CatRepository(apiService, dao)
    }
}