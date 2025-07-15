package com.moseti.cutecats.network

import com.moseti.cutecats.db.CatImageDao
import kotlinx.coroutines.flow.Flow

class CatRepository(
    private val apiService: CatApiService,
    private val catImageDao: CatImageDao
) {
    suspend fun fetchCatImagesFromNetwork(page: Int): List<CatImage> {
        return apiService.getCatImages(page = page, limit = 25)
    }

    val favoriteCats: Flow<List<CatImage>> = catImageDao.getAllCats()
    suspend fun saveFavorites(cats: List<CatImage>) {
        catImageDao.insertAll(cats)
    }
}