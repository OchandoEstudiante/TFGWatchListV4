package com.example.tfgwatchlist.yourwatchlist.data.network

import android.util.Log
import com.example.tfgwatchlist.yourwatchlist.data.network.api.YourWatchlistApiService
import com.example.tfgwatchlist.yourwatchlist.data.network.model.MovieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ServerStatus
import com.example.tfgwatchlist.yourwatchlist.data.network.model.TemporadaMongoItem
import com.example.tfgwatchlist.yourwatchlist.domain.YourWatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//Lmao, xd

class YourWatchlistRepositoryImpl(
    private val apiService: YourWatchlistApiService
): YourWatchlistRepository {
    override suspend fun checkServer(): ServerStatus {
        return apiService.getInfo()
    }

    override suspend fun fetchSeriesFlow(): Flow<List<SerieMongoItem>> {
        return flow{
            val seriesMongoResponse = apiService.getSeries()
            emit(seriesMongoResponse)
        }
    }

    override suspend fun fetchPeliculasFlow(): Flow<List<MovieMongoItem>> {
        return flow{
            val peliculasMongoResponse = apiService.getPeliculas()
            emit(peliculasMongoResponse)
        }
    }

    override suspend fun fetchPeliculasFiltered(filtro: String, query: String): Flow<List<MovieMongoItem>> {
        return flow{

        }
    }

    override suspend fun fetchSeriesFiltered(filtro: String, query: String): Flow<List<SerieMongoItem>> {
        return flow{

        }
    }

    override suspend fun deletePelicula(id: Int) {
        apiService.deleteMovie(id)
    }

    override suspend fun deleteSerie(id: Int) {
        apiService.deleteSerie(id)
    }

    override suspend fun changeStatePelicula(id: Int, state: String) {
        apiService.changeStateMovie(id, state)
    }

    override suspend fun changeStateSerie(id: Int, state: String) {
        apiService.changeStateSerie(id, state)
    }

    override suspend fun fetchSeasonsNamesFlow(id: Int): Flow<List<String>> {
        return flow{
            val seasonsMongoNames = apiService.getSeasonsNames(id)
            emit(seasonsMongoNames)
        }
    }

    override suspend fun fetchSeasonSeriesFlow(id: Int, numeroTemporada: Int): Flow<TemporadaMongoItem> {
        return flow{
            val seasonsMongoResponse = apiService.getSeasonSeries(id, numeroTemporada)
            emit(seasonsMongoResponse)
        }
    }

    override suspend fun addSerieToYourWatchlist() {

    }

    override suspend fun addMovieToYourWatchlist() {

    }
}