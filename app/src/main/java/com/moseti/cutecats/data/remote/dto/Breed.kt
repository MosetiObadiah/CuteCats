package com.moseti.cutecats.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Breed(
    val name: String,
    val temperament: String? = null,
    val origin: String? = null
)