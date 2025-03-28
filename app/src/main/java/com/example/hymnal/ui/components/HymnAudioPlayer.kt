package com.example.hymnal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forward5
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay5
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hymnal.data.formatDuration
import kotlin.math.round

@Composable
fun HymnAudioPlayer(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    progress: Float,
    duration: Long,
    onPlayPause: () -> Unit,
    onSeek: (Float) -> Unit,
    onForward: () -> Unit,
    onRewind: () -> Unit
) {
    Surface(
        modifier = modifier,
        tonalElevation = 3.dp,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Slider(
                value = progress,
                onValueChange = onSeek,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${formatDuration(round(progress * duration).toLong())} / ${formatDuration(duration)}",
                    style = MaterialTheme.typography.bodySmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onRewind) {
                        Icon(
                            imageVector = Icons.Rounded.Replay5,
                            contentDescription = "Rewind 5 seconds"
                        )
                    }

                    FilledIconButton(onClick = onPlayPause) {
                        Icon(
                            imageVector = if (isPlaying)
                                Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play"
                        )
                    }

                    IconButton(onClick = onForward) {
                        Icon(
                            imageVector = Icons.Rounded.Forward5,
                            contentDescription = "Forward 5 seconds"
                        )
                    }
                }
            }
        }
    }
}