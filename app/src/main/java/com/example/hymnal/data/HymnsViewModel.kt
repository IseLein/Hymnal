package com.example.hymnal.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HymnsViewModel(private val hymnRepository: FavouriteHymnRepository): ViewModel() {
    val hymnState: StateFlow<List<FavouriteHymn>> = hymnRepository.favouriteHymns
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavourite(hymnId: String) {
        viewModelScope.launch {
            hymnRepository.toggleFavourite(hymnId)
        }
    }
}