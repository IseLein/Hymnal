package com.example.hymnal.data

import kotlinx.serialization.Serializable

@Serializable
data class Hymn(
    val hymn: Int,
    val title: String,
    val bible_ref: String,
    val key: String,
    val verses: List<String>,
    val chorus: List<String>,
    val author: String,
    val author_music: String,
    val meta_title: String,
    val meta_music: String
)