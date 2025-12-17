package com.example.tfgwatchlist.watchlist.ui.views.watchlistDetailed

import android.os.Message
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItemDetails
import com.example.tfgwatchlist.watchlist.data.network.model.PeliculaDetailsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsEpisodesResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse

sealed class WatchlistDetailsUiState {
    data object Loading: WatchlistDetailsUiState()
    data object Empty: WatchlistDetailsUiState()
    data class Error(val message: String): WatchlistDetailsUiState()
    data class SuccessMedia(val mediaDetails: MediaItemDetails): WatchlistDetailsUiState()
    /*
    data class ErrorCast(val message: String): WatchlistDetailsUiState()
    data class SuccessCast(): WatchlistDetailsUiState()
    data class ErrorVideos(val message: String): WatchlistDetailsUiState()
    data class SuccessVideos(): WatchlistDetailsUiState()
     */
}