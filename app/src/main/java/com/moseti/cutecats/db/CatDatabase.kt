// In package: com.moseti.cutecats.db
package com.moseti.cutecats.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moseti.cutecats.network.CatImage

@Database(
    entities = [CatImage::class],
    version = 1, // Increment this when you change the schema
    exportSchema = false // Good practice for simple apps
)
@TypeConverters(BreedListConverter::class) // Register your converter here
abstract class CatDatabase : RoomDatabase() {

    abstract fun catImageDao(): CatImageDao

    companion object {
        // The @Volatile annotation ensures that the INSTANCE variable is always up-to-date
        // and visible to all execution threads.
        @Volatile
        private var INSTANCE: CatDatabase? = null

        fun getDatabase(context: Context): CatDatabase {
            // Return the existing instance if it's not null.
            // If it is, create the database in a synchronized block to avoid race conditions.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CatDatabase::class.java,
                    "cat_database" // Name of the database file
                )
                    // You can add migrations here if you change the schema later.
                    .build()
                INSTANCE = instance
                // Return the newly created instance
                instance
            }
        }
    }
}