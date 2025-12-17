package com.example.tfgwatchlist.watchlist.data.network

import android.util.Log
import com.example.tfgwatchlist.watchlist.data.network.api.WatchlistApiService
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItem
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItemDetails
import com.example.tfgwatchlist.watchlist.data.network.model.PeliculaDetailsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsEpisodesResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse
import com.example.tfgwatchlist.watchlist.domain.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WatchlistRepositoryImpl (
    private val apiService: WatchlistApiService
): WatchlistRepository{
    override suspend fun fetchItemsFlow(query: String): Flow<List<MediaItem>> {
        return flow{
            val seriesResponse = apiService.getSeriesSearch(query)
            val peliculasResponse = apiService.getPeliculasSearch(query)
            val combinedResults: List<MediaItem> = seriesResponse.results + peliculasResponse.results
            Log.i("Chando", "Empanada ${combinedResults}")
            emit(combinedResults)
            /*val response = apiService.getSeriesSearch(query) + apiService.getPeliculasSearch(query)
            Log.i("Chando", "Empanada ${response}")
            val series = response.results.map{it}
            emit(series) */
        }
    }

    override suspend fun fetchPeliculasFlow(query: String): Flow<List<MediaItem>> {
        return flow{
            val seriesResponse = apiService.getPeliculasSearch(query)
            emit(seriesResponse.results)
        }
    }

    override suspend fun fetchSeriesFlow(query: String): Flow<List<MediaItem>> {
        return flow{
            val peliculasResponse = apiService.getSeriesSearch(query)
            emit(peliculasResponse.results)
        }
    }

    override suspend fun fetchSerieDetails(id: String): Flow<MediaItemDetails> {
        return flow{
            val serieDetails = apiService.getDetailsSerie(id)
            emit(serieDetails)
        }
    }

    override suspend fun fetchPeliculaDetails(id: String): Flow<MediaItemDetails> {
        return flow{
            val peliculaDetails = apiService.getDetailsMovie(id)
            emit(peliculaDetails)
        }
    }

    override suspend fun fetchSerieEpisodes(id: String, number_season: Int): Flow<List<SerieDetailsEpisodesResponse>?> {
        return flow{
            val response = apiService.getDetailsSeasons(id, number_season)
            val episodios = response.episodios
            emit(episodios)
        }
    }
}