package com.example.tfgwatchlist.watchlist.domain

import com.example.tfgwatchlist.watchlist.data.network.model.MediaItem
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItemDetails
import com.example.tfgwatchlist.watchlist.data.network.model.PeliculaDetailsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsEpisodesResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    suspend fun fetchItemsFlow(query: String): Flow<List<MediaItem>>

    suspend fun fetchPeliculasFlow(query: String): Flow<List<MediaItem>>

    suspend fun fetchSeriesFlow(query: String): Flow<List<MediaItem>>

    suspend fun fetchSerieDetails(id: String): Flow<MediaItemDetails>

    suspend fun fetchPeliculaDetails(id: String): Flow<MediaItemDetails>

    suspend fun fetchSerieEpisodes(id: String, number_season: Int): Flow<List<SerieDetailsEpisodesResponse>?>
}