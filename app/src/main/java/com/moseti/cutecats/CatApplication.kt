package com.moseti.cutecats

import android.app.Application
import com.moseti.cutecats.db.CatDatabase
import com.moseti.cutecats.network.CatApiService
import com.moseti.cutecats.network.CatRepository
import com.moseti.cutecats.network.RetrofitClient

class CatApplication : Application() {
    private val database by lazy { CatDatabase.getDatabase(this) }
    private val apiService by lazy { RetrofitClient.retrofit.create(CatApiService::class.java) }
    val repository by lazy { CatRepository(apiService, database.catImageDao()) }
}