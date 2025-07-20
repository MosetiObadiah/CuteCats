package com.moseti.cutecats.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moseti.cutecats.network.CatImage
import com.moseti.cutecats.network.CatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface CatUiState {
    data class Success(val photos: List<CatImage>) : CatUiState
    data object Error : CatUiState
    data object Loading : CatUiState
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class CatViewModel(private val repository: CatRepository) : ViewModel() {

    var catsUiState: CatUiState by mutableStateOf(CatUiState.Loading)
        private set
    private val _networkCatImages = mutableStateListOf<CatImage>()
    val catImages: List<CatImage> get() = _networkCatImages

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds = _favoriteIds.asStateFlow()
    private val _sessionLikedIds = mutableSetOf<String>()

    init {
        viewModelScope.launch {
            repository.favoriteCats.collect { dbFavorites ->
                _sessionLikedIds.addAll(dbFavorites.map { it.id })
                _favoriteIds.value = _sessionLikedIds.toSet() // Trigger state update
            }
        }
    }

    fun toggleFavorite(cat: CatImage) {
        if (_sessionLikedIds.contains(cat.id)) {
            _sessionLikedIds.remove(cat.id)
        } else {
            _sessionLikedIds.add(cat.id)
        }

        _favoriteIds.value = _sessionLikedIds.toSet()

        viewModelScope.launch {
            val currentFavorites = _networkCatImages.filter { it.id in _sessionLikedIds }
            repository.saveFavorites(currentFavorites)
        }

        catsUiState = CatUiState.Success(_networkCatImages.toList())
    }

    fun isFavorite(cat: CatImage): Boolean {
        return _favoriteIds.value.contains(cat.id)
    }

    val favoriteCats: StateFlow<List<CatImage>> = repository.favoriteCats
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Stay active 5s after UI is gone
            initialValue = emptyList()
        )

    private var currentPage = 0
    var isLoading = false
        private set

    // Fetch cat images from network
    fun loadNextPage() {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            try {
                val newImages = repository.fetchCatImagesFromNetwork(currentPage)
                _networkCatImages.addAll(newImages)
                catsUiState = CatUiState.Success(_networkCatImages.toList())
                currentPage++
            } catch (e: Exception) {
                Log.e("CatViewModel", "Error fetching cat images", e)
                catsUiState = CatUiState.Error
            } finally {
                isLoading = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("CatViewModel", "onCleared called. Saving favorites...")
        viewModelScope.launch {
            // Filter the master list of network images to get the full objects of our liked IDs
            val finalFavorites = _networkCatImages.filter { it.id in _sessionLikedIds }
            if (finalFavorites.isNotEmpty()) {
                repository.saveFavorites(finalFavorites)
                Log.d("CatViewModel", "Saved ${finalFavorites.size} cats to the database.")
            }
        }
    }
}

class CatViewModelFactory(private val repository: CatRepository) : ViewModelProvider.Factory {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}