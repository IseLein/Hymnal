package com.example.hymnal.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hymnal.data.HymnRepository
import com.example.hymnal.data.HymnsViewModel
import com.example.hymnal.data.HymnsViewModelFactory

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HymnsScreen(
    viewModel: HymnsViewModel = viewModel(factory = HymnsViewModelFactory(HymnRepository(LocalContext.current))),
    searchQuery: String,
) {
    val hymns = viewModel.hymns.collectAsState()
    val filteredHymns = remember (searchQuery, hymns.value) {
        hymns.value.filter { hymn ->
            hymn.title.contains(searchQuery, ignoreCase = true)
                || hymn.hymn.toString().contains(searchQuery, ignoreCase = true)
                || hymn.author.contains(searchQuery, ignoreCase = true)
                || hymn.verses.any { verse -> verse.contains(searchQuery, ignoreCase = true) }
                || hymn.chorus.any { chorus -> chorus.contains(searchQuery, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        items(filteredHymns) { hymn ->
            Text(hymn.hymn.toString() + ". " + hymn.title)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HymnsSearchBar(query: String, onQueryChange: (String) -> Unit) {
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {},
                expanded = false,
                onExpandedChange = {},
                placeholder = { Text("Search") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = if (query.isNotEmpty()) {
                    {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                } else null
            )
        },
        expanded = false,
        onExpandedChange = {},
        content = {},
        shape = RoundedCornerShape(99.dp),
    )
}
