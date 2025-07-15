package com.moseti.cutecats.network

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "cat_images")
@Serializable
data class CatImage(
    @PrimaryKey
    val id: String,
    val url: String,
    val breeds: List<Breed>? = null,
    val width: Int?,
    val height: Int?
)


@Serializable
data class Breed(
    val name: String
)