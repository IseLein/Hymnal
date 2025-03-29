package com.example.hymnal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hymnal.data.FavouriteHymnRepository
import com.example.hymnal.data.FavouriteRepository
import com.example.hymnal.data.HymnAudioViewModel
import com.example.hymnal.data.HymnRepository
import com.example.hymnal.data.HymnsViewModel
import com.example.hymnal.data.HymnsViewModelFactory
import com.example.hymnal.ui.components.BottomNavigationBar
import com.example.hymnal.ui.components.FavouritesScreen
import com.example.hymnal.ui.components.HymnDetailScreen
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

                val hymnRepository = HymnRepository(LocalContext.current)
                val favouriteRepository = FavouriteRepository(LocalContext.current)
                val favouriteHymnRepository = FavouriteHymnRepository(hymnRepository, favouriteRepository)
                val viewModelFactory = HymnsViewModelFactory(favouriteHymnRepository)
                val hymnsViewModel = ViewMoelProvider(this, viewModelFactory)
                    .get(HymnsViewModel::class.java)
                val audioViewModel = ViewModelProvider(this).get(HymnAudioViewModel::class.java)

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    topBar = { HymnalTopBar(navController, searchQuery, { searchQuery = it }, scrollBehavior) },
                    bottomBar = {
                        if (currentDestination?.route?.startsWith("HymnDetail/") != true) {
                            BottomNavigationBar(navController)
                        }
                    },
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Hymns",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("Hymns") { 
                            HymnsScreen(
                                hymnsViewModel,
                                searchQuery,
                                hymnsViewModel::toggleFavourite,
                                onHymnClick = { hymnId ->
                                    navController.navigate("HymnDetail/$hymnId")
                                }
                            )
                        }
                        composable("Favourites") { 
                            FavouritesScreen(
                                hymnsViewModel,
                                hymnsViewModel::toggleFavourite,
                                onHymnClick = { hymnId ->
                                    navController.navigate("HymnDetail/$hymnId")
                                }
                            )
                        }
                        composable(
                            route = "HymnDetail/{hymnId}",
                            arguments = listOf(
                                navArgument("hymnId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val hymnId = backStackEntry.arguments?.getString("hymnId") ?: return@composable
                            HymnDetailScreen(
                                hymnId = hymnId,
                                viewModel = hymnsViewModel,
                                audioViewModel = audioViewModel,
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }
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
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (currentDestination?.route == "Hymns") {
        CenterAlignedTopAppBar(
            title = { HymnsSearchBar(searchQuery, onQueryChange) },
            scrollBehavior = scrollBehavior,
            windowInsets = WindowInsets(
                top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                bottom = 10.dp
            )
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
