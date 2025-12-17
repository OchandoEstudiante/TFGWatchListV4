package com.example.tfgwatchlist.watchlist.data.network.api

import com.example.tfgwatchlist.watchlist.data.network.model.General.CreditsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.PeliculaDetailsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.PeliculasResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsSeasonsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SeriesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WatchlistApiService {
    // --- Series ---
    //Llamada simple a api que obtiene multiples series buscando por su nombre
    @GET("search/tv")
    suspend fun getSeriesSearch(@Query("query") query: String
    ): SeriesResponse
    //Llamada a Api para obtener datos mas especificos de la serie
    @GET("tv/{id}")
    suspend fun getDetailsSerie(@Path("id") serieId: String,
                                @Query("append_to_response") append: String = "videos,credits"
    ): SerieDetailsResponse
    //Llamada a Api para obtener datos detallados de una temporada
    @GET("tv/{id}/season/{season_number}")
    suspend fun getDetailsSeasons(@Path("id") serieId: String,
                                  @Path("season_number") numeroTemporada: Int
    ): SerieDetailsSeasonsResponse

    @GET("tv/{id}/credits")
    suspend fun getCreditsSerie(@Path("id") serieId: String)
    // --- Peliculas ---
    //Llamada simple a api que obtiene multiples peliculas buscando por su nombre
    @GET("search/movie")
    suspend fun getPeliculasSearch(@Query ("query") query: String
    ): PeliculasResponse

    //Llamada a Api para obtener datos detallados de una pelicula
    @GET("movie/{id}")
    suspend fun getDetailsMovie(@Path("id") movieId: String,
                                @Query("append_to_response") append: String = "videos,credits"
    ): PeliculaDetailsResponse
    //
    @GET("movie/{id}/credits")
    suspend fun getCreditsMovie(@Path("id")movieId: String): CreditsResponse
    //
}