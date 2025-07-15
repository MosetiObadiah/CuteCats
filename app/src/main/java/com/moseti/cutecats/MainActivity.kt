package com.moseti.cutecats

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moseti.cutecats.ui.screens.FavoritesPage
import com.moseti.cutecats.ui.screens.HomePage
import com.moseti.cutecats.ui.screens.UploadsPage
import com.moseti.cutecats.ui.theme.CuteCatsTheme
import com.moseti.cutecats.ui.viewmodels.CatViewModel
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    private val catViewModel : CatViewModel by viewModels()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuteCatsTheme {
                val navController = rememberNavController()
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

                var selectedItemIndex by remember { mutableIntStateOf(0) }
                val navDestination  = listOf(Home, Uploads, Favorites)
                val items = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIconResId = R.drawable.home_filled_smile_svgrepo_com,
                        unselectedIconResId = R.drawable.home_outlined_smile_svgrepo_com

                    ),
                    BottomNavigationItem(
                        title = "Uploads",
                        selectedIconResId = R.drawable.upload_filled_svgrepo_com,
                        unselectedIconResId = R.drawable.upload_outlined_svgrepo_com__1_
                    ),
                    BottomNavigationItem(
                        title = "Favorites",
                        selectedIconResId = R.drawable.favorite_filled_svgrepo_com,
                        unselectedIconResId = R.drawable.favorite_outlined_svgrepo_com
                    )
                )

                Scaffold(
                    topBar = {
                        // TODO add search bar
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(
                                    text = "Cute Cats",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(Alignment.Start)
                                )
                            },
                            scrollBehavior = scrollBehavior,
                            actions = {
                                IconButton(
                                    onClick = {}
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_camera_alt_24),
                                        contentDescription = "Localized description"
                                    )
                                }

                                IconButton(
                                    onClick = {}
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Search,
                                        contentDescription = "Localized description"
                                    )
                                }

                                IconButton(
                                    onClick = {}
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_filter_list_24),
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                val isSelected = index == selectedItemIndex
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index
                                        navController.navigate(navDestination[index])
                                    },
                                    label = {
                                        Text(text = item.title)
                                    },
                                    alwaysShowLabel = false,
                                    icon = {
                                        Icon(
                                            modifier = Modifier.size(32.dp),
                                            painter = painterResource(id = if (isSelected) item.selectedIconResId else item.unselectedIconResId),
                                            contentDescription = item.title
                                        )
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = Home) {
                        composable<Home> {
                            HomePage(catViewModel, Modifier.padding(innerPadding))
                        }
                        composable<Favorites> {
                            FavoritesPage(catViewModel, Modifier.padding(innerPadding))
                        }
                        composable<Uploads> {
                            UploadsPage(catViewModel, Modifier.padding(innerPadding))
                        }
                    }
                }
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    @DrawableRes val selectedIconResId: Int,
    @DrawableRes val unselectedIconResId: Int
)

@Serializable
object Home

@Serializable
object Favorites

@Serializable
object Uploads