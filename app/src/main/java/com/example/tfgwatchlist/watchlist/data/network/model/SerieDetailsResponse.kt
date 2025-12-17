package com.example.tfgwatchlist.watchlist.data.network.model

import com.example.tfgwatchlist.watchlist.data.network.model.General.CreditsResponse
import com.example.tfgwatchlist.watchlist.data.network.model.General.VideosResponse
import com.squareup.moshi.Json

data class SerieDetailsResponse (
    @Json(name="id") override val id: Int,
    @Json(name="name") override val titulo: String?,
    @Json(name="overview") override val premisa: String?,
    @Json(name="poster_path") override val poster: String?,
    @Json(name="backdrop_path") override val banner: String?,
    @Json(name="first_air_date")override val fechaEstreno: String?,
    @Json(name="number_of_seasons") val cantidadTemporadas: Int,
    @Json(name="genres")override val generos: List<GenerosMediaItemDetails> = listOf(),
    @Json(name="seasons") val temporadas: List<SerieDetailsSeasonsResponse> = listOf(),
    @Json(name="credits")override val credits: CreditsResponse?,
    @Json(name="videos")override val videos: VideosResponse?,
): MediaItemDetails()