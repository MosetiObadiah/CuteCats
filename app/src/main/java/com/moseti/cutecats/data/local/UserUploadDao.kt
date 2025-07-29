package com.moseti.cutecats.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserUploadDao {
    /**
     * Inserts a user upload record into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(upload: UserUpload)

    /**
     * Retrieves all user uploads from the database, ordered by the newest first.
     * Returns a Flow that automatically updates the UI when the data changes.
     */
    @Query("SELECT * FROM user_uploads ORDER BY timestamp DESC")
    fun getAllUploads(): Flow<List<UserUpload>>
}