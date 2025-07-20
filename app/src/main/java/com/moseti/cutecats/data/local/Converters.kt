package com.moseti.cutecats.data.local

import androidx.room.TypeConverter
import com.moseti.cutecats.data.remote.dto.Breed
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromBreedList(breeds: List<Breed>): String {
        return json.encodeToString(breeds)
    }

    @TypeConverter
    fun toBreedList(breedsJson: String): List<Breed> {
        // Handle cases where the JSON might be empty or invalid gracefully.
        return if (breedsJson.isEmpty()) {
            emptyList()
        } else {
            json.decodeFromString(breedsJson)
        }
    }
}