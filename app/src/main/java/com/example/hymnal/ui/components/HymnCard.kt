package com.example.hymnal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.hymnal.data.FavouriteHymn
import com.example.hymnal.data.formatCase

@Composable
fun HymnCard(hymnData: FavouriteHymn, searchQuery: String, toggleFavourite: (String) -> Unit) {
    val numWidth = with(LocalDensity.current) {
        rememberTextMeasurer().measure(
            text = "____",
            style = MaterialTheme.typography.bodyLarge
        ).size.width.toDp()
    }
    ListItem(
        leadingContent = {
            Text(
                text = hymnData.hymn.hymn.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.width(numWidth),
                textAlign = TextAlign.Center
            )
        },
        headlineContent = {
            val annotatedTitle = buildAnnotatedString {
                append(formatCase(hymnData.hymn.title))
                if (searchQuery.isNotEmpty()) {
                    val start = hymnData.hymn.title.indexOf(searchQuery, ignoreCase = true)
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
            val imageVector = if (hymnData.isFavourite) {
                Icons.Filled.Favorite
            } else {
                Icons.Outlined.FavoriteBorder
            }
            IconButton(onClick = {
                toggleFavourite(hymnData.hymn.hymn.toString())
            }) {
                Icon(
                    imageVector = imageVector,
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
