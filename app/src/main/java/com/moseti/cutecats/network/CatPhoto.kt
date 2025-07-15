package com.moseti.cutecats.network

import kotlinx.serialization.Serializable

@Serializable
data class CatImage(
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