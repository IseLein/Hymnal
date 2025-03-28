package com.example.hymnal.data

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log // Import Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.IOException

class HymnAudioViewModel : ViewModel() {
    private var mediaPlayer: MediaPlayer? = null
    private var progressUpdateJob: Job? = null

    // Represents the availability of the audio file
    private val _isAudioAvailable = MutableStateFlow<Boolean?>(null) // null = loading/unknown, true = available, false = not found/error
    val isAudioAvailable = _isAudioAvailable.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration = _duration.asStateFlow()

    // Call this once when the screen loads or hymnId changes
    fun initializeAudio(context: Context, hymnNumber: String) {
        Log.d("HymnAudioViewModel", "Initializing audio for hymn: $hymnNumber")
        // Reset state and release previous player/job first
        releasePlayer()
        _isAudioAvailable.value = null // Set to loading/unknown state
        _isPlaying.value = false
        _progress.value = 0f
        _duration.value = 0L

        val audioPath = "audio/$hymnNumber.mp3"
        var afd: AssetFileDescriptor? = null

        try {
            // Check if asset exists *before* creating MediaPlayer
            afd = context.assets.openFd(audioPath)
            Log.d("HymnAudioViewModel", "Asset found: $audioPath")

            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                setOnPreparedListener { mp ->
                    Log.d("HymnAudioViewModel", "MediaPlayer prepared. Duration: ${mp.duration}")
                    _duration.value = mp.duration.toLong()
                    _isAudioAvailable.value = true // Mark as available *after* successful preparation
                    startProgressUpdates()
                }
                setOnCompletionListener {
                    Log.d("HymnAudioViewModel", "MediaPlayer completion.")
                    _isPlaying.value = false
                    _progress.value = 1f // Set progress to end
                    // Optionally seek to start: it.seekTo(0)
                }
                setOnErrorListener { mp, what, extra ->
                    Log.e("HymnAudioViewModel", "MediaPlayer Error - what: $what, extra: $extra")
                    _isAudioAvailable.value = false // Mark as unavailable on error
                    releasePlayer() // Clean up on error
                    true // Indicate error was handled
                }
                Log.d("HymnAudioViewModel", "Calling prepareAsync()")
                prepareAsync() // Use prepareAsync to avoid blocking main thread
            }
        } catch (e: FileNotFoundException) {
            Log.w("HymnAudioViewModel", "Audio file not found: $audioPath", e)
            _isAudioAvailable.value = false // Mark as unavailable
        } catch (e: IOException) {
            Log.e("HymnAudioViewModel", "IOException during MediaPlayer setup", e)
            _isAudioAvailable.value = false // Mark as unavailable
            releasePlayer()
        } catch (e: IllegalStateException) {
            Log.e("HymnAudioViewModel", "IllegalStateException during MediaPlayer setup", e)
            _isAudioAvailable.value = false // Mark as unavailable
            releasePlayer()
        } finally {
            // Make sure to close the AssetFileDescriptor
            try {
                afd?.close()
            } catch (e: IOException) {
                Log.e("HymnAudioViewModel", "Failed to close AssetFileDescriptor", e)
            }
        }
    }

    private fun startProgressUpdates() {
        progressUpdateJob?.cancel() // Cancel any existing job first
        progressUpdateJob = viewModelScope.launch {
            while (true) {
                if (_isPlaying.value && mediaPlayer?.isPlaying == true) {
                    try {
                        val currentPosition = mediaPlayer?.currentPosition ?: 0
                        val duration = mediaPlayer?.duration ?: 1 // Avoid division by zero
                        _progress.value = currentPosition.toFloat() / duration.toFloat()
                    } catch (e: IllegalStateException) {
                        Log.e("HymnAudioViewModel", "Error accessing MediaPlayer state in progress update", e)
                        // Might happen if player is released unexpectedly
                        _isPlaying.value = false // Stop trying to update
                        break // Exit the loop
                    }
                }
                delay(500)
            }
        }
    }


    fun playPause() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    Log.d("HymnAudioViewModel", "Pausing playback")
                    it.pause()
                    _isPlaying.value = false
                    progressUpdateJob?.cancel() // Stop progress updates while paused
                } else {
                    // Ensure player is ready before starting
                    if (_isAudioAvailable.value == true) {
                        Log.d("HymnAudioViewModel", "Starting playback")
                        it.start()
                        _isPlaying.value = true
                        startProgressUpdates() // Resume progress updates
                    } else {
                        Log.w("HymnAudioViewModel", "Play attempt when audio not available/ready.")
                    }
                }
            } catch (e: IllegalStateException) {
                Log.e("HymnAudioViewModel", "IllegalStateException during play/pause", e)
                // Reset or handle error appropriately
                releasePlayer()
                _isAudioAvailable.value = false
            }
        } ?: Log.w("HymnAudioViewModel", "playPause called but mediaPlayer is null")
    }

    fun seek(position: Float) {
        // Only seek if audio is available and prepared
        if (_isAudioAvailable.value == true) {
            mediaPlayer?.let {
                try {
                    val newPosition = (position * it.duration).toInt()
                    Log.d("HymnAudioViewModel", "Seeking to position: $newPosition ms ($position)")
                    it.seekTo(newPosition)
                    _progress.value = position // Update progress immediately for responsiveness
                } catch (e: IllegalStateException) {
                    Log.e("HymnAudioViewModel", "IllegalStateException during seek", e)
                }
            }
        } else {
            Log.w("HymnAudioViewModel", "Seek attempt when audio not available/ready.")
        }
    }

    fun forward5Seconds() {
        if (_isAudioAvailable.value == true) {
            mediaPlayer?.let {
                try {
                    val newPosition = minOf(it.currentPosition + 5000, it.duration)
                    Log.d("HymnAudioViewModel", "Forwarding to: $newPosition")
                    it.seekTo(newPosition)
                    _progress.value = newPosition.toFloat() / it.duration.toFloat()
                } catch (e: IllegalStateException) {
                    Log.e("HymnAudioViewModel", "IllegalStateException during forward", e)
                }
            }
        }
    }

    fun rewind5Seconds() {
        if (_isAudioAvailable.value == true) {
            mediaPlayer?.let {
                try {
                    val newPosition = maxOf(it.currentPosition - 5000, 0)
                    Log.d("HymnAudioViewModel", "Rewinding to: $newPosition")
                    it.seekTo(newPosition)
                    _progress.value = newPosition.toFloat() / it.duration.toFloat()
                } catch (e: IllegalStateException) {
                    Log.e("HymnAudioViewModel", "IllegalStateException during rewind", e)
                }
            }
        }
    }

    private fun releasePlayer() {
        Log.d("HymnAudioViewModel", "Releasing MediaPlayer")
        progressUpdateJob?.cancel()
        progressUpdateJob = null
        mediaPlayer?.release()
        mediaPlayer = null
        // Keep state related to availability/playing consistent
        _isPlaying.value = false
        // Don't reset _isAudioAvailable here, initializeAudio handles the next state
    }

    override fun onCleared() {
        Log.d("HymnAudioViewModel", "onCleared called")
        releasePlayer()
        super.onCleared()
    }
}