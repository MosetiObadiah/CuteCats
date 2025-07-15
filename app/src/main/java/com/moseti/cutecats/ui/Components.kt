package com.moseti.cutecats.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.moseti.cutecats.R

@Composable
fun CustomFilterChip(label: String) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        onClick = { selected = !selected },
        label = {
            Text(label)
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
        modifier = Modifier.padding(end = 3.dp)
    )
}

@Composable
fun CatCard(imageUrl: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .padding(2.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Translated description of what the image contains",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            placeholder = painterResource(R.drawable.image_file_svgrepo_com__1_),
            error = painterResource(R.drawable.error_svgrepo_com)
        )

//        IconButton(
//            onClick = {}
//        ) {
//            Icon(
//                imageVector = Icons.Outlined.ThumbUp,
//                contentDescription = "Localized description"
//            )
//        }
//
//        IconButton(
//            onClick = {}
//        ) {
//            Icon(
//                imageVector = Icons.Outlined.Favorite,
//                contentDescription = "Localized description"
//            )
//        }
    }
}