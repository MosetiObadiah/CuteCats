package com.moseti.cutecats.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.moseti.cutecats.ui.screens.FavoritesScreen
import com.moseti.cutecats.ui.screens.HomeScreen
import com.moseti.cutecats.ui.screens.SettingsScreen
import com.moseti.cutecats.ui.screens.UploadsScreen
import com.moseti.cutecats.ui.viewmodels.CatViewModel
import kotlinx.serialization.Serializable

@Serializable object Home
@Serializable object Favorites
@Serializable object Uploads
@Serializable object Settings

@Composable
fun AppNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    catViewModel: CatViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable<Home> {
            HomeScreen(
                catViewModel = catViewModel,
                onCatClicked = { /* TODO Handle cat detail navigation */ }
            )
        }
        composable<Favorites> {
            FavoritesScreen(
                catViewModel = catViewModel,
                onCatClicked = { /*TODO Handle cat detail navigation */ }
            )
        }
        composable<Uploads> {
            UploadsScreen(catViewModel)
        }
        composable<Settings> {
            SettingsScreen(catViewModel )
        }
    }
}