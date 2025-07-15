package com.moseti.cutecats.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moseti.cutecats.ui.viewmodels.CatViewModel

@Composable
fun UploadsPage(catViewModel: CatViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Upload an Image of your Cat \nAI will be used to verify if the image you've uploaded is a cat image"
        )
        // Todo, use AI to verify if it is a valid cat image
    }
}