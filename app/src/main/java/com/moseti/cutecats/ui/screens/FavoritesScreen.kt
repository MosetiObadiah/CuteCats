package com.moseti.cutecats.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moseti.cutecats.data.remote.dto.CatImage
import com.moseti.cutecats.ui.components.CatCard
import com.moseti.cutecats.ui.viewmodels.CatViewModel

@Composable
fun FavoritesScreen(
    catViewModel: CatViewModel,
    onCatClicked: (CatImage) -> Unit,
    modifier: Modifier = Modifier
) {
    // viewModel provides a direct flow of favorite cats from the database.
    val favoriteCats by catViewModel.favoriteCats.collectAsState()

    if (favoriteCats.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("You haven't liked any cats yet!")
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(180.dp),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favoriteCats, key = { it.id }) { cat ->
                CatCard(
                    catImage = cat,
                    isFavorite = true, // All cats on this screen are favorites
                    onFavoriteClick = { catViewModel.toggleFavorite(it) }, // This will remove it
                    onCardClick = { onCatClicked(it) }
                )
            }
        }
    }
}