package com.example.hymnal.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HymnsViewModelFactory(
    private val hymnRepository: FavouriteHymnRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HymnsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HymnsViewModel(hymnRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}