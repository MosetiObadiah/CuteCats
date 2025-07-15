package com.moseti.cutecats.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moseti.cutecats.network.CatApiService
import com.moseti.cutecats.network.CatImage
import com.moseti.cutecats.network.CatRepository
import com.moseti.cutecats.network.RetrofitClient
import kotlinx.coroutines.launch

sealed interface CatUiState {
    data class Success(val photos: List<CatImage>) : CatUiState
    object Error : CatUiState
    object Loading : CatUiState
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class CatViewModel : ViewModel() {
    var catsUiState: CatUiState by mutableStateOf(CatUiState.Loading)
        private set


    private val _catImages = mutableStateListOf<CatImage>()
    val catImages: List<CatImage> get() = _catImages

    private var currentPage = 0
    var isLoading = false
        private set

    // ApiService instance and the Repository
    private val apiService = RetrofitClient.retrofit.create(CatApiService::class.java)
    private val repository = CatRepository(apiService)

    // fetch cat images and handle pagination
    fun loadNextPage() {
        if (isLoading) return // Prevent multiple simultaneous requests

        viewModelScope.launch {
            isLoading = true
            try {
                val newImages = repository.fetchCatImages(currentPage)
                _catImages.addAll(newImages)
                catsUiState = CatUiState.Success(_catImages)
                Log.d("CatViewModel", "Fetched ${newImages.size} images, total: ${_catImages.size}")
                currentPage++
            } catch (e: Exception) {
                Log.e("CatViewModel", "Error fetching cat images", e)
                catsUiState = CatUiState.Error
            } finally {
                isLoading = false
            }
        }
    }

    val filterChips = listOf(
        "Yellow",
        "Cute",
        "Black",
        "Fluffy"
    )

}