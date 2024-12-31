package com.example.hymnal.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HymnsViewModel(private val hymnRepository: HymnRepository): ViewModel() {
    private val _hymns = MutableStateFlow<List<Hymn>>(emptyList())
    val hymns = _hymns.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _hymns.value = hymnRepository.loadHymns()
        }
    }
}