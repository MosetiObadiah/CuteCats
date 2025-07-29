package com.moseti.cutecats.data

import com.moseti.cutecats.data.local.CatImageDao
import com.moseti.cutecats.data.local.UserUpload
import com.moseti.cutecats.data.local.UserUploadDao
import com.moseti.cutecats.data.remote.CatApiService
import com.moseti.cutecats.data.remote.dto.CatImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatRepository @Inject constructor(
    private val apiService: CatApiService,
    private val catImageDao: CatImageDao,
    private val userUploadDao: UserUploadDao
) {

    /**
     * Exposes a flow of all user-uploaded images from the local database.
     */
    fun getMyUploads(): Flow<List<UserUpload>> = userUploadDao.getAllUploads()

    /**
     * Saves a user upload record to the local database.
     */
    suspend fun saveMyUpload(upload: UserUpload) {
        userUploadDao.insert(upload)
    }

    /**
     * Fetches a paginated list of cat images from the remote API.
     */
    suspend fun getNetworkCatImages(page: Int, limit: Int): List<CatImage> {
        return apiService.getCatImages(page = page, limit = limit)
    }

    /**
     * Exposes a flow of all favorite cats from the local database.
     */
    fun getFavoriteCats(): Flow<List<CatImage>> = catImageDao.getAllFavoriteCats()

    /**
     * Exposes a flow of just the IDs of all favorite cats as a Set.
     * It gets a list from the DAO and transforms it into a set. This abstracts
     * the data source implementation detail from the rest of the app.
     */
    fun getFavoriteCatIds(): Flow<Set<String>> = catImageDao.getAllFavoriteCatIds()
        .map { listOfIds -> listOfIds.toSet() }

    /**
     * Adds a cat to the local favorites database.
     */
    suspend fun addFavorite(cat: CatImage) {
        catImageDao.insert(cat)
    }

    /**
     * Removes a cat from the local favorites database.
     */
    suspend fun removeFavorite(cat: CatImage) {
        catImageDao.delete(cat)
    }
}