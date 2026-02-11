package com.example.tfgwatchlist.yourwatchlist.data.network.api

import android.R
import android.graphics.Movie
import com.example.tfgwatchlist.yourwatchlist.data.network.model.CastMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.MovieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ServerStatus
import com.example.tfgwatchlist.yourwatchlist.data.network.model.TemporadaMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.requests.postMovieRequest
import com.example.tfgwatchlist.yourwatchlist.data.network.model.requests.postSerieRequest
import com.example.tfgwatchlist.yourwatchlist.data.network.model.responses.postMovieResponse
import com.example.tfgwatchlist.yourwatchlist.data.network.model.responses.postSerieResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface YourWatchlistApiService {
    @GET("check")
    suspend fun getInfo(): ServerStatus
    @GET("seriesMongo/getAllSeries/{userName}")
    suspend fun getSeries(@Path("userName") nombreUsuario: String): List<SerieMongoItem>

    @GET("seriesMongo/getSeriesFiltered/{userName}/filter/{filter}")
    suspend fun getSeriesFilteredState(@Path("userName") nombreUsuario: String,
                                       @Path("filter") filtro: String): List<SerieMongoItem>

    @GET("seriesMongo/getSeriesFiltered/{userName}/query/{query}")
    suspend fun getSeriesFilteredQuery(@Path("userName") nombreUsuario: String,
                                       @Path("query") query: String): List<SerieMongoItem>

    @GET("seriesMongo/getSeriesFiltered/{userName}/filter/{filter}/query/{query}")
    suspend fun getSeriesFilteredStateAndQuery(@Path("userName") nombreUsuario: String,
                                               @Path("filter") filtro: String,
                                               @Path("query") query: String): List<SerieMongoItem>

    @GET("seriesMongo/getSerieSeasonsNames/{idSerie}")
    suspend fun getSeasonsNames(@Path("idSerie") serieID: Int): List<String>

    @GET("seriesMongo/getSeason/{idSerie}/season/{season_number}")
    suspend fun getSeasonSeries(@Path("idSerie") serieID: Int?,
                                @Path("season_number") numeroTemporada: Int): TemporadaMongoItem
    //LMAO
    @GET("seriesMongo/getSeriesCast/{idSerie}/cast")
    suspend fun getCastSeries(@Path("idSerie") serieID: Int):List<CastMongoItem>
    @GET("moviesMongo/getAllMovies/{userName}")
    suspend fun getPeliculas(@Path("userName") nombreUsuario: String): List<MovieMongoItem>

    @GET("moviesMongo/getMoviesFiltered/{userName}/filter/{state}")
    suspend fun getMoviesFilteredState(@Path("userName") nombreUsuario: String,
                                       @Path("state") filtro: String): List<MovieMongoItem>

    @GET("moviesMongo/getMoviesFiltered/{userName}/query/{query}")
    suspend fun getMoviesFilteredQuery(@Path("userName") nombreUsuario: String,
                                       @Path("query") query: String): List<MovieMongoItem>

    @GET("moviesMongo/getMoviesFiltered/{userName}/filter/{state}/query/{query}")
    suspend fun getMoviesFilteredStateAndQuery(@Path("userName") nombreUsuario: String,
                                               @Path("state") filtro: String,
                                               @Path("query") query: String): List<MovieMongoItem>

    @GET("moviesMongo/getMoviesCast/{idMovie}/cast")
    suspend fun getCastPeliculas(@Path("idMovie") movieID: Int): List<CastMongoItem>

    @DELETE("moviesMongo/deleteMovie/{userName}/id/{id}")
    suspend fun deleteMovie(@Path("userName") nombreUsuario: String,
                            @Path("id") peliculaID: Int)

    @DELETE("seriesMongo/deleteSeries/{userName}/{id}")
    suspend fun deleteSerie(@Path("userName") nombreUsuario: String,
                            @Path("id") serieID: Int)

    @PATCH("seriesMongo/changeStateSeries/{userName}/{serieId}/state/{state}")
    suspend fun changeStateSerie(@Path("userName") nombreUsuario: String,
                                 @Path("serieId") serieID: Int,
                                 @Path("state") estadoCambio: String)

    @PATCH("moviesMongo/changeStateMovies/{userName}/{id}/state/{state}")
    suspend fun changeStateMovie(@Path("userName") nombreUsuario: String,
                                 @Path("id") movieID: Int,
                                 @Path("state") estadoCambio: String)

    @POST("moviesMongo/addMovie")
    suspend fun postMovie(
        @Body request: postMovieRequest
    ): Response<postMovieResponse>

    @POST("seriesMongo/addSerie")
    suspend fun postSerie(
        @Body request: postSerieRequest
    ): Response<postSerieResponse>

}