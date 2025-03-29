package com.example.hymnal.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun BottomNavigationBar(navController: NavHostController)  {
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(MaterialTheme.colorScheme.surfaceContainer)

    val routes = listOf(
        Route("Hymns", Icons.Rounded.Home, Icons.Outlined.Home),
        Route("Favourites", Icons.Rounded.Favorite, Icons.Outlined.FavoriteBorder),
        Route("About", Icons.Rounded.Info, Icons.Outlined.Info)
    )

    NavigationBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        routes.forEach { route ->
            val selected = currentDestination?.route == route.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) route.selectedIcon else route.unselectedIcon,
                        contentDescription = route.route
                    )
                },
                label = { Text(route.route) },
                selected = selected,
                onClick = { navController.navigate(route.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                } }
            )
        }
    }
}

data class Route(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
