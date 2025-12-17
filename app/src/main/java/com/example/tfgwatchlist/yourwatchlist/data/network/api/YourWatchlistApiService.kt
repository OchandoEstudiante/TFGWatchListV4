package com.example.tfgwatchlist.yourwatchlist.data.network.api

import android.R
import android.graphics.Movie
import com.example.tfgwatchlist.yourwatchlist.data.network.model.MovieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ServerStatus
import com.example.tfgwatchlist.yourwatchlist.data.network.model.TemporadaMongoItem
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface YourWatchlistApiService {
    @GET("check")
    suspend fun getInfo(): ServerStatus
    @GET("series/getAll")
    suspend fun getSeries(): List<SerieMongoItem>

    @GET("series/getFiltered/{filter}/{query}")
    suspend fun getSeriesFiltered(@Path("filter") filtro: String,
                                  @Path("query") busqueda: String): List<SerieMongoItem>

    @GET("movies/getAll")
    suspend fun getPeliculas(): List<MovieMongoItem>

    @GET("movies/getFiltered/{filter}/{query}")
    suspend fun getPeliculasFiltered(@Path("filter") filtro: String,
                                     @Path("query") busqueda: String): List<MovieMongoItem>

    @GET("series/{id}/getSeasonsNames")
    suspend fun getSeasonsNames(@Path("id") serieID: Int): List<String>
    @GET("series/{id}/season/{season_number}")
    suspend fun getSeasonSeries(@Path("id") serieID: Int?,
                                @Path("season_number") numeroTemporada: Int): TemporadaMongoItem
    @DELETE("movies/delete/{id}")
    suspend fun deleteMovie(@Path("id") peliculaID: Int)

    @DELETE("series/delete/{id}")
    suspend fun deleteSerie(@Path("id") serieID: Int)

    @PATCH("series/{id}/change/{state}")
    suspend fun changeStateSerie(@Path("id") serieID: Int,
                                 @Path("state") estadoCambio: String)

    @PATCH("movies/{id}/change/{state}")
    suspend fun changeStateMovie(@Path("id") movieID: Int,
                                 @Path("state") estadoCambio: String)
}