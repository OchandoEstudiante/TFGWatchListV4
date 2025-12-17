package com.example.tfgwatchlist.watchlist.ui.views.watchlistSearch

import com.example.tfgwatchlist.watchlist.data.network.model.MediaItem

sealed class WatchlistUiState {
    data object Loading: WatchlistUiState()
    data object Empty: WatchlistUiState()
    data class Error(val message: String) : WatchlistUiState()
    data class Success(val items: List<MediaItem>): WatchlistUiState()
}