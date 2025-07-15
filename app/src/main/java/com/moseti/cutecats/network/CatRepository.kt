package com.moseti.cutecats.network

class CatRepository(private val apiService: CatApiService) {
    suspend fun fetchCatImages(page: Int): List<CatImage> {
        return apiService.getCatImages(page = page, limit = 25)
    }
}