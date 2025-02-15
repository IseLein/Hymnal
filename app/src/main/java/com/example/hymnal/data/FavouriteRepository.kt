package com.example.hymnal.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "hymnal_preferences")

class FavouriteRepository(private val context: Context) {
    companion object {
        val FAVOURITES = stringSetPreferencesKey("favourites")
    }

    val favouritesFlow = context.dataStore.data.map { preferences ->
        preferences[FAVOURITES] ?: emptySet()
    }

    suspend fun toggleFavourites(hymnId: String) {
        context.dataStore.edit { preferences ->
            val favourites = preferences[FAVOURITES] ?: emptySet()
            if (favourites.contains(hymnId)) {
                preferences[FAVOURITES] = favourites.minus(hymnId)
            } else {
                preferences[FAVOURITES] = favourites.plus(hymnId)
            }
        }
    }
}
