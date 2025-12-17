package com.example.tfgwatchlist.yourwatchlist.domain

import com.example.tfgwatchlist.yourwatchlist.data.network.model.MovieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ServerStatus
import com.example.tfgwatchlist.yourwatchlist.data.network.model.TemporadaMongoItem
import kotlinx.coroutines.flow.Flow

interface YourWatchlistRepository {
    suspend fun checkServer(): ServerStatus
    suspend fun fetchSeriesFlow(): Flow<List<SerieMongoItem>>
    suspend fun fetchPeliculasFlow(): Flow<List<MovieMongoItem>>
    suspend fun fetchPeliculasFiltered(filtro: String, query: String): Flow<List<MovieMongoItem>>
    suspend fun fetchSeriesFiltered(filtro: String, query: String): Flow<List<SerieMongoItem>>
    suspend fun deletePelicula(id: Int)
    suspend fun deleteSerie(id: Int)
    suspend fun changeStatePelicula(id:Int, state: String)
    suspend fun changeStateSerie(id:Int, state: String)
    suspend fun fetchSeasonsNamesFlow(id: Int): Flow<List<String>>
    suspend fun fetchSeasonSeriesFlow(idSerie: Int, numeroTemporada: Int): Flow<TemporadaMongoItem>

    suspend fun addSerieToYourWatchlist()

    suspend fun addMovieToYourWatchlist()
}