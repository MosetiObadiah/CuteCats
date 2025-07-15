package com.moseti.cutecats.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moseti.cutecats.network.CatImage
import kotlinx.coroutines.flow.Flow

@Dao
interface CatImageDao {
    /**
     * Inserts a list of cat images into the database. If a cat image with the
     * same primary key already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(catImages: List<CatImage>)

    /**
     * Retrieves all cat images from the database as a Flow.
     * The Flow will automatically emit a new list whenever the data changes.
     */
    @Query("SELECT * FROM cat_images")
    fun getAllCats(): Flow<List<CatImage>>

    /**
     * Deletes all cat images from the table.
     */
    @Query("DELETE FROM cat_images")
    suspend fun clearAll()
}