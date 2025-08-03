package com.moseti.cutecats.ui.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moseti.cutecats.data.CatRepository
import com.moseti.cutecats.data.local.UserUpload
import com.moseti.cutecats.data.remote.dto.CatImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CatViewModel @Inject constructor(
    private val repository: CatRepository,
    application: Application
) : AndroidViewModel(application) {

    // This will hold ALL images fetched from the network, our source of truth.
    private var allNetworkImages = mutableListOf<CatImage>()

    // A private mutable state flow that will be updated within the ViewModel.
    private val _uiState = MutableStateFlow(CatUiState())
    // A public, read-only state flow that the UI can collect to observe state changes.
    val uiState: StateFlow<CatUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private var fetchJob: Job? = null

    fun toggleSearchVisibility() {
        _uiState.update { it.copy(isSearchVisible = !it.isSearchVisible) }
        // If we're hiding the search bar, clear the query and show all images
        if (!_uiState.value.isSearchVisible) {
            onSearchQueryChanged("")
        }
    }

    /** Updates the search query and filters the list of images. */
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterImages()
    }

    /** Private helper to apply the current search query to the master list. */
    private fun filterImages() {
        val query = _uiState.value.searchQuery
        val filteredList = if (query.isBlank()) {
            allNetworkImages
        } else {
            allNetworkImages.filter { cat ->
                // Search in breed names. The '?.' makes it safe if breeds list is null or empty.
                cat.breeds.any { breed ->
                    breed.name.contains(query, ignoreCase = true)
                }
            }
        }
        _uiState.update { it.copy(images = filteredList) }
    }

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
                // Add new images to our master list
                allNetworkImages.addAll(newImages)

                filterImages()

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

    // A flow of the user's personal uploads from the database.
    val myUploads: StateFlow<List<UserUpload>> = repository.getMyUploads()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    /**
     * Creates a UserUpload object from a URI and saves it to the database.
     */
    /**
     * Takes a temporary content URI, copies its data to a permanent file in the app's
     * internal storage, and then saves the path to that file in the database.
     */
    fun submitUserUpload(imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Now we can use getApplication() which comes from AndroidViewModel
                val app = getApplication<Application>()
                val uploadsDir = File(app.filesDir, "uploads")
                if (!uploadsDir.exists()) {
                    uploadsDir.mkdirs()
                }
                val destinationFile = File(uploadsDir, "upload_${System.currentTimeMillis()}.jpg")

                app.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    FileOutputStream(destinationFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }

                val newUserUpload = UserUpload(
                    filePath = destinationFile.absolutePath,
                    timestamp = System.currentTimeMillis()
                )
                repository.saveMyUpload(newUserUpload)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}