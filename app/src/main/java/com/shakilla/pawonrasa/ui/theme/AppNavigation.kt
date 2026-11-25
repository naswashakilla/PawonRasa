package com.shakilla.pawonrasa.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shakilla.pawonrasa.R
import com.shakilla.pawonrasa.viewmodel.SavedViewModel
import com.shakilla.pawonrasa.viewmodel.SettingsViewModel

private data class Tab(val route: String, val icon: ImageVector, val label: Int)

@Composable
fun AppNavigation(settings: SettingsViewModel, saved: SavedViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route

    val tabs = listOf(
        Tab("home", Icons.Default.Home, R.string.nav_home),
        Tab("favorites", Icons.Default.Favorite, R.string.nav_favorites),
        Tab("collections", Icons.Default.Bookmark, R.string.nav_collections),
        Tab("settings", Icons.Default.Settings, R.string.nav_settings)    )
    val showBottomBar = tabs.any { it.route == route }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = route == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo("home")
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(stringResource(tab.label)) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    onMealClick = { id -> navController.navigate("detail/$id") },
                    contentPadding = innerPadding
                )
            }
            composable("favorites") {
                SavedScreen(
                    title = stringResource(R.string.favorites_title),
                    emptyText = stringResource(R.string.favorites_empty),
                    items = saved.items.filter { it.isFavorite },
                    showCategoryFilter = false,
                    onMealClick = { id -> navController.navigate("detail/$id") },
                    contentPadding = innerPadding
                )
            }
            composable("collections") {
                SavedScreen(
                    title = stringResource(R.string.collections_title),
                    emptyText = stringResource(R.string.collections_empty),
                    items = saved.items.filter { it.inCollection },
                    showCategoryFilter = true,
                    onMealClick = { id -> navController.navigate("detail/$id") },
                    contentPadding = innerPadding
                )
            }
            composable("settings") {
                SettingsScreen(settings = settings, saved = saved, contentPadding = innerPadding)
            }
            composable(
                route = "detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { entry ->
                DetailScreen(
                    id = entry.arguments?.getString("id").orEmpty(),
                    onBack = { navController.popBackStack() },
                    saved = saved,
                    contentPadding = innerPadding
                )
            }
        }
    }
}