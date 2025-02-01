package com.example.hymnal.data

fun formatCase(str: String): String {
    return str.lowercase().split(" ").joinToString(" ") {
        if (it[0] == '\'') {
            it.replaceFirstChar { char -> char.uppercase() }
        }
        it.replaceFirstChar { char -> char.uppercase() }
    }
}