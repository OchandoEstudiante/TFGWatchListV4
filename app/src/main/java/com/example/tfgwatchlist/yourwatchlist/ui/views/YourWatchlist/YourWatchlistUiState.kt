package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlist

import com.example.tfgwatchlist.yourwatchlist.data.network.model.ItemMediaMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem

sealed class YourWatchlistUiState {
    data object Loading: YourWatchlistUiState()
    data object Empty: YourWatchlistUiState()
    data class Error(val message: String): YourWatchlistUiState()
    data class Success(val items: List<ItemMediaMongoItem>): YourWatchlistUiState()
}