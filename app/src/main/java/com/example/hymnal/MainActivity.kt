package com.example.hymnal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hymnal.ui.components.BottomNavigationBar
import com.example.hymnal.ui.components.HymnsScreen
import com.example.hymnal.ui.components.HymnsSearchBar
import com.example.hymnal.ui.theme.HymnalTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HymnalTheme {
                val navController = rememberNavController()
                var searchQuery by remember { mutableStateOf("") }
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
                    rememberTopAppBarState()
                )

                Scaffold(
                    topBar = { HymnalTopBar(navController, searchQuery, { searchQuery = it }, scrollBehavior) },
                    bottomBar = { BottomNavigationBar(navController) },
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Hymns",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("Hymns") { HymnsScreen(searchQuery = searchQuery) }
                        composable("Favourites") { Text("Favourites") }
                        composable("Settings") { Text("Settings") }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HymnalTopBar(
    navController: NavHostController,
    searchQuery: String, onQueryChange: (String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination?.route == "Hymns") {
        CenterAlignedTopAppBar(
            title = { HymnsSearchBar(searchQuery, onQueryChange) },
            scrollBehavior = scrollBehavior,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    } else if (currentDestination?.route == "Favourites") {
        CenterAlignedTopAppBar(
            title = { Text("Favourites") },
        )
    } else if (currentDestination?.route == "Settings") {
        CenterAlignedTopAppBar(
            title = { Text("Settings") },
        )
    }
}
