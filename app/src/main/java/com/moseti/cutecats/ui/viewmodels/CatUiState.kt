package com.moseti.cutecats.ui.viewmodels

import com.moseti.cutecats.data.remote.dto.CatImage

/**
 * Represents the state of the home screen UI. It's a single source of truth.
 * Using a data class makes the state immutable, which is a best practice for Compose.
 *
 * @param images The list of cat images to display.
 * @param isLoadingInitial True when the very first page is being loaded.
 * @param isLoadingMore True when subsequent pages are being loaded (for pagination).
 * @param error An optional error message to display to the user.
 * @param endReached True if the API has no more images to provide.
 */
data class CatUiState(
    val images: List<CatImage> = emptyList(),
    val isLoadingInitial: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false,
    val isSearchVisible: Boolean = false,
    val searchQuery: String = ""
)