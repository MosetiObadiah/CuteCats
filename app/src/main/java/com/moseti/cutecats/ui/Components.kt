package com.moseti.cutecats.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import com.moseti.cutecats.R
import com.moseti.cutecats.network.CatImage
import com.moseti.cutecats.ui.viewmodels.CatViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun CatCard(
    catViewModel: CatViewModel,
    catImage: CatImage,
    isFavorite: Boolean,
    onFavoriteClick: (CatImage) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier.clickable(
            enabled = true,
            onClickLabel = "Like the cat",
            onClick = {
                onFavoriteClick(catImage)
            }
        )
    ) {
        val context = LocalContext.current

        Box {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(catImage.url)
                    .crossfade(true) // Smooth transition when image loads
                    .size(Size(300, 300)) // Approximate size to reduce memory usage
                    .memoryCachePolicy(CachePolicy.ENABLED) // memory caching
                    .diskCachePolicy(CachePolicy.ENABLED) // disk caching
                    .build(),
                contentDescription = "Cat image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.74f) // consistent aspect ratio
                    .background(Color.Gray), // Debug background
                placeholder = painterResource(R.drawable.icons8_cat_100),
                error = painterResource(R.drawable.error_svgrepo_com),
                onError = { Log.e("CatCard", "Failed to load image: ${catImage.url}") }
            )

            Icon(
                imageVector = if (catViewModel.isFavorite(catImage)) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color.Red else Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clickable { onFavoriteClick(catImage) }
            )
        }
    }
}