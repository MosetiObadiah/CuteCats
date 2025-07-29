package com.moseti.cutecats.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.moseti.cutecats.R
import com.moseti.cutecats.data.remote.dto.CatImage

/**
 * A reusable Composable to display a single cat image in a card.
 *
 * @param catImage The data object for the cat.
 * @param isFavorite Whether this cat is currently in the user's favorites.
 * @param onFavoriteClick A lambda to be invoked when the favorite icon is clicked.
 * @param onCardClick A lambda to be invoked when the card itself is clicked.
 * @param modifier The modifier to be applied to this Composable.
 */
@Composable
fun CatCard(
    catImage: CatImage,
    isFavorite: Boolean,
    onFavoriteClick: (CatImage) -> Unit,
    onCardClick: (CatImage) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clip(CardDefaults.shape),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onCardClick(catImage) }
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(catImage.url)
                    .crossfade(true) // Smooth fade-in animation
                    .size(512)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = "A cute cat from theCatAPI",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(catImage.width?.toFloat()?.div(catImage.height?.toFloat() ?: 1f) ?: 1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                loading = {
                    // Show a circular progress indicator while loading.
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    // Show an error icon if the image fails to load.
                    Log.e("CatCard", "Failed to load image: ${catImage.url}")
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(painterResource(id = R.drawable.error_svgrepo_com), contentDescription = "Error")
                    }
                }
            )

            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
                tint = if (isFavorite) Color.Red else Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.3f), shape = RoundedCornerShape(50))
                    .padding(6.dp)
                    .clickable { onFavoriteClick(catImage) }
            )
        }
    }
}