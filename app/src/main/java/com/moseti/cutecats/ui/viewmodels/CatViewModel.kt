package com.moseti.cutecats.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moseti.cutecats.data.CatRepository
import com.moseti.cutecats.data.remote.dto.CatImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatViewModel @Inject constructor(
    private val repository: CatRepository
) : ViewModel() {

    // A private mutable state flow that will be updated within the ViewModel.
    private val _uiState = MutableStateFlow(CatUiState())
    // A public, read-only state flow that the UI can collect to observe state changes.
    val uiState: StateFlow<CatUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private var fetchJob: Job? = null

    // A flow of favorite cat IDs from the database. It's highly efficient.
    val favoriteCatIds: StateFlow<Set<String>> = repository.getFavoriteCatIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // A flow of the full favorite cat objects for the Favorites screen.
    val favoriteCats: StateFlow<List<CatImage>> = repository.getFavoriteCats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    init {
        // Loads the first page of cats when the ViewModel is created.
        loadNextPage(isInitialLoad = true)
    }

    /**
     * Loads the next page of cat images from the repository.
     * It prevents multiple simultaneous requests.
     */
    fun loadNextPage(isInitialLoad: Boolean = false) {
        // Prevent new loads if a load is already in progress, or if we've reached the end.
        if (fetchJob?.isActive == true || _uiState.value.endReached) return

        fetchJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingInitial = isInitialLoad,
                    isLoadingMore = !isInitialLoad,
                    error = null
                )
            }
            try {
                val newImages = repository.getNetworkCatImages(page = currentPage, limit = 20)
                _uiState.update { currentState ->
                    currentState.copy(
                        images = currentState.images + newImages,
                        isLoadingInitial = false,
                        isLoadingMore = false,
                        endReached = newImages.isEmpty() // If API returns empty, we're at the end.
                    )
                }
                if (newImages.isNotEmpty()) {
                    currentPage++
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Failed to load cats: ${e.message}",
                        isLoadingInitial = false,
                        isLoadingMore = false
                    )
                }
            }
        }
    }

    /**
     * Toggles the favorite status of a cat.
     * It checks if the cat is currently a favorite and either adds or removes it from the database.
     * The UI will update automatically because it observes the `favoriteCatIds` flow.
     */
    fun toggleFavorite(cat: CatImage) {
        viewModelScope.launch {
            if (favoriteCatIds.value.contains(cat.id)) {
                repository.removeFavorite(cat)
            } else {
                repository.addFavorite(cat)
            }
        }
    }
}