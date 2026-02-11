package com.example.tfgwatchlist.yourwatchlist.data.network

import android.util.Log
import com.example.tfgwatchlist.yourwatchlist.data.network.api.YourWatchlistApiService
import com.example.tfgwatchlist.yourwatchlist.data.network.model.CastMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.MovieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ServerStatus
import com.example.tfgwatchlist.yourwatchlist.data.network.model.TemporadaMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.requests.postMovieRequest
import com.example.tfgwatchlist.yourwatchlist.data.network.model.requests.postSerieRequest
import com.example.tfgwatchlist.yourwatchlist.data.network.model.responses.postMovieResponse
import com.example.tfgwatchlist.yourwatchlist.data.network.model.responses.postSerieResponse
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

    override suspend fun fetchSeriesFlow(userName: String): Flow<List<SerieMongoItem>> {
        return flow{
            val seriesMongoResponse = apiService.getSeries(userName)
            emit(seriesMongoResponse)
        }
    }

    override suspend fun fetchPeliculasFlow(userName: String): Flow<List<MovieMongoItem>> {
        return flow{
            val peliculasMongoResponse = apiService.getPeliculas(userName)
            emit(peliculasMongoResponse)
        }
    }

    override suspend fun fetchPeliculasFiltered(userName: String, filtro: String, query: String): Flow<List<MovieMongoItem>> {
        var peliculasMongoResponse: List<MovieMongoItem>
        return flow{
            if(query.length == 0){
                peliculasMongoResponse = apiService.getMoviesFilteredState(userName, filtro)
            } else {
                if(filtro == "Todas"){
                    peliculasMongoResponse = apiService.getMoviesFilteredQuery(userName, query)
                } else {
                    peliculasMongoResponse = apiService.getMoviesFilteredStateAndQuery(userName, filtro, query)
                }
            }
            emit(peliculasMongoResponse)
        }
    }

    override suspend fun fetchSeriesFiltered(userName: String, filtro: String, query: String): Flow<List<SerieMongoItem>> {
        var seriesMongoResponse: List<SerieMongoItem>
        return flow{
            if(query.length == 0){
                seriesMongoResponse = apiService.getSeriesFilteredState(userName,filtro)
            } else {
                if(filtro == "Todas"){
                    seriesMongoResponse = apiService.getSeriesFilteredQuery(userName, query)
                } else {
                    seriesMongoResponse = apiService.getSeriesFilteredStateAndQuery(userName, filtro, query)
                }
            }
            emit(seriesMongoResponse)
        }
    }

    override suspend fun deletePelicula(userName: String,id: Int) {
        apiService.deleteMovie(userName, id)
    }

    override suspend fun deleteSerie(userName: String,id: Int) {
        apiService.deleteSerie(userName, id)
    }

    override suspend fun changeStatePelicula(userName: String, id: Int, state: String) {
        apiService.changeStateMovie(userName, id, state)
    }

    override suspend fun changeStateSerie(userName: String, id: Int, state: String) {
        apiService.changeStateSerie(userName, id, state)
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

    override suspend fun fetchCastSeriesFlow(idSerie: Int): Flow <List<CastMongoItem>> {
        return flow{
            val castMongoResponse = apiService.getCastSeries(idSerie)
            emit(castMongoResponse)
        }
    }

    override suspend fun fetchCastMoviesFlow(idMovie: Int): Flow <List<CastMongoItem>> {
        return flow{
            val castMongoResponse = apiService.getCastPeliculas(idMovie)
            emit(castMongoResponse)
        }
    }

    override suspend fun addSerieToYourWatchlist(
        serieId: String,
        nombreUsuario: String
    ): Flow<postSerieResponse> = flow{
        val postSerieResponse = apiService.postSerie(
            postSerieRequest(
                serieId,
                nombreUsuario
            )
        )
        if(postSerieResponse.isSuccessful){
            postSerieResponse.body()?.let { postSerieResponse ->
                emit(postSerieResponse)
            } ?: Exception("Response post serie empty")
        } else {
            throw Exception("Error register ${postSerieResponse.code()}")
        }
    }

    override suspend fun addMovieToYourWatchlist(
        movieId: String,
        nombreUsuario: String
    ): Flow<postMovieResponse> = flow {
        val postMovieResponse = apiService.postMovie(
            postMovieRequest(
                movieId,
                nombreUsuario
            )
        )
        if(postMovieResponse.isSuccessful){
            postMovieResponse.body()?.let{ postMovieResponse ->
                emit(postMovieResponse)
            } ?: Exception("Response post movie empty")
        } else {
            throw Exception("Error register ${postMovieResponse.code()}")
        }
    }

    override suspend fun userVerification() {
        TODO("Not yet implemented")
    }

}