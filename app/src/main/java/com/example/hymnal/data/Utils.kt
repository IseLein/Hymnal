package com.example.hymnal.data

fun formatCase(str: String): String {
    return str.lowercase().split(" ").joinToString(" ") {
        if (it[0] == '\'' || it[0] == '\u201c') {
            it.replaceFirst(it[1], it[1].uppercase()[0])
        } else {
            it.replaceFirstChar { char -> char.uppercase() }
        }
    }
}