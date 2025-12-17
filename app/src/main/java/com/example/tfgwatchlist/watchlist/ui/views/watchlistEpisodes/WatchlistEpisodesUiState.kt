package com.example.tfgwatchlist.watchlist.ui.views.watchlistEpisodes

import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsEpisodesResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse


sealed class WatchlistEpisodesUiState {
    data object Loading: WatchlistEpisodesUiState()
    data object Empty: WatchlistEpisodesUiState()
    data class Error(val message: String): WatchlistEpisodesUiState()
    data class ErrorEpisodes(val message: String): WatchlistEpisodesUiState()
    data class Success(val serieDetailsResponse: SerieDetailsResponse): WatchlistEpisodesUiState()
    data class SuccessEpisodes(val listaEpisodios: List<SerieDetailsEpisodesResponse>?): WatchlistEpisodesUiState()
}