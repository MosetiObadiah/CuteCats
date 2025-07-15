package com.moseti.cutecats.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moseti.cutecats.ui.viewmodels.CatViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun FavoritesPage(catViewModel: CatViewModel, modifier: Modifier =  Modifier) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Favorites"
        )
    }

}