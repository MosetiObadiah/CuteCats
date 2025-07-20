package com.moseti.cutecats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moseti.cutecats.navigation.AppNavHost
import com.moseti.cutecats.navigation.Settings
import com.moseti.cutecats.ui.theme.CuteCatsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint // This enables field injection in this activity.
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuteCatsTheme {
                val navController = rememberNavController()

                // List of top-level navigation items
                val navItems = remember {
                    listOf(
                        BottomNavItem.Home,
                        BottomNavItem.Uploads,
                        BottomNavItem.Favorites
                    )
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("CuteCats") },
                            actions = {
                                IconButton(onClick = { /* TODO: Implement search */ }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_search_24),
                                        contentDescription = "Search"
                                    )
                                }
                                IconButton(onClick = { navController.navigate(Settings) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_settings_24),
                                        contentDescription = "Settings"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            navItems.forEach { item ->
                                val isSelected = currentDestination?.hierarchy?.any { it.route == item.route.toString() } == true
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            modifier = Modifier.size(32.dp),
                                            painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                                            contentDescription = stringResource(id = item.titleRes)
                                        )
                                    },
                                    label = { Text(stringResource(id = item.titleRes)) },
                                    selected = isSelected,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            // Pop up to the start destination of the graph to avoid building up a large back stack
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies of the same destination when re-selecting the same item
                                            launchSingleTop = true
                                            // Restore state when re-selecting a previously selected item
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    // Our centralized navigation logic is called here.
                    AppNavHost(navController = navController, paddingValues = innerPadding)
                }
            }
        }
    }
}

// A sealed class is a great way to define bottom navigation items for type safety.
sealed class BottomNavItem(
    val route: @Serializable Any,
    val titleRes: Int,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    data object Home : BottomNavItem(
        route = com.moseti.cutecats.navigation.Home,
        titleRes = R.string.bottom_nav_home,
        selectedIcon = R.drawable.home_filled_smile_svgrepo_com,
        unselectedIcon = R.drawable.home_outlined_smile_svgrepo_com
    )
    data object Uploads : BottomNavItem(
        route = com.moseti.cutecats.navigation.Uploads,
        titleRes = R.string.bottom_nav_uploads,
        selectedIcon = R.drawable.upload_filled_svgrepo_com,
        unselectedIcon = R.drawable.upload_outlined_svgrepo_com__1_
    )
    data object Favorites : BottomNavItem(
        route = com.moseti.cutecats.navigation.Favorites,
        titleRes = R.string.bottom_nav_favorites,
        selectedIcon = R.drawable.favorite_filled_svgrepo_com,
        unselectedIcon = R.drawable.favorite_outlined_svgrepo_com
    )
}