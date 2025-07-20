package com.moseti.cutecats.data.remote.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for a cat image from theCatAPI.
 * This class is also used as the Room @Entity for the database, which is acceptable
 * for simple apps. In larger apps, you might create a separate domain model.
 *
 * @param id The unique identifier for the cat image, used as the primary key.
 * @param url The URL of the image.
 * @param breeds A list of breeds associated with the image.
 * @param width The width of the image.
 * @param height The height of the image.
 */
@Entity(tableName = "favorite_cats")
@Serializable
data class CatImage(
    @PrimaryKey
    val id: String,
    val url: String,
    val breeds: List<Breed> = emptyList(), // default value provided for stability
    val width: Int?,
    val height: Int?
)