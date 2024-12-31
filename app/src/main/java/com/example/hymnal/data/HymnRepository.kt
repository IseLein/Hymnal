package com.example.hymnal.data

import android.content.Context
import kotlinx.serialization.json.Json

class HymnRepository(private val context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun loadHymns(): List<Hymn> {
        return try {
            val jsonString  = context.assets
                .open("hymns.json")
                .bufferedReader()
                .use { it.readText() }

            json.decodeFromString(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}