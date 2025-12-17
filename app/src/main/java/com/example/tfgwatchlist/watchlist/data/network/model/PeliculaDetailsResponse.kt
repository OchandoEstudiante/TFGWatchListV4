package com.example.tfgwatchlist.watchlist.data.network.model

import com.example.tfgwatchlist.watchlist.data.network.model.General.CreditsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.General.VideosResponse
import com.squareup.moshi.Json

data class PeliculaDetailsResponse (
    @Json(name="id") override val id: Int,
    @Json(name="title") override val titulo: String?,
    @Json(name="overview") override val premisa: String?,
    @Json(name="poster_path") override val poster: String?,
    @Json(name="backdrop_path") override val banner: String?,
    @Json(name="release_date")override val fechaEstreno: String?,
    @Json(name="genres")override val generos: List<GenerosMediaItemDetails> = listOf(),
    @Json(name="runtime") val duracion: Int,
    @Json(name="videos")override val videos: VideosResponse?,
    @Json(name="credits")override val credits: CreditsResponse?

): MediaItemDetails()