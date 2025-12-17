package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistEpisodes

import com.example.tfgwatchlist.yourwatchlist.data.network.model.TemporadaMongoItem

sealed class YourWatchlistEpisodesUiState {
    data object Loading: YourWatchlistEpisodesUiState()
    data object Empty: YourWatchlistEpisodesUiState()
    data class Error(val message: String): YourWatchlistEpisodesUiState()
    data class Success(val season: TemporadaMongoItem): YourWatchlistEpisodesUiState()
    data class SuccessNames(val temporadas: List<String>): YourWatchlistEpisodesUiState()
}