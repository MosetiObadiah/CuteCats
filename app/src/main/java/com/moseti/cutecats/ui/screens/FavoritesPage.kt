package com.moseti.cutecats.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
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
import com.moseti.cutecats.ui.CatCard
import com.moseti.cutecats.ui.viewmodels.CatViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun FavoritesPage(
    catViewModel: CatViewModel,
    modifier: Modifier = Modifier
) {
    // Collect the StateFlow from the ViewModel. This comes directly from the database.
    val favoriteCats by catViewModel.favoriteCats.collectAsState()

    if (favoriteCats.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("You haven't liked any cats yet!")
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(190.dp),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(favoriteCats, key = { it.id }) { cat ->
                // The CatCard here doesn't need the like button, or it could be used for un-liking
                CatCard(
                    catImage = cat,
                    isFavorite = true, // All cats on this page are favorites
                    onFavoriteClick = { /* Decide if you want to un-favorite from here */ }
                )
            }
        }
    }
}