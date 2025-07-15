package com.moseti.cutecats.db

import androidx.room.TypeConverter
import com.moseti.cutecats.network.Breed
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BreedListConverter {
    @TypeConverter
    fun fromBreedList(breeds: List<Breed>): String {
        // Convert the list of Breed objects into a single JSON string
        return Json.encodeToString(breeds)
    }

    @TypeConverter
    fun toBreedList(breedsJson: String): List<Breed> {
        // Convert the JSON string back into a list of Breed objects
        return Json.decodeFromString(breedsJson)
    }
}