package com.example.hymnal.data

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class FavouriteHymn(
    val hymn: Hymn,
    val isFavourite: Boolean
)

class FavouriteHymnRepository(
    hymnRepository: HymnRepository,
    private val favouriteRepository: FavouriteRepository
) {
    val favouriteHymns = combine(
        flowOf(hymnRepository.loadHymns()),
        favouriteRepository.favouritesFlow
    ) { hymns, favourites ->
        hymns.map { hymn ->
            FavouriteHymn(
                hymn = hymn,
                isFavourite = favourites.contains(hymn.hymn.toString())
            )
        }
    }

    suspend fun toggleFavourite(hymnId: String) {
        favouriteRepository.toggleFavourites(hymnId)
    }
}