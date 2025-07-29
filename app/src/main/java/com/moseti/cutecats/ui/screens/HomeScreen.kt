package com.moseti.cutecats.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moseti.cutecats.data.remote.dto.CatImage
import com.moseti.cutecats.ui.components.CatCard
import com.moseti.cutecats.ui.viewmodels.CatViewModel

@Composable
fun HomeScreen(
    catViewModel: CatViewModel,
    onCatClicked: (CatImage) -> Unit
) {
    val uiState by catViewModel.uiState.collectAsState()
    val favoriteIds by catViewModel.favoriteCatIds.collectAsState()
    val gridState = rememberLazyStaggeredGridState()

    // A derived state to check if we are near the end of the list.
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = layoutInfo.totalItemsCount
            // Load more when the user is 5 items away from the end.
            !uiState.isLoadingMore && !uiState.endReached && totalItems > 0 && lastVisibleItemIndex >= totalItems - 5
        }
    }

    // triggers loading the next page when shouldLoadMore becomes true.
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            catViewModel.loadNextPage()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Add the AnimatedVisibility composable for the search bar
        AnimatedVisibility(
            visible = uiState.isSearchVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChanged = { catViewModel.onSearchQueryChanged(it) }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoadingInitial) {
                // Full-screen loading indicator for the initial load.
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null && uiState.images.isEmpty()) {
                // Error message and retry button if the initial load fails.
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { catViewModel.loadNextPage(isInitialLoad = true) }) {
                        Text("Retry")
                    }
                }
            } else {
                // main content grid.
                LazyVerticalStaggeredGrid(
                    state = gridState,
                    columns = StaggeredGridCells.Adaptive(180.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(uiState.images, key = { _, cat -> cat.id }) { index, catImage ->
                        CatCard(
                            catImage = catImage,
                            isFavorite = favoriteIds.contains(catImage.id),
                            onFavoriteClick = { catViewModel.toggleFavorite(it) },
                            onCardClick = { onCatClicked(it) }
                        )
                    }

                    // Shows a loading indicator at the bottom while paginating.
                    if (uiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Search by breed...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            // Show a clear button only if there's text
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                }
            }
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}