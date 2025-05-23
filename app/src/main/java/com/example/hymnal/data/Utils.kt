package com.example.hymnal.data

import java.util.Locale
import java.util.concurrent.TimeUnit

fun formatCase(str: String): String {
    return str.lowercase().split(" ").joinToString(" ") {
        if (it[0] == '\'' || it[0] == '\u201c') {
            it.replaceFirst(it[1], it[1].uppercase()[0])
        } else {
            it.replaceFirstChar { char -> char.uppercase() }
        }
    }
}

fun formatDuration(durationMs: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
    return String.format(Locale.ROOT, "%d:%02d", minutes, seconds)
}