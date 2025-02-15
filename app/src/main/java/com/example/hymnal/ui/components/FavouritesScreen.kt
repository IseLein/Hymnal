package com.example.hymnal.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hymnal.data.HymnsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavouritesScreen(
    viewModel: HymnsViewModel,
    toggleFavourite: (String) -> Unit,
) {
    val hymnData = viewModel.hymnState.collectAsState()
    val filteredHymns = hymnData.value.filter { data -> data.isFavourite }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (filteredHymns.isEmpty()) {
            item {
                Text(
                    text = "There no favourite hymns",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                )
            }
        } else {
            items(filteredHymns) { hymnData ->
                HymnCard(hymnData, "", toggleFavourite)
            }
        }
    }
}