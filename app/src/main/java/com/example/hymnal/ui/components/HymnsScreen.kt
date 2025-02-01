package com.example.hymnal.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hymnal.data.Hymn
import com.example.hymnal.data.HymnRepository
import com.example.hymnal.data.HymnsViewModel
import com.example.hymnal.data.HymnsViewModelFactory
import com.example.hymnal.data.formatCase

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

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(filteredHymns) { hymn ->
            HymnCard(hymn, searchQuery)
        }
    }
}

@Composable
fun HymnCard(hymn: Hymn, searchQuery: String) {
    val numWidth = with(LocalDensity.current) {
        rememberTextMeasurer().measure(
            text = "____",
            style = MaterialTheme.typography.bodyLarge
        ).size.width.toDp()
    }
    ListItem(
        leadingContent = {
            Text(
                text = hymn.hymn.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.width(numWidth),
                textAlign = TextAlign.Center
            )
        },
        headlineContent = {
            val annotatedTitle = buildAnnotatedString {
                append(formatCase(hymn.title))
                if (searchQuery.isNotEmpty()) {
                    val start = hymn.title.indexOf(searchQuery, ignoreCase = true)
                    if (start != -1) {
                        addStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            ),
                            start = start,
                            end = start + searchQuery.length
                        )
                    }
                }
            }
            Text(
                text = annotatedTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingContent = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Clear"
                )
            }
        },
        supportingContent = {
            if (searchQuery.isNotEmpty()) {
                Text(
                    text = "let's go????",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
    )
}