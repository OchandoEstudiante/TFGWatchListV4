package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistDetailed

import com.example.tfgwatchlist.yourwatchlist.data.network.model.CastMongoItem

sealed class YourWatchlistDetailedUiState {
    data object Loading: YourWatchlistDetailedUiState()
    data object Empty: YourWatchlistDetailedUiState()
    data class Error(val message: String): YourWatchlistDetailedUiState()
    data class Success(val casts: List<CastMongoItem>): YourWatchlistDetailedUiState()
}