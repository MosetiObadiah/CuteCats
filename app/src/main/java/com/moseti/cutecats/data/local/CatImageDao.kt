package com.moseti.cutecats.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moseti.cutecats.data.remote.dto.CatImage
import kotlinx.coroutines.flow.Flow

@Dao
interface CatImageDao {
    /**
     * Inserts a cat image into the favorites. If it already exists, it's replaced.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(catImage: CatImage)

    /**
     * Deletes a specific cat image from the favorites.
     */
    @Delete
    suspend fun delete(catImage: CatImage)

    /**
     * Retrieves all favorite cat images from the database as a Flow.
     * The Flow automatically emits a new list whenever the favorite cats data changes.
     */
    @Query("SELECT * FROM favorite_cats ORDER BY id DESC")
    fun getAllFavoriteCats(): Flow<List<CatImage>>

    /**
     * Retrieves a set of all favorite cat IDs as a Flow.
     * This is highly efficient for checking if a cat is a favorite without fetching the whole object.
     */
    @Query("SELECT id FROM favorite_cats")
    fun getAllFavoriteCatIds(): Flow<List<String>>
}