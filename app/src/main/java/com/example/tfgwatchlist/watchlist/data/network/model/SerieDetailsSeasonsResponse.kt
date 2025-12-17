package com.example.tfgwatchlist.watchlist.data.network.model

import com.squareup.moshi.Json

data class SerieDetailsSeasonsResponse (
    @Json(name = "id") val id: Int,
    @Json(name="name") val tituloTemporada: String?,
    @Json(name="air_date") val fechaEstrenoTemporada: String?,
    @Json(name="season_number") val numeroTemporada: Int,
    @Json(name="poster_path") val imagenTemporada: String?,
    @Json(name="overview") val premisaTemporada: String,
    @Json(name="episodes") val episodios: List<SerieDetailsEpisodesResponse>?
    )