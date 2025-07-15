package com.moseti.cutecats.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moseti.cutecats.ui.CatCard
import com.moseti.cutecats.ui.viewmodels.CatUiState
import com.moseti.cutecats.ui.viewmodels.CatViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun HomePage(catViewModel: CatViewModel, modifier: Modifier = Modifier) {
    val gridStaggeredState = rememberLazyStaggeredGridState()

    LaunchedEffect(Unit) {
        if ((catViewModel.catsUiState as? CatUiState.Success)?.photos.isNullOrEmpty()) {
            catViewModel.loadNextPage()
        }
    }

    val catImages = catViewModel.catImages

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (catViewModel.catsUiState) {
            is CatUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is CatUiState.Error -> {
                Text(
                    text = "Error loading images",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is CatUiState.Success -> {
                LazyVerticalStaggeredGrid(
                    state = gridStaggeredState,
                    columns = StaggeredGridCells.Adaptive(190.dp),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    content = {
                        items(catImages) { catImage ->
                            Log.d("CatCard", "Loading image: ${catImage.url}")
                            CatCard(
                                catImage = catImage,
                                isFavorite = catViewModel.isFavorite(catImage),
                                onFavoriteClick = { catViewModel.toggleFavorite(it) }
                            )
                        }
                        item {
                            if (catViewModel.catImages.isNotEmpty() && !catViewModel.isLoading) {
                                LaunchedEffect(catViewModel.catImages.size) {
                                    catViewModel.loadNextPage()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}