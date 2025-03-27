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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hymnal.data.HymnsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HymnsScreen(
    viewModel: HymnsViewModel,
    searchQuery: String,
    toggleFavourite: (String) -> Unit,
    onHymnClick: (String) -> Unit
) {
    val hymnData = viewModel.hymnState.collectAsState()
    // keep a dictionary of the hymns with their ranking score based on the search query
    val hymnScores = remember (searchQuery, hymnData.value) {
        val scores = mutableMapOf<String, Int>()
        hymnData.value.forEach { data ->
            var score = 0

            if (searchQuery.isNotEmpty()) {
                if (data.hymn.hymn.toString().contains(searchQuery, ignoreCase = true)) {
                    score += 100
                }
                if (data.hymn.title.contains(searchQuery, ignoreCase = true)) {
                    score += 50
                }
                if (data.hymn.verses.any { verse ->
                        verse.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }) {
                    score += 10
                }
                if (data.hymn.chorus.any { chorus ->
                        chorus.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }) {
                    score += 20
                } else if (data.hymn.author.contains(searchQuery, ignoreCase = true)) {
                    score += 9
                }
            } else {
                score = 0
            }
            scores[data.hymn.hymn.toString()] = score
        }
        scores
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (hymnData.value.isEmpty()) {
            item {
                Text(
                    text = "No hymns found",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                )
            }
        } else {
            items(hymnData.value.sortedByDescending { hymnScores[it.hymn.hymn.toString()] }) { hymnData ->
                HymnCard(
                    hymnData = hymnData,
                    searchQuery = searchQuery,
                    toggleFavourite = toggleFavourite,
                    onClick = { onHymnClick(hymnData.hymn.hymn.toString()) }
                )
            }
        }
    }
}