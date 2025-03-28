package com.example.hymnal.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.hymnal.data.HymnAudioViewModel
import com.example.hymnal.data.HymnsViewModel
import com.example.hymnal.data.formatCase
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HymnDetailScreen(
    hymnId: String,
    viewModel: HymnsViewModel,
    audioViewModel: HymnAudioViewModel,
    onNavigateBack: () -> Unit
) {
    val hymnData by viewModel.hymnState.collectAsState()
    val currentHymn = hymnData.find { it.hymn.hymn.toString() == hymnId }
    val context = LocalContext.current

    LaunchedEffect(key1 = hymnId) {
        audioViewModel.initializeAudio(context, hymnId)
    }

    // handle lifecycle events
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                if (audioViewModel.isPlaying.value) {
                    audioViewModel.playPause()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            // release player?
        }
    }
    
    if (currentHymn == null) {
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
        return
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.surfaceContainer)

    val isAudioAvailable by audioViewModel.isAudioAvailable.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentHymn.hymn.hymn.toString() + ". " + formatCase(currentHymn.hymn.title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    val imageVector = if (currentHymn.isFavourite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    }
                    IconButton(onClick = { viewModel.toggleFavourite(hymnId) }) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = "Toggle favourite"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                windowInsets = WindowInsets(0.dp)
            )
        },
        bottomBar = {
            val showPlayer = (isAudioAvailable == true) && (scrollBehavior.state.collapsedFraction < 0.5f)
            AnimatedVisibility(
                visible = showPlayer,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                HymnAudioPlayer(
                    isPlaying = audioViewModel.isPlaying.collectAsState().value,
                    progress = audioViewModel.progress.collectAsState().value,
                    duration = audioViewModel.duration.collectAsState().value,
                    onPlayPause = audioViewModel::playPause,
                    onSeek = audioViewModel::seek,
                    onForward = audioViewModel::forward5Seconds,
                    onRewind = audioViewModel::rewind5Seconds
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = rememberLazyListState()
        ) {
            item {
                Text(
                    text = formatCase(currentHymn.hymn.title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }

            if (currentHymn.hymn.key.isNotEmpty()) {
                val annotatedText = buildAnnotatedString {
                    append("Key: ")
                    append(currentHymn.hymn.key)
                    addStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        ),
                        start = "Key: ".length,
                        end = "Key: ".length + currentHymn.hymn.key.length
                    )
                }
                item {
                    Text(
                        text = annotatedText,
                        style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 36.dp, vertical = 4.dp)
                    )
                }
            }
            if (currentHymn.hymn.bible_ref.isNotEmpty()) {
                item {
                    Text(
                        text = "\"" + currentHymn.hymn.bible_ref + "\"",
                        style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 36.dp, vertical = 4.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            currentHymn.hymn.verses.forEachIndexed { index, verse ->
                item {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 60.dp, vertical = 8.dp),
                        textAlign = TextAlign.End
                    )
                    verse.split("\n").forEach { line ->
                        Text(
                            text = line,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (currentHymn.hymn.chorus.isNotEmpty()) {
                        currentHymn.hymn.chorus.forEach { chorus ->
                            Text(
                                text = chorus,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            if (currentHymn.hymn.author.isNotEmpty()) {
                var metaTitle = currentHymn.hymn.meta_title
                if (metaTitle.isNotEmpty()) {
                    metaTitle = "; $metaTitle"
                }
                item {
                    val annotatedText = buildAnnotatedString {
                        append("Author: ")
                        append(currentHymn.hymn.author)
                        append(metaTitle)
                        addStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold
                            ),
                            start = 0,
                            end = "Author:".length
                        )
                    }
                    Text(
                        text = annotatedText,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 4.dp),
                    )
                }
            }
            if (currentHymn.hymn.author_music.isNotEmpty()) {
                var metaMusic = currentHymn.hymn.meta_music
                if (metaMusic.isNotEmpty()) {
                    metaMusic = "; $metaMusic"
                }
                val annotatedText = buildAnnotatedString {
                    append("Music: ")
                    append(currentHymn.hymn.author_music)
                    append(metaMusic)
                    addStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        ),
                        start = 0,
                        end = "Music:".length
                    )
                }
                item {
                    Text(
                        text = annotatedText,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 4.dp),
                    )
                }
            }
        }
    }
}