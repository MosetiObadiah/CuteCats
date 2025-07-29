package com.moseti.cutecats.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moseti.cutecats.data.remote.dto.CatImage

/**
 * The Room database for the application.
 * Hilt provides the singleton instance of this database.
 */
@Database(
    entities = [CatImage::class, UserUpload::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CatDatabase : RoomDatabase() {
    abstract fun catImageDao(): CatImageDao

    abstract fun userUploadDao(): UserUploadDao
}