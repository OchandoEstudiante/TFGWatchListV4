package com.example.tfgwatchlist.yourwatchlist.domain

import com.example.tfgwatchlist.yourwatchlist.data.network.model.CastMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.MovieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ServerStatus
import com.example.tfgwatchlist.yourwatchlist.data.network.model.TemporadaMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.responses.postMovieResponse
import com.example.tfgwatchlist.yourwatchlist.data.network.model.responses.postSerieResponse
import kotlinx.coroutines.flow.Flow

interface YourWatchlistRepository {
    suspend fun checkServer(): ServerStatus
    suspend fun fetchSeriesFlow(userName: String): Flow<List<SerieMongoItem>>
    suspend fun fetchPeliculasFlow(userName: String): Flow<List<MovieMongoItem>>
    suspend fun fetchPeliculasFiltered(userName: String,filtro: String, query: String): Flow<List<MovieMongoItem>>
    suspend fun fetchSeriesFiltered(userName: String, filtro: String, query: String): Flow<List<SerieMongoItem>>
    suspend fun deletePelicula(userName: String, id: Int)
    suspend fun deleteSerie(userName: String, id: Int)
    suspend fun changeStatePelicula(userName: String, id:Int, state: String)
    suspend fun changeStateSerie(userName: String, id:Int, state: String)
    suspend fun fetchSeasonsNamesFlow(id: Int): Flow<List<String>>
    suspend fun fetchSeasonSeriesFlow(idSerie: Int, numeroTemporada: Int): Flow<TemporadaMongoItem>
    suspend fun fetchCastSeriesFlow(idSerie: Int): Flow<List<CastMongoItem>>
    suspend fun fetchCastMoviesFlow(idMovie: Int): Flow<List<CastMongoItem>>
    suspend fun addSerieToYourWatchlist(
        serieId: String,
        nombreUsuario: String
    ): Flow<postSerieResponse>
    suspend fun addMovieToYourWatchlist(
        movieId: String,
        nombreUsuario: String
    ): Flow<postMovieResponse>

    suspend fun userVerification()
}