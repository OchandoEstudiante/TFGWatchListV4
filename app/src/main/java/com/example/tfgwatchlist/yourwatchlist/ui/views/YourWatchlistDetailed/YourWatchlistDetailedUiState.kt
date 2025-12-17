package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistDetailed

sealed class YourWatchlistDetailedUiState {
    data object Loading: YourWatchlistDetailedUiState()
    data object Empty: YourWatchlistDetailedUiState()
}